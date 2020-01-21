package estudo.com.mapalocal.modelo

import java.io.Serializable

class Local(
    val id: Long? = null,
    val descricao: String = "",
    val telefone: String = "",
    val observacao: String = "",
    val categoria: Categoria
) : Serializable