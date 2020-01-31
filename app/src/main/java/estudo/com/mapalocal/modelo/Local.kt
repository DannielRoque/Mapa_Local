package estudo.com.mapalocal.modelo

import java.io.Serializable

class Local(
    var id: Int? = null,
    var caminhoImagem: String? = "",
    var descricao: String = "",
    var telefone: String = "",
    var site: String = "",
    var latitude: String = "",
    var longitude: String = ""
) : Serializable