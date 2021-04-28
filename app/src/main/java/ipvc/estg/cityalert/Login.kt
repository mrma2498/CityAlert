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
import kotlinx.android.synthetic.main.recyclerview_item.view.*
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
        var id_utilizador = sharedPref.getInt("idUti",0)
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
                        val c: Utilizador = response.body()!! //obter id do utilizador e colocar no shared preferences

                        val sharedPref: SharedPreferences = getSharedPreferences("login",Context.MODE_PRIVATE)
                        with(sharedPref.edit()){
                            putBoolean("isUserLogged", true);
                            putInt("idUti",c.id);
                            commit()
                        }




                        val intent = Intent(this@Login, PerfilUtilizador::class.java)
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


    }



}