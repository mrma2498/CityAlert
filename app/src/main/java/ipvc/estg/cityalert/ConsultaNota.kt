package ipvc.estg.cityalert

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_consulta_nota.*

class ConsultaNota : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consulta_nota)


        val titulo: String? = intent.getStringExtra("NoteTitle")
        val desc: String? = intent.getStringExtra("NoteDes")
        titleNote.text = titulo
        descNote.text = desc
    }
}