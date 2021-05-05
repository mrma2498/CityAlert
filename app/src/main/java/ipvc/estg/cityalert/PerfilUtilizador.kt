package ipvc.estg.cityalert

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.api.EndPoints
import ipvc.estg.cityalert.api.Irregularidade
import ipvc.estg.cityalert.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_perfil_utilizador.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PerfilUtilizador : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private val LOCATION_PERMISSION_REQUEST_CODE = 12;
    private lateinit var mMap: GoogleMap
    private val PaginaINICIAL = 6
    private lateinit var irregularidades: List<Irregularidade>
    private val NotesRequestActivity = 9
    private val NovaIrregularidadeRequestActivity = 10
    private val verIrregularidadeREQUESTCODE = 15

    lateinit var position: LatLng

    lateinit var loc: LatLng

    /**Guarda a id do utilizador que registou a irregularidade*/
    var idUtilizadorIrr: Int = 0

    var idIrregularidade: Int = 0

    var idLogin: Int = 0

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_utilizador)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        createLocationRequest()



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        var latitude = 0.0
        var longitude = 0.0


        locationCallback = object  : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                loc = LatLng(lastLocation.latitude,lastLocation.longitude)
                latitude = lastLocation.latitude
                longitude = lastLocation.longitude
                //Log.d("MARIA",loc.toString())
                //Log.d("MARIA",latitude.toString())
                //Log.d("MARIA",longitude.toString())
            }
        }



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
        Snackbar.make(snackbarBack, R.string.logout2, Snackbar.LENGTH_SHORT)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.filters, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.obras -> {
                mMap.clear()

                for (ir in irregularidades){
                    if (ir.tipo=="Obras"){

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
                Log.d("MARIA","Filtra por Obras")
                true
            }
            R.id.acidentes -> {
                Log.d("MARIA","Filtra por Acidentes")

                mMap.clear()

                for (ir in irregularidades){
                    if (ir.tipo=="Acidente"){

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
                true
            }

            R.id.defeitos -> {
                Log.d("MARIA","Filtra por Defeitos")

                mMap.clear()

                for (ir in irregularidades){
                    if (ir.tipo=="Defeitos"){

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

                true
            }
            R.id.outros -> {
                Log.d("MARIA","Filtra por Outros")

                mMap.clear()

                for (ir in irregularidades){
                    if (ir.tipo=="Outros"){

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

                true
            }


            R.id.raio5km -> {
                mMap.clear()


                val circle: Circle = mMap.addCircle(
                    CircleOptions()
                        .center(LatLng(loc.latitude, loc.longitude))
                        .radius(5000.0)
                        .strokeColor(Color.RED)
                        .fillColor(0x220000FF)
                )

                for (ir in irregularidades) {
                    var distancia = calculaDistancia(
                        lastLocation.latitude,
                        lastLocation.longitude,
                        ir.latitude,
                        ir.longitude
                    )
                    Log.d("MARIA", "$distancia")

                    if (distancia <= 5000) {
                        position = LatLng(ir.latitude, ir.longitude)
                        idUtilizadorIrr = ir.id_utilizador;
                        idIrregularidade = ir.id





                        var info =
                            ir.descricao + "\n" + ir.tipo + "\n" + ir.id_utilizador + "\n" + ir.image_url + "\n" + ir.id + "\n" + position.latitude + "\n" + position.longitude

                        if (idLogin == idUtilizadorIrr) {
                            mMap.addMarker(
                                MarkerOptions().position(position).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                                ).title(ir.nome).snippet(info)
                            )
                        } else {
                            mMap.addMarker(
                                MarkerOptions().position(position).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                ).title(ir.nome).snippet(info)
                            )
                        }

                    }
                }

                true
            }

            R.id.raio10km -> {
                mMap.clear()


                val circle: Circle = mMap.addCircle(
                    CircleOptions()
                        .center(LatLng(loc.latitude, loc.longitude))
                        .radius(10000.0)
                        .strokeColor(Color.BLUE)
                        .fillColor(0x220000FF)
                )

                for (ir in irregularidades) {
                    var distancia = calculaDistancia(
                        lastLocation.latitude,
                        lastLocation.longitude,
                        ir.latitude,
                        ir.longitude
                    )
                    Log.d("MARIA", "$distancia")

                    if (distancia <= 10000) {
                        position = LatLng(ir.latitude, ir.longitude)
                        idUtilizadorIrr = ir.id_utilizador;
                        idIrregularidade = ir.id





                        var info =
                            ir.descricao + "\n" + ir.tipo + "\n" + ir.id_utilizador + "\n" + ir.image_url + "\n" + ir.id + "\n" + position.latitude + "\n" + position.longitude

                        if (idLogin == idUtilizadorIrr) {
                            mMap.addMarker(
                                MarkerOptions().position(position).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                                ).title(ir.nome).snippet(info)
                            )
                        } else {
                            mMap.addMarker(
                                MarkerOptions().position(position).icon(
                                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                                ).title(ir.nome).snippet(info)
                            )
                        }

                    }
                }

                true
            }

            R.id.todos -> {
                mMap.clear()

                for (ir in irregularidades){

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

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun calculaDistancia(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float{
        val results = FloatArray(1)
        Location.distanceBetween(lat1,lng1,lat2,lng2,results)
        return results[0]
    }

    private fun startLocationsUpdates(){
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, null)
    }

    private fun createLocationRequest(){
        locationRequest = LocationRequest()

        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)

    }

    override fun onResume() {
        super.onResume()
        startLocationsUpdates()

    }

}

