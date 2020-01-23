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
    private val campoLatLng: TextView = activity.campo_lat_long
    private val local = Local()

    fun carregaImagem(caminhoFoto: String) {
        if (caminhoFoto != null) {
            val bitmap: Bitmap = BitmapFactory.decodeFile(caminhoFoto)
            val bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 500, 550, true)
            campoImagemLocal.setImageBitmap(bitmapReduzido)
            campoImagemLocal.tag = caminhoFoto
        }
    }

    fun pegaLocal(): Local {
        local.caminhoImagem = (campoImagemLocal.tag as String?).toString()
        local.descricao = campoDescricao.editText?.text.toString()
        local.telefone = campoTelefone.editText?.text.toString()
        local.latLng = campoLatLng.text.toString()
        return local
    }
}