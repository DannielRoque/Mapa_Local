package estudo.com.mapalocal.modelo

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable

class Local(
    val id: Long? = null,
    val descricao: String = "",
    val telefone: String = "",
    val observacao: String = "",
    val latLng: LatLng
) : Serializable