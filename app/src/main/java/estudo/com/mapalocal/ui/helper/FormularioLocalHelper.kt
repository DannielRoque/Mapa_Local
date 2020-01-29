package estudo.com.mapalocal.ui.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import estudo.com.mapalocal.modelo.Local
import estudo.com.mapalocal.ui.FormularioLocalActivity
import kotlinx.android.synthetic.main.activity_formulario_local.*

class FormularioLocalHelper(activity: FormularioLocalActivity) {

    private val campoImagemLocal: ImageView = activity.activity_formulario_imagem_local
    private val campoDescricao: TextInputLayout = activity.activity_local_formulario_descricao
    private val campoTelefone: TextInputLayout = activity.activity_formulario_telefone
    private val campoLatitude: TextView = activity.local_campo_latitude
    private val campoLongitude: TextView = activity.local_campo_longitude
    private val local = Local()

    fun carregaImagem(caminhoFoto: String) {
        caminhoFoto.let {
            val bitmap: Bitmap = BitmapFactory.decodeFile(caminhoFoto)
            val bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 500, 550, true)
            campoImagemLocal.setImageBitmap(bitmapReduzido)
            campoImagemLocal.tag = caminhoFoto
        }
    }

    fun pegaLocal(): Local {
        local.caminhoImagem = campoImagemLocal.tag as String?
        local.descricao = campoDescricao.editText?.text.toString().toLowerCase().replace(" ", "")
        local.telefone = campoTelefone.editText?.text.toString()
        local.latitude = campoLatitude.text.toString()
        local.longitude = campoLongitude.text.toString()
        return local
    }
}