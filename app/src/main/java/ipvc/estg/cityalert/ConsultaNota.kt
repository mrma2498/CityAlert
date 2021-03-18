package ipvc.estg.cityalert

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.entities.Nota
import kotlinx.android.synthetic.main.activity_consulta_nota.*


class ConsultaNota : AppCompatActivity() {

    private val editNoteActivityRequestCode = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta_nota)


        val titulo: String? = intent.getStringExtra("NoteTitle")
        val desc: String? = intent.getStringExtra("NoteDes")
        titleNote.text = titulo
        descNote.text = desc


        //Abre uma nova atividade para editar a nota
        val editar = findViewById<FloatingActionButton>(R.id.editNote)
        editar.setOnClickListener {
            val intent = Intent(this, EditaNota::class.java)
            startActivityForResult(intent,editNoteActivityRequestCode)

        }
    }


}