package ipvc.estg.cityalert.api

import retrofit2.http.GET
import retrofit2.http.Path

import retrofit2.Call



interface EndPoints {

    @GET("/utilizadores/")
    fun getUtilizadores(): Call<List<Utilizador>>

    @GET("/utilizador/{id}")
    fun getUtilizadorById(@Path("id") id:Int): Call<Utilizador>
}