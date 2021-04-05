package ipvc.estg.cityalert

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


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
                        Log.d("CREATE","Funcionou!")
                        Toast.makeText(this@Login, c.username, Toast.LENGTH_SHORT).show()
                        val snackbarSucess = findViewById<View>(R.id.loginpage)

                        Snackbar.make(snackbarSucess, "Login com sucesso!", Snackbar.LENGTH_SHORT)
                                .show()
                    }

                }

                override fun onFailure(call: Call<Utilizador>, t: Throwable) {
                    Toast.makeText(this@Login, "Falhou", Toast.LENGTH_SHORT).show()

                    Log.d("fail","Falhou!")
                    Log.d("fail",t.message.toString())
                }
            })

        }

    }

    fun getSingle(view: View){
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUtilizadorById(1);

        call.enqueue(object : Callback<Utilizador> {
            override fun onResponse(call: Call<Utilizador>, response: Response<Utilizador>) {
                if (response.isSuccessful) {
                    val c: Utilizador = response.body()!!
                    Toast.makeText(this@Login, c.username, Toast.LENGTH_SHORT).show()


                }
            }

            override fun onFailure(call: Call<Utilizador>, t: Throwable) {
                Toast.makeText(this@Login, "Falhou", Toast.LENGTH_SHORT).show()
            }
        })
    }



}