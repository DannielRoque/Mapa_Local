package estudo.com.mapalocal.modelo

import java.io.Serializable

class Categoria(
    val id : String? = null,
    val caminhoImagem : String = "",
    val descricao : String = ""
) : Serializable