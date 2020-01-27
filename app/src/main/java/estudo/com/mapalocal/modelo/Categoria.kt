package estudo.com.mapalocal.modelo

import java.io.Serializable

class Categoria(
    var id : Long? = null,
    var caminhoIcone : Int = 0,
    var descricao : String = ""
) : Serializable