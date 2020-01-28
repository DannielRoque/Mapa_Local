package estudo.com.mapalocal.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.CODE_ERRO
import estudo.com.mapalocal.constantes.HINT_SEARCH
import estudo.com.mapalocal.constantes.PATH_FORMULARIO
import estudo.com.mapalocal.constantes.TITLE_HOME
import estudo.com.mapalocal.dao.LocalDAO
import estudo.com.mapalocal.modelo.Categoria
import estudo.com.mapalocal.ui.adapter.ActivityHomeAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private var isLight = true
    private var isSatelite = true
    private var isTerrain = true
    private var client: FusedLocationProviderClient? = null
    private lateinit var adapter: ActivityHomeAdapter
    private val dao = LocalDAO(this)
    private var listaCategorias: MutableList<Categoria> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configuraToolbar()
        configuraListaCategoriasHome()

    }

    private fun configuraListaCategoriasHome() {
        listaCategorias = dao.selectAllCategorias()
        adapter = ActivityHomeAdapter(listaCategorias)
        activity_main_recycler_view.adapter = adapter
    }

    private fun configuraToolbar() {
        setSupportActionBar(activity_main_toolbar)
        supportActionBar?.title = TITLE_HOME
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (!mMap.equals(null)) {
            val permission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (permission == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            }
        }
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.setOnMapLongClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        configuracaoInflaMenu(menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun configuracaoInflaMenu(menu: Menu) {
        val search: MenuItem = menu.findItem(R.id.activity_main_menu_search)
        val searchView: SearchView = search.actionView as SearchView
        searchView.queryHint = HINT_SEARCH
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {/*realiza a busca interna*/return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mapa_satelite -> {
                configuraMapaSatelital()
            }
            R.id.mapa_hibrido -> {
                configuraMapaHibrido()
            }
            R.id.mapa_terreno -> {
                configuraMapaTerreno()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configuraMapaTerreno() {
        if (isTerrain) {
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
        } else {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
        isTerrain = !isTerrain
        isSatelite = true
        isLight = true
    }

    private fun configuraMapaHibrido() {
        if (isLight) {
            mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        } else {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
        isLight = !isLight
        isSatelite = true
        isTerrain = true
    }

    private fun configuraMapaSatelital() {
        if (isSatelite) {
            mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        } else {
            mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
        isSatelite = !isSatelite
        isLight = true
        isTerrain = true
    }

    override fun onMapLongClick(latLng: LatLng) {
        val objectJson = Gson()
        val intentFormulario = Intent(this, FormularioLocalActivity::class.java)
        val objetoTransf = objectJson.toJson(latLng)
        intentFormulario.putExtra(objetoTransf, PATH_FORMULARIO)
        Log.e("Teste", "LongCLick $latLng")
        startActivity(intentFormulario)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CODE_ERRO -> {
                Log.e("Teste", "requestCode $requestCode e $CODE_ERRO")
                if ((grantResults.isEmpty()) or (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    finish()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), CODE_ERRO
                )
            }
        } else {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)

            client = LocationServices.getFusedLocationProviderClient(this)
        }

        when (val errorCode =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)) {
            ConnectionResult.SERVICE_MISSING, ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED,
            ConnectionResult.SERVICE_DISABLED -> {
                Log.e("Teste", "Show dialog erro services")
                GoogleApiAvailability.getInstance().getErrorDialog(this, errorCode, CODE_ERRO)
                { finish() }.show()
            }
            ConnectionResult.SUCCESS -> {
                Log.e("Teste", "services atualizado")
            }
        }

        client?.lastLocation?.addOnSuccessListener { sucess ->
            sucess?.let {
                val origem = LatLng(it.latitude, it.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origem, 15f))
            }
        }?.addOnFailureListener { }
    }
}
