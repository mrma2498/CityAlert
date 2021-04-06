package ipvc.estg.cityalert

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_perfil_utilizador.*

class PerfilUtilizador : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val PaginaINICIAL = 6


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_utilizador)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false



        fabAdd.setOnClickListener {
            Log.d("NOTICE","Adicionar irregularidade")
        }



        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.backMenu -> {
                    // Respond to navigation item 1 click
                    val intent = Intent(this@PerfilUtilizador, PaginaInicial::class.java)
                    startActivityForResult(intent, PaginaINICIAL)
                    true
                }


                R.id.logoutButton -> {
                    // Respond to navigation item 3 click
                    val sharedPref: SharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
                    with(sharedPref.edit()){
                        remove("isUserLogged")
                        commit()
                    }

                    val intent = Intent(this@PerfilUtilizador, PaginaInicial::class.java)
                    startActivityForResult(intent, PaginaINICIAL)
                    true
                }

                else -> false
            }
        }

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}