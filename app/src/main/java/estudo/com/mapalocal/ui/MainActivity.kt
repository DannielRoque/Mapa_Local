package estudo.com.mapalocal.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.dynamic.IObjectWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.CODE_ERRO
import estudo.com.mapalocal.constantes.HINT_SEARCH
import estudo.com.mapalocal.constantes.PATH_FORMULARIO
import estudo.com.mapalocal.constantes.TITLE_HOME
import estudo.com.mapalocal.dao.LocalDAO
import estudo.com.mapalocal.modelo.Categoria
import estudo.com.mapalocal.modelo.Local
import estudo.com.mapalocal.ui.adapter.ActivityHomeAdapter
import estudo.com.mapalocal.ui.adapter.OnItemCLickListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.marker_customizado.*
import kotlinx.android.synthetic.main.marker_customizado.view.*
import java.lang.Double.parseDouble

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var latlong: LatLng
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var isLight = true
    private var isTerrain = true
    private var isSatelite = true
    private val dao = LocalDAO(this)
    private lateinit var adapter: ActivityHomeAdapter
    private var client: FusedLocationProviderClient? = null
    private var listaCategorias: MutableList<Categoria> = arrayListOf()
    private var listaLocais: MutableList<Local> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configuraToolbar()
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
        configuraListaLocaisComTodosRetornadosBD()

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
        intentFormulario.putExtra(PATH_FORMULARIO, objetoTransf)
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
        listaCategorias = dao.selectAllCategorias()
        configuraListaCategoriasHome()
        configuraCliqueListaCategoria()
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

    private fun configuraListaLocaisComTodosRetornadosBD() {
        mMap.clear()
        val listaMarkers: MutableList<LatLng> = arrayListOf()
        var caminho : Int? = null
        listaLocais = dao.selectAllLocal()
        Log.e("teste", "listaAllLocal $listaLocais")

        if (!listaLocais.equals("")) {
            for (local in listaLocais) {
                latitude = parseDouble(local.latitude)
                longitude = parseDouble(local.longitude)
                latlong = LatLng(latitude, longitude)
                listaCategorias = dao.buscaTodasCategoriasOndeLocal(local.descricao)!!
                for (cat in listaCategorias){
                    caminho=cat.caminhoIcone!!
                }
                mMap.addMarker(MarkerOptions().position(latlong).title(local.descricao).icon(BitmapDescriptorFactory.fromBitmap(bitmapDescriptor(this, caminho!!))))
                listaMarkers.add(latlong)
            }
        }
        //bounds não funfa
        chamaBounds(listaMarkers)
    }

    //bounds não funfa
    private fun chamaBounds(lista: MutableList<LatLng>) {
        if (lista.isNotEmpty()) {
            val padding: Int = 0
            val builder = LatLngBounds.Builder()
            for (marker in lista) {
                builder.include(marker)
            }
            val bounds: LatLngBounds = builder.build()
            val cu: CameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            mMap.moveCamera(cu)
        }
    }

    private fun configuraListaCategoriasHome() {
        adapter = ActivityHomeAdapter(listaCategorias)
        activity_main_recycler_view.adapter = adapter
    }

    private fun configuraCliqueListaCategoria() {
        adapter.setOnItemClickListener(object : OnItemCLickListener {
            override fun onItemClick(view: String, position: Int) {
                val categoria: Categoria =
                    Gson().fromJson(view, object : TypeToken<Categoria>() {}.type)
                val dados = dao.buscaTodosLocaisClicandoCategoria(categoria.descricao)
                Log.e("teste", "click: ${dados.toString()}")

                if (dados != null) {
                    mMap.clear()
                    for (m in dados) {
                        latitude = parseDouble(m.latitude)
                        longitude = parseDouble(m.longitude)
                        latlong = LatLng(latitude, longitude)
                        mMap.addMarker(
                            MarkerOptions().position(latlong).title(m.descricao).icon(
                                BitmapDescriptorFactory.fromBitmap(
                                    bitmapDescriptor(
                                        this@MainActivity,
                                        categoria.caminhoIcone!!
                                    )
                                )
                            )
                        )
                    }
                }
            }
        })
    }

    private fun bitmapDescriptor(context: Context, @DrawableRes resId: Int): Bitmap {

        val marker: View = LayoutInflater.from(context).inflate(R.layout.marker_customizado, null)

        var campo_icone_marker: ImageView = marker.my_card_image_marker
        campo_icone_marker.setImageResource(resId)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)

        val bitmap: Bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)
        return bitmap
    }

}
