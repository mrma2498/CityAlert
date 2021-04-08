package ipvc.estg.cityalert.api

data class Irregularidade (

    val id: Int,
    val nome: String,
    val descricao: String,
    val tipo: String,
    val latitude: Double,
    val longitude: Double,
    val id_utilizador: Int

)