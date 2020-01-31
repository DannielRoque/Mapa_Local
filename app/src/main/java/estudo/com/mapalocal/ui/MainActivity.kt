package estudo.com.mapalocal.ui

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.*
import estudo.com.mapalocal.dao.LocalDAO
import estudo.com.mapalocal.modelo.Categoria
import estudo.com.mapalocal.modelo.Local
import estudo.com.mapalocal.ui.adapter.ActivityHomeAdapter
import estudo.com.mapalocal.ui.adapter.OnItemCLickListener
import estudo.com.mapalocal.ui.adapter.OnItemLongClickListener
import estudo.com.mapalocal.ui.helper.ActivityHelper
import estudo.com.mapalocal.ui.infowindow.InfoWindowPersonalizado
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_options.*
import java.lang.Double.parseDouble

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
    GoogleMap.OnInfoWindowClickListener {

    private lateinit var webView: WebView
    private lateinit var mMap: GoogleMap
    private lateinit var help: ActivityHelper
    private lateinit var latlong: LatLng
    private var isLight = true
    private var isTerrain = true
    private var isSatelite = true
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private val dao = LocalDAO(this)
    private lateinit var adapter: ActivityHomeAdapter
    private var client: FusedLocationProviderClient? = null
    private var listaCategorias: MutableList<Categoria> = arrayListOf()
    private var listaLocais: MutableList<Local> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configuraToolbar()
        help = ActivityHelper(this)

        webView = webview_activity
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView, url : String): Boolean{
                view.loadUrl(url)
                return true
            }
        }

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
        mMap.setOnInfoWindowClickListener(this)
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

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.equals("")) {
                    configuraSearch(newText!!)
                }
                return false
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
        startActivity(intentFormulario)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CODE_ERRO -> {
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
        configuraLongCliqueLista()
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
                GoogleApiAvailability.getInstance().getErrorDialog(this, errorCode, CODE_ERRO)
                { finish() }.show()
            }
            ConnectionResult.SUCCESS -> {
            }
        }

        client?.lastLocation?.addOnSuccessListener { sucess ->
            sucess?.let {
                val origem = LatLng(it.latitude, it.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origem, 15f))
            }
        }?.addOnFailureListener { }
    }

    private fun configuraSearch(description: String) {
        val listaPosicaoSearch: MutableList<LatLng> = arrayListOf()
        val caminho: Int? = null
        if (!description.equals(null)) {
            val listaSearch = dao.selectLocal(description)
            for (busca in listaSearch) {
                latitude = parseDouble(busca.latitude)
                longitude = parseDouble(busca.longitude)
                latlong = LatLng(latitude, longitude)
                listaPosicaoSearch.add(latlong)
                listaCategorias = dao.buscaTodasCategoriasOndeLocal(busca.descricao)!!

                configuraMarkerPersonalizado(caminho, busca)
            }
            chamaBounds(listaPosicaoSearch)
        }
    }

    private fun configuraListaLocaisComTodosRetornadosBD() {
        mMap.clear()
        val caminho: Int? = null
        listaLocais = dao.selectAllLocal()

        if (!listaLocais.equals("")) {
            for (local in listaLocais) {
                latitude = parseDouble(local.latitude)
                longitude = parseDouble(local.longitude)
                latlong = LatLng(latitude, longitude)
                listaCategorias = dao.buscaTodasCategoriasOndeLocal(local.descricao)!!
                configuraMarkerPersonalizado(caminho, local)
            }
        }
    }

    private fun configuraMarkerPersonalizado(
        caminho: Int?,
        local: Local
    ) {
        var caminho1 = caminho
        for (cat in listaCategorias) {
            caminho1 = cat.caminhoIcone!!
        }

        val addMarker = mMap.addMarker(
            MarkerOptions().position(latlong)
                .title(local.descricao)
                .snippet(local.telefone)
                .icon(BitmapDescriptorFactory.fromBitmap(help.bitmapDescriptor(this, caminho1!!)))
        )
        val infoWindow: GoogleMap.InfoWindowAdapter = InfoWindowPersonalizado(this, local)
        mMap.setInfoWindowAdapter(infoWindow)
        addMarker.tag


        mMap.setOnInfoWindowClickListener {


            it?.hideInfoWindow()
            val dialog = Dialog(this)
            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(layoutInflater.inflate(R.layout.dialog_options, null))
            dialog.show()

            val ligar: TextView = dialog.options_ligar
            val site: TextView = dialog.options_site
            val editar: TextView = dialog.options_editar
            val excluir: TextView = dialog.options_excluir

            ligar.setOnClickListener {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        PHONE
                    )
                } else {
                    val intentLigar = Intent(Intent.ACTION_CALL)
                    intentLigar.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intentLigar.data = Uri.parse("tel:" + local.telefone)
                    startActivity(intentLigar)
                    dialog.dismiss()
                }
            }


            site.setOnClickListener {
            var versite : String =local.site
            if (!versite.startsWith("https://")){
                versite = "https://$versite"
            }
                dialog.dismiss()
                webView.loadUrl(versite)
                webView.visibility=View.VISIBLE
            }
            editar.setOnClickListener {
                Log.e("teste", "edit")
            }
            excluir.setOnClickListener {
                Log.e("teste", "delete")
            }
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

                if (dados != null) {
                    mMap.clear()
                    val listaMarkers: MutableList<LatLng> = arrayListOf()
                    for (m in dados) {
                        latitude = parseDouble(m.latitude)
                        longitude = parseDouble(m.longitude)
                        latlong = LatLng(latitude, longitude)
                        mMap.addMarker(
                            MarkerOptions().position(latlong).title(m.descricao).icon(
                                BitmapDescriptorFactory.fromBitmap(
                                    help.bitmapDescriptor(
                                        this@MainActivity,
                                        categoria.caminhoIcone!!
                                    )
                                )
                            )
                        )
                        listaMarkers.add(latlong)
                        chamaBounds(listaMarkers)
                    }
                }
            }
        })
    }

    private fun configuraLongCliqueLista() {
        adapter.setOnLongClickListener(object : OnItemLongClickListener {
            override fun onItemLongClick(view: String, position: Int): Boolean {
                Toast.makeText(this@MainActivity, "teste", Toast.LENGTH_LONG).show()
                return false
            }
        })
    }

    private fun chamaBounds(lista: MutableList<LatLng>) {
        if (lista.isNotEmpty()) {
            val padding = 150
            val builder = LatLngBounds.Builder()
            for (marker in lista) {
                builder.include(marker)
            }
            val bounds: LatLngBounds = builder.build()
            val cu: CameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
            mMap.animateCamera(cu)
            mMap.minZoomLevel
        }
    }



    override fun onInfoWindowClick(marker: Marker?) {

    }
}
