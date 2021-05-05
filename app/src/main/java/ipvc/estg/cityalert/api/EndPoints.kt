package ipvc.estg.cityalert.api

import retrofit2.Call
import retrofit2.http.*


interface EndPoints {


    @FormUrlEncoded
    @POST("myslim/api/login")
    fun userLogin(
            @Field("username") username: String,
            @Field("password") password: String): Call<Utilizador>


    /**Adiciona uma nova irregularidade à BD*/
    @FormUrlEncoded
    @POST("myslim/api/addIrregularidade")
    fun adicionaIrregularidade(
        @Field("nome") nome: String,
        @Field("descricao") descricao: String,
        @Field("tipo") tipo: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
        @Field("image_url") image_url: String,
        @Field("id_utilizador") id_utilizador: Int): Call<Irregularidade>


    @GET("myslim/api/irregularidades")
    fun getIrregularidades(): Call<List<Irregularidade>>

    @FormUrlEncoded
    @POST("myslim/api/delete")
    fun eliminaIrregularidade(
        @Field("id") id: Int): Call<EliminarIrr>

    @FormUrlEncoded
    @POST("myslim/api/update")
    fun editaIrregularidade(
        @Field("id") id: Int,
        @Field("nome") nome: String,
        @Field("descricao") descricao: String,
        @Field("tipo") tipo: String
        ): Call<EditaIrr>


}