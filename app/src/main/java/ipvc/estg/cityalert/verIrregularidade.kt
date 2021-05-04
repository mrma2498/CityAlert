package ipvc.estg.cityalert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.api.EliminarIrr
import ipvc.estg.cityalert.api.EndPoints
import ipvc.estg.cityalert.api.Irregularidade
import ipvc.estg.cityalert.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_consulta_nota.*
import kotlinx.android.synthetic.main.activity_ver_irregularidade.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class verIrregularidade : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_irregularidade)

        var nome = intent.getStringExtra("nome")
        var desc = intent.getStringExtra("descricao")
        var tipo = intent.getStringExtra("tipo")
        var lat = intent.getDoubleExtra("latitude",0.0)
        var long = intent.getDoubleExtra("longitude",0.0)

        val idIrregularidade = intent.getIntExtra("id",0)


        //Mostrar coordenadas
        //Mostrar foto

        //Reportado por:

        nomeT.text = nome
        desT.text = desc
        tipoT.text = tipo
        latitudeT.text = lat.toString()
        longitudeT.text = long.toString()

        deleteButton.setOnClickListener {

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.eliminaIrregularidade(idIrregularidade)

            call.enqueue(object : Callback<EliminarIrr> {


                override fun onResponse(call: Call<EliminarIrr>, response: Response<EliminarIrr>) {

                    if (response.isSuccessful) {

                        Log.d("MARIA","Eliminado com sucesso!")
                        finish()
                        //Recarregar mapa

                    }
                }

                override fun onFailure(call: Call<EliminarIrr>, t: Throwable) {
                    Log.d("MARIA",t.toString())
                }

            })



        }
    }
}