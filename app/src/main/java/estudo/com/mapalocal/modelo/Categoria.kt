package estudo.com.mapalocal.modelo

import java.io.Serializable

class Categoria(
    val id : String? = null,
    var caminhoIcone : Int = 0,
    var descricao : String = ""
) : Serializable