package ipvc.estg.cityalert

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.entities.Nota


class PaginaInicial : AppCompatActivity() {

    private val NotesActivityRequestCode = 3
    private val LOGINActivityRequestCode = 4
    private val PERFILActivityRequestCode = 5




    override fun onCreate(savedInstanceState: Bundle?) {



        val sharedPref: SharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);


        if (sharedPref.contains("isUserLogged")){
            //Mandar para a página inicial
            val intent = Intent(this, PerfilUtilizador::class.java)
            startActivityForResult(intent, PERFILActivityRequestCode)

            //Log.d("SHAREDPREFENCES3", "Send to main page")
        }


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

    override fun onBackPressed() {
       //Overrides method
    }



}