package ipvc.estg.cityalert.api

import retrofit2.Call
import retrofit2.http.*


interface EndPoints {

    @GET("myslim/api/utilizadores/")
    fun getUtilizadores(): Call<List<Utilizador>>

    @GET("myslim/api/utilizador/{id}")
    fun getUtilizadorById(@Path("id") id:Int): Call<Utilizador>

    @FormUrlEncoded
    @POST("myslim/api/login")
    fun userLogin(
            @Field("username") username: String,
            @Field("password") password: String): Call<Utilizador>



}