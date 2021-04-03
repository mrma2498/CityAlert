package ipvc.estg.cityalert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import ipvc.estg.cityalert.api.EndPoints
import ipvc.estg.cityalert.api.ServiceBuilder
import ipvc.estg.cityalert.api.Utilizador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)





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