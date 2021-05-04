package ipvc.estg.cityalert

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.api.EndPoints
import ipvc.estg.cityalert.api.Irregularidade
import ipvc.estg.cityalert.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_criar_irregularidade.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


import android.util.Base64
import com.google.android.gms.common.api.GoogleApiClient


class CriarIrregularidade : AppCompatActivity() {


    private lateinit var  nomeText: EditText
    private lateinit var  descText: EditText

    private val LOCATION_PERMISSION_REQUEST_CODE = 12;

    private val IMG_REQUEST = 777
    private var path: Uri? = null
    private lateinit var bitmap: Bitmap
    lateinit var img: ImageView

    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    var imagem64 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_irregularidade)


        createLocationRequest()



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        var latitude = 0.0
        var longitude = 0.0


        locationCallback = object  : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude,lastLocation.longitude)
                latitude = lastLocation.latitude
                longitude = lastLocation.longitude
                //Log.d("MARIA",loc.toString())
                //Log.d("MARIA",latitude.toString())
                //Log.d("MARIA",longitude.toString())
            }
        }

        /*
        findViewById<TextView>(R.id.latitude).setText(latitude.toString())
        findViewById<TextView>(R.id.longitude).setText(longitude.toString())*/


        val sharedPref: SharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val radioGroup = findViewById<RadioGroup>(R.id.radiogroup)

        img = findViewById(R.id.picture)

        nomeText = findViewById(R.id.nomeIr)
        descText = findViewById(R.id.descricaoIr)


        var idUtilizador = sharedPref.getInt("idUti", 0);

        /**Guarda o tipo de irregularidade*/
        var tipo = ""


        val btnUpload = findViewById<Button>(R.id.upload)

        btnUpload.setOnClickListener {
            selectImage()
        }


        /**Coloca na variável tipo, o tipo de irregularidade mediante o radio button selecionado.*/
        radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            var rb = findViewById<RadioButton>(checkedId)
            val index: Int = radioGroup.indexOfChild(rb)

            when (index) {
                0 -> tipo = "Obras"
                1 -> tipo = "Acidente"
                2 -> tipo = "Defeitos"
                3 -> tipo = "Outros"
                else -> {
                    tipo = ""
                }

            }
            Log.d("MARIA", tipo)

        }

        btnAdd.setOnClickListener {

            Log.d("MARIA",latitude.toString())
            Log.d("MARIA",longitude.toString())


            var nome = nomeText.text.toString()
            var descricao = descText.text.toString()

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.adicionaIrregularidade(nome,descricao,tipo,latitude,longitude,imagem64,idUtilizador)

            call.enqueue(object : Callback<Irregularidade> {

                override fun onResponse(call: Call<Irregularidade>, response: Response<Irregularidade>) {
                    //Adicionar mandar para atividade para o mapa
                    if (response.isSuccessful) {

                        val snackbarSuccess = findViewById<View>(R.id.criarirregularidade)

                        Snackbar.make(snackbarSuccess, "Adicionado com sucessso!", Snackbar.LENGTH_SHORT)
                            .show()

                        //Adicionar temporizador para dar tempo para ver o adicionado com sucesso
                        finish()
                        //Recarregar mapa
                    }
                }

                override fun onFailure(call: Call<Irregularidade>, t: Throwable) {
                    Log.d("MARIA","Falhou adicionar")
                }
            })



        }


        Log.d("MARIA", idUtilizador.toString())






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


    /**Abre a galeria de fotografias do telemóvel*/
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMG_REQUEST)
    }


    /**Obtém a foto e coloca-a no ImageView*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var bitmap : Bitmap
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMG_REQUEST && resultCode == Activity.RESULT_OK && data != null) {

            img.setImageURI(data.data) // handle chosen image


            bitmap = (img.drawable as BitmapDrawable).bitmap  // passar para bitmap

            //Passa para base 64

            val ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream)
            val imgByte = ByteArrayOutputStream.toByteArray()
            val encodedImage = Base64.encodeToString(imgByte, Base64.DEFAULT)
            imagem64 = encodedImage

        }

    }

}