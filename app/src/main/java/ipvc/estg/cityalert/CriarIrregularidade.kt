package ipvc.estg.cityalert

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.info_window.*


class CriarIrregularidade : AppCompatActivity() {

    private val IMG_REQUEST = 777
    private var path: Uri? = null
    private lateinit var bitmap: Bitmap
    lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_criar_irregularidade)

        val sharedPref: SharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val radioGroup = findViewById<RadioGroup>(R.id.radiogroup)

        img = findViewById(R.id.picture)


        var nome = findViewById<EditText>(R.id.nomeIr).text.toString()
        var descricao = findViewById<EditText>(R.id.descricaoIr).text.toString()
        var idUtilizador = sharedPref.getInt("idUti", 0);

        /**Guarda o tipo de irregularidade*/
        var tipo: String


        val btnUpload = findViewById<Button>(R.id.upload)



        btnUpload.setOnClickListener {
            selectImage()
        }


        /*
        * Latitude
        * Longitude
        * Obter fotografia
        * */

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
                    tipo=""
                }

            }
            Log.d("MARIA",tipo)
        }

        Log.d("MARIA",idUtilizador.toString())



    }

    /**Abre a galeria de fotografias do telemóvel*/
    private fun selectImage(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMG_REQUEST)
    }


    /**Obtém a foto e coloca-a no ImageView*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==IMG_REQUEST && resultCode== Activity.RESULT_OK && data !=null){

            img.setImageURI(data.data) // handle chosen image


        }

    }


}