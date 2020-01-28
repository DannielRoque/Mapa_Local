package estudo.com.mapalocal.modelo

import java.io.Serializable

class Categoria(
    var id : Int? = null,
    var caminhoIcone : Int? = null,
    var descricao : String = ""
) : Serializable