package ipvc.estg.cityalert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import ipvc.estg.cityalert.api.EditaIrr
import ipvc.estg.cityalert.api.EliminarIrr
import ipvc.estg.cityalert.api.EndPoints
import ipvc.estg.cityalert.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_editar_irregularidade.*
import kotlinx.android.synthetic.main.activity_add_new_note.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditarIrregularidade : AppCompatActivity() {

    var tipo: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_irregularidade)



        val id = intent.getIntExtra("id",0)

        val nome = intent.getStringExtra("nome")
        val desc = intent.getStringExtra("descricao")
        var tipoIr = intent.getStringExtra("tipo")
        //val latitude  = intent.getStringExtra("latitude")
        //val longitude  = intent.getStringExtra("longitude")

        val radioGroup = findViewById<RadioGroup>(R.id.radiogroup)

        when (tipoIr) {
            "Obras" -> {
                radioGroup.check(radio_one.id)
            }
            "Acidente" -> {
                radioGroup.check(radio_two.id)
            }
            "Defeitos" -> {
                radioGroup.check(radio_three.id)
            }
            "Outros" -> {
                radioGroup.check(radio_four.id)
            }


            /**Coloca na variável tipo, o tipo de irregularidade mediante o radio button selecionado.*/
        }


        val nomeIr = findViewById<EditText>(R.id.nomeIr)
        val descricao = findViewById<EditText>(R.id.descricaoIr)


        nomeIr.setText(nome)
        descricao.setText(desc)


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

        }



        btnEdit.setOnClickListener {


            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.editaIrregularidade(id,nomeIr.text.toString(),descricaoIr.text.toString(),tipo)

            call.enqueue(object : Callback<EditaIrr> {


                override fun onResponse(call: Call<EditaIrr>, response: Response<EditaIrr>) {

                    if (response.isSuccessful) {


                        val intent = Intent(this@EditarIrregularidade, PerfilUtilizador::class.java)
                        startActivity(intent)


                        Log.d("MARIA","Editado com sucesso!")

                    }
                }

                override fun onFailure(call: Call<EditaIrr>, t: Throwable) {
                    Log.d("MARIA",t.toString())
                }

            })

        }
    }
}