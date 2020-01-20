package estudo.com.mapalocal

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val bottomSheet :  View = bottom_sheet
        val mBottomSheetBehavioer : BottomSheetBehavior<View> = BottomSheetBehavior.from(bottomSheet)


        image_up.setOnClickListener {
            mBottomSheetBehavioer.state = BottomSheetBehavior.STATE_EXPANDED
            image_up.visibility = View.GONE
            image_down.visibility = View.VISIBLE
        }

        image_down.setOnClickListener {
            mBottomSheetBehavioer.state = BottomSheetBehavior.STATE_COLLAPSED
            image_up.visibility = View.VISIBLE
            image_down.visibility = View.GONE
        }

        mBottomSheetBehavioer.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){

            override fun onSlide(bottomSheet: View, slideOffset: Float) {  /*animação */  }

            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        mBottomSheetBehavioer.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        mBottomSheetBehavioer.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}
