package ipvc.estg.cityalert

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_perfil.*

class Perfil : AppCompatActivity() {


    private val PaginaINICIAL = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        logoutButton.setOnClickListener {

            val sharedPref: SharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
            with(sharedPref.edit()){
                remove("isUserLogged")
                commit()
            }

            //finish()

            //Mostrar snackbar quando fizer logout
            /*
            val snackbarLogout = findViewById<View>(R.id.perfil)

            Snackbar.make(snackbarLogout, R.string.logout, Snackbar.LENGTH_SHORT)
                .show()*/

            val intent = Intent(this@Perfil, PaginaInicial::class.java)
            startActivityForResult(intent, PaginaINICIAL)
        }


    }
}