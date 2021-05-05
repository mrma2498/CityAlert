package ipvc.estg.cityalert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    private val EditarREQUEST = 20


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_irregularidade)

        val nome = intent.getStringExtra("nome")
        val desc = intent.getStringExtra("descricao")
        val tipo = intent.getStringExtra("tipo")
        val lat = intent.getStringExtra("latitude")
        val long = intent.getStringExtra("longitude")

        val idIrregularidade = intent.getStringExtra("id")
        val id: Int? = idIrregularidade!!.toInt()

        val idUtilizador = intent.getStringExtra("idUtilizador")
        val idUti: Int? = idUtilizador!!.toInt()

        val idLogin= intent.getIntExtra("idLogin",0)

        /**O botão só fica ativado se id do utilizador que criou a irregularidade foi igual à id do utilizador autenticado */
        if(idLogin != idUti){
            deleteButton.isEnabled=false
            editButton.isEnabled=false
        }


        //Mostrar foto

        //Reportado por:

        nomeT.text = nome
        desT.text = desc
        tipoT.text = tipo
        latitudeT.text = lat.toString()
        longitudeT.text = long.toString()


        deleteButton.setOnClickListener {


            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.eliminaIrregularidade(id!!)
            call.enqueue(object : Callback<EliminarIrr> {


                override fun onResponse(call: Call<EliminarIrr>, response: Response<EliminarIrr>) {

                    if (response.isSuccessful) {



                        val intent = Intent(this@verIrregularidade, PerfilUtilizador::class.java)
                        startActivity(intent)

                        Log.d("MARIA","Eliminado com sucesso!")


                    }
                }

                override fun onFailure(call: Call<EliminarIrr>, t: Throwable) {
                    Log.d("MARIA",t.toString())
                }

            })



        }



        editButton.setOnClickListener {

            val intent = Intent(this@verIrregularidade, EditarIrregularidade::class.java)

            intent.putExtra("id",id) //Int
            intent.putExtra("nome",nome)
            intent.putExtra("descricao",desc)
            intent.putExtra("tipo",tipo)
            intent.putExtra("latitude",lat)
            intent.putExtra("longitude",long)



            //Criar intent para a imagem
            startActivity(intent)
        }
    }

    //Verificar se faz falta
    override fun onBackPressed() {

        finish()

    }

}