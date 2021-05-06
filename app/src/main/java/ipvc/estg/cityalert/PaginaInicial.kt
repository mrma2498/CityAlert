package ipvc.estg.cityalert

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.entities.Nota


class PaginaInicial : AppCompatActivity(), SensorEventListener {

    private val NotesActivityRequestCode = 3
    private val LOGINActivityRequestCode = 4
    private val PERFILActivityRequestCode = 5

    private lateinit var sensorManager: SensorManager
    var sensor: Sensor? = null



    override fun onCreate(savedInstanceState: Bundle?) {

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)


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



    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        val lux = event.values[0]
        // Do something with this sensor value.


        val botaoLogin = findViewById<Button>(R.id.loginB)
        val botaoNotas = findViewById<Button>(R.id.notespage)


        //Log.d("MARIA",lux.toString())
        /**Se a luminosidade for inferior a 20000 os botões ficam cinzentos*/
        if (lux <= 20000){
            botaoLogin.setBackgroundColor(Color.GRAY)
            botaoNotas.setBackgroundColor(Color.GRAY)
        }
        else {
            botaoLogin.setBackgroundColor(Color.rgb(252, 166, 86))
            botaoNotas.setBackgroundColor(Color.rgb(252, 166, 86))
        }
    }

    override fun onResume() {
        super.onResume()
        sensor?.also { light ->
            sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

}