package estudo.com.mapalocal.modelo

import java.io.Serializable

class Categoria(
    val id : String? = null,
    var caminhoImagem : String = "",
    var descricao : String = ""
) : Serializable