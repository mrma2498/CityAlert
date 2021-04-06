package ipvc.estg.cityalert

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import ipvc.estg.cityalert.api.EndPoints
import ipvc.estg.cityalert.api.ServiceBuilder
import ipvc.estg.cityalert.api.Utilizador
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import kotlin.math.log


class Login : AppCompatActivity() {

    private val PERFILActivityRequestCode = 5
    private val PaginaINICIAL = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val sharedPref: SharedPreferences = getSharedPreferences("login",Context.MODE_PRIVATE)

        val isUserLogin = sharedPref.getBoolean("isUserLogged",false)
        //Log.d("SHAREDPREFENCES", "Read $isUserLogged")


        loginAccount.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()


            if (username.isEmpty()){
                editTextUsername.error="Username required"
                editTextUsername.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()){
                editTextPassword.error="Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }



            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.userLogin(username, password)

            call.enqueue(object : Callback<Utilizador> {

                override fun onResponse(call: Call<Utilizador>, response: Response<Utilizador>) {
                    if (response.isSuccessful) {
                        val c: Utilizador = response.body()!!

                        //Mostrar snackbar do login realizado com sucesso
                       /*
                        val snackbarSucess = findViewById<View>(R.id.loginpage)

                        Snackbar.make(snackbarSucess, "Bem-vindo " + c.username, Snackbar.LENGTH_SHORT)
                                .show()*/

                        val sharedPref: SharedPreferences = getSharedPreferences("login",Context.MODE_PRIVATE)
                        with(sharedPref.edit()){
                            putBoolean("isUserLogged", true);
                            commit()
                        }

                        Log.d("SHAREDPREFENCES2", "Read $isUserLogin")


                        val intent = Intent(this@Login, Perfil::class.java)
                        startActivityForResult(intent, PERFILActivityRequestCode)

                    }



                }

                override fun onFailure(call: Call<Utilizador>, t: Throwable) {


                    val snackbarFail = findViewById<View>(R.id.loginpage)

                    Snackbar.make(snackbarFail, R.string.loginfail, Snackbar.LENGTH_SHORT)
                            .show()

                    Log.d("fail","Falhou!")
                    Log.d("fail",t.message.toString())

                }
            })

        }

        backMenu.setOnClickListener {
            val intent = Intent(this@Login, PaginaInicial::class.java)
            startActivityForResult(intent, PaginaINICIAL)
        }

    }



}