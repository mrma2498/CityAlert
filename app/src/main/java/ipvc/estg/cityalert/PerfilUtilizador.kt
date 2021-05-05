package ipvc.estg.cityalert

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.MediaStore.Video.VideoColumns.CATEGORY
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.api.EndPoints
import ipvc.estg.cityalert.api.Irregularidade
import ipvc.estg.cityalert.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_perfil_utilizador.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PerfilUtilizador : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private lateinit var mMap: GoogleMap
    private val PaginaINICIAL = 6
    private lateinit var irregularidades: List<Irregularidade>
    private val NotesRequestActivity = 9
    private val NovaIrregularidadeRequestActivity = 10
    private val verIrregularidadeREQUESTCODE = 15

    lateinit var position: LatLng

    /**Guarda a id do utilizador que registou a irregularidade*/
    var idUtilizadorIrr: Int = 0

    var idIrregularidade: Int = 0

    var idLogin: Int = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_utilizador)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val sharedPref: SharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        /**
         * Fab para redirecionar para o menu das notas.
         * */
        notasButton.setOnClickListener {
            val intent = Intent(this@PerfilUtilizador, MainActivity::class.java)
            startActivityForResult(intent, NotesRequestActivity)
        }

        /**
         * Fab para fazer logout da conta.
         * */
        logoutButton.setOnClickListener {

            with(sharedPref.edit()) {
                remove("isUserLogged")
                remove("idUti")
                commit()
            }

            val intent = Intent(this@PerfilUtilizador, PaginaInicial::class.java)
            startActivityForResult(intent, PaginaINICIAL)
        }

        /**
         * Fab para adicionar nova irregularidade.
         * */

        novaButton.setOnClickListener(){
            val intent = Intent(this@PerfilUtilizador, CriarIrregularidade::class.java)
            startActivityForResult(intent, NovaIrregularidadeRequestActivity)
        }


        /**
         * Introduz no mapa todas as irregularidades provenientes do WS.
         * */
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getIrregularidades()




        /**Obtém o valor da ID do utilizador autenticado*/
        idLogin = sharedPref.getInt("idUti", 0);


        call.enqueue(object : Callback<List<Irregularidade>> {


            override fun onResponse(call: Call<List<Irregularidade>>, response: Response<List<Irregularidade>>) {
                if (response.isSuccessful) {
                    irregularidades = response.body()!!
                    for (ir in irregularidades) {
                        position = LatLng(ir.latitude, ir.longitude)
                        idUtilizadorIrr = ir.id_utilizador;
                        idIrregularidade=ir.id

                        //Log.d("MARIA","$idUtilizadorIrr e $idLogin");

                        var info = ir.descricao + "\n" + ir.tipo + "\n" + ir.id_utilizador + "\n" + ir.image_url + "\n" + ir.id + "\n" + position.latitude + "\n" + position.longitude

                        if (idLogin == idUtilizadorIrr) {
                            mMap.addMarker(MarkerOptions().position(position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(ir.nome).snippet(info))
                        } else {
                            mMap.addMarker(MarkerOptions().position(position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(ir.nome).snippet(info))
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<Irregularidade>>, t: Throwable) {
                Log.d("ERRO", "Erro ao carregar irregularidades")
                Log.d("ERRO", t.toString())
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
        mMap.setInfoWindowAdapter(InfoWindowAdapter(this))

        googleMap.setOnInfoWindowClickListener(this)

    }

    override fun onBackPressed() {

        val snackbarBack = findViewById<View>(R.id.perfilutilizador)
        Snackbar.make(snackbarBack, "You have to logout!", Snackbar.LENGTH_SHORT) //Criar string
                .show()
    }

    override fun onInfoWindowClick(p0: Marker) {

        val title = p0.title

        val info = p0.snippet.split("\n").toTypedArray()
        val descricao = info[0]
        val tipo = info[1]

        val idUtilizador = info[2]

        /**Guarda a id da irregularidade*/
        val idIrregularidade = info[4]

        //val imageUrl = "https://cityalertcm.000webhostapp.com/myslim/api/"+info[3]

        val lat = info[5]
        val long = info[6]


        val intent = Intent(this@PerfilUtilizador, verIrregularidade::class.java)

        intent.putExtra("id",idIrregularidade)
        intent.putExtra("nome",title)
        intent.putExtra("descricao",descricao)
        intent.putExtra("tipo",tipo)
        intent.putExtra("latitude",lat)
        intent.putExtra("longitude",long)
        intent.putExtra("idUtilizador",idUtilizador)
        intent.putExtra("idLogin",idLogin)



        //Criar intent para a imagem
        startActivity(intent)
    }


}

