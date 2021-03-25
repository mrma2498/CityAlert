package ipvc.estg.cityalert

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.adapters.NotaAdapter
import ipvc.estg.cityalert.entities.Nota
import ipvc.estg.cityalert.viewModel.NotaViewModel
import kotlinx.android.synthetic.main.activity_consulta_nota.*
import kotlinx.android.synthetic.main.activity_edita_nota.*

class EditaNota : AppCompatActivity() {


    private lateinit var  titulo: EditText
    private lateinit var  descricao: EditText





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edita_nota)



        val tituloEdit = intent.getStringExtra("titulo")
        val descEdit = intent.getStringExtra("desc")


        val titulo = findViewById<EditText>(R.id.addTitle)
        val descricao = findViewById<EditText>(R.id.addDescription)


        titulo.setText(tituloEdit)
        descricao.setText(descEdit)


        val posicao: Int? = intent.getIntExtra("id",0)


        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {

            val replyIntent = Intent()
            if (TextUtils.isEmpty(titulo.text) || TextUtils.isEmpty(descricao.text)){
                setResult(Activity.RESULT_CANCELED,replyIntent)

                if (TextUtils.isEmpty(titulo.text) && TextUtils.isEmpty(descricao.text)) {
                    titulo.error = resources.getString(R.string.empty);
                    descricao.error = resources.getString(R.string.empty);

                }

                else if (TextUtils.isEmpty(titulo.text)){
                    titulo.error = resources.getString(R.string.empty);
                }

                else if (TextUtils.isEmpty(descricao.text)){
                    descricao.error = resources.getString(R.string.empty);
                }


            } else {


                replyIntent.putExtra(EXTRA_REPLY_TITULO, titulo.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESCRICAO, descricao.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_POSICAO, posicao)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()

            }

        }

    }

    companion object {

        const val EXTRA_REPLY_TITULO = "ipvc.estg.cityalert.titulo"
        const val EXTRA_REPLY_DESCRICAO = "ipvc.estg.cityalert.description"
        const val EXTRA_REPLY_POSICAO = "ipvc.estg.cityalert.posicao"
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }





    /*Código identico ao adicionar nota? mas é necessário fazer a query para fazer update*/



}