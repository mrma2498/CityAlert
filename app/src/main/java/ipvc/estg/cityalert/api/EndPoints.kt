package ipvc.estg.cityalert.api

import retrofit2.Call
import retrofit2.http.*


interface EndPoints {


    @FormUrlEncoded
    @POST("myslim/api/login")
    fun userLogin(
            @Field("username") username: String,
            @Field("password") password: String): Call<Utilizador>

    @GET("myslim/api/irregularidades")
    fun getIrregularidades(): Call<List<Irregularidade>>


}