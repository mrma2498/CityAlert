package ipvc.estg.cityalert

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.api.EndPoints
import ipvc.estg.cityalert.api.Irregularidade
import ipvc.estg.cityalert.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_perfil_utilizador.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilUtilizador : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val PaginaINICIAL = 6
    private lateinit var irregularidades: List<Irregularidade>
    private val NotesRequestActivity = 9


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_utilizador)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)





        /**
         * Fab para redirecionar para o menu das notas.
         * */
        notasButton.setOnClickListener {
            val intent = Intent(this@PerfilUtilizador, MainActivity::class.java)
            startActivityForResult(intent,NotesRequestActivity)
        }

        /**
         * Fab para fazer logout da conta.
         * */
        logoutButton.setOnClickListener {
            val sharedPref: SharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                remove("isUserLogged")
                remove("idUti")
                commit()
            }

            val intent = Intent(this@PerfilUtilizador, PaginaInicial::class.java)
            startActivityForResult(intent, PaginaINICIAL)
        }



        /**
         * Introduz no mapa todas as irregularidades provenientes do WS.
         * */
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getIrregularidades()
        var position: LatLng
        var idUtilizadorIrr: Int

        call.enqueue(object : Callback<List<Irregularidade>> {


            override fun onResponse(call: Call<List<Irregularidade>>, response: Response<List<Irregularidade>>) {
               if(response.isSuccessful){
                   irregularidades = response.body()!!
                   for (ir in irregularidades){
                       position = LatLng(ir.latitude,ir.longitude)
                       idUtilizadorIrr = ir.id_utilizador;

                       //if (utilizadorLogado(passado através do shared preferences) == ir.idutilizador)
                       mMap.addMarker(MarkerOptions().position(position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(ir.nome))
                       //cor verde
                       //else vermelho
                   }
               }
            }

            override fun onFailure(call: Call<List<Irregularidade>>, t: Throwable) {
                Log.d("ERRO", "Erro ao carregar irregularidades")
            }


        })



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
        val portugal = LatLng(39.557191, -7.8536599)

        //mMap.addMarker(MarkerOptions().position(portugal).title("Marker in Portugal"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(portugal))
    }

    override fun onBackPressed() {

        val snackbarBack = findViewById<View>(R.id.perfilutilizador)
        Snackbar.make(snackbarBack, "You have to logout!", Snackbar.LENGTH_SHORT) //Criar string
            .show()
    }

}

