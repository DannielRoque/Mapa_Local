package estudo.com.mapalocal.modelo

import java.io.Serializable

class Local(
    val id: Long? = null,
    var caminhoImagem: String? = null,
    var descricao: String = "",
    var telefone: String = "",
    var latLng: String = ""
) : Serializable