package estudo.com.mapalocal.ui.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import estudo.com.mapalocal.modelo.Categoria
import estudo.com.mapalocal.modelo.Local
import estudo.com.mapalocal.ui.FormularioLocalActivity
import kotlinx.android.synthetic.main.activity_formulario_local.*
import java.io.File

class FormularioLocalHelper(activity: FormularioLocalActivity) {

    private val campoImagemLocal: ImageView = activity.activity_formulario_imagem_local
    private val campoDescricao: EditText = activity.activity_local_formulario_descricao
    private val campoTelefone: EditText = activity.activity_formulario_telefone
    private val campoSite: EditText = activity.activity_formulario_site
    private val campoLatitude: TextView = activity.local_campo_latitude
    private val campoLongitude: TextView = activity.local_campo_longitude
    private val campoCategoriaId: TextView = activity.activity_formulario_campo_categoria_id
    private val local = Local()


    fun pegaLocal(): Local {
        local.caminhoImagem = campoImagemLocal.tag as String?
        local.descricao = campoDescricao.text.toString().toLowerCase().replace(" ", "")
        local.telefone = campoTelefone.text.toString()
        local.site = campoSite.text.toString()
        local.latitude = campoLatitude.text.toString()
        local.longitude = campoLongitude.text.toString()
        return local
    }

    fun preencheFormulario(local: Local) {

        campoDescricao.setText(local.descricao)
        campoTelefone.setText(local.telefone)
        campoSite.setText(local.site)
        campoLatitude.text = local.latitude
        campoLongitude.text = local.longitude
        carregaImagem(local.caminhoImagem!!)
//        campoCategoriaId.text = categoria.id.toString()
    }

    fun carregaImagem(caminhoFoto: String) {
        caminhoFoto.let {
            val bitmap: Bitmap = BitmapFactory.decodeFile(caminhoFoto)
            val bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 500, 550, true)
            campoImagemLocal.setImageBitmap(bitmapReduzido)
            campoImagemLocal.tag = caminhoFoto
        }
    }
}
