package ipvc.estg.cityalert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_consulta_nota.*
import kotlinx.android.synthetic.main.activity_ver_irregularidade.*

class verIrregularidade : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_irregularidade)

        var nome = intent.getStringExtra("nome")
        var desc = intent.getStringExtra("descricao")
        var tipo = intent.getStringExtra("tipo")
        var lat = intent.getDoubleExtra("latitude",0.0)
        var long = intent.getDoubleExtra("longitude",0.0)

        Log.d("MARIA",lat.toString() + "Qualquer coisa")
        //Mostrar coordenadas
        //Mostrar foto

        //Reportado por:

        nomeT.text = nome
        desT.text = desc
        tipoT.text = tipo
        latitudeT.text = lat.toString()
        longitudeT.text = long.toString()



    }
}