package ipvc.estg.cityalert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_pagina_inicial.*

class PaginaInicial : AppCompatActivity() {

    private val NotesActivityRequestCode = 3
    private val LOGINActivityRequestCode = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagina_inicial)



        val login = findViewById<Button>(R.id.loginB)


        login.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivityForResult(intent, LOGINActivityRequestCode)
        }

        val notes = findViewById<Button>(R.id.notespage)

        notes.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivityForResult(intent, NotesActivityRequestCode)
        }
    }
}