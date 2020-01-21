package estudo.com.mapalocal.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.TITLE_HOME
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private var isLight = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        configuraToolbar()
    }

    private fun configuraToolbar() {
        setSupportActionBar(activity_main_toolbar)
        supportActionBar?.title = TITLE_HOME
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setOnMapLongClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        val search: MenuItem = menu.findItem(R.id.activity_main_menu_search)
        val searchView: SearchView = search.actionView as SearchView
        searchView.queryHint = "Busca Local"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {/*realiza a busca interna*/return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mapa_dark -> {
                if (isLight) {
                    title = "Mapa Diurno"
                    mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            this,
                            R.raw.style_noturno
                        )
                    )
                } else {
                    title = "Mapa Noturno"
                    mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                            this,
                            R.raw.style_sem_novidades
                        )
                    )
                }
                isLight = !isLight
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapLongClick(latLng: LatLng) {
        val objectJson = Gson()
        val intentFormulario = Intent(this, FormularioLocalActivity::class.java)
        val objetoTransf = objectJson.toJson(latLng)
//        intentFormulario.putExtra(objetoTransf, PATH_FORMULARIO)
        startActivity(intentFormulario)
    }

}
