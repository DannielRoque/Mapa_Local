package estudo.com.mapalocal.ui.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.google.android.material.textfield.TextInputLayout
import estudo.com.mapalocal.modelo.Categoria
import estudo.com.mapalocal.ui.FormularioCategoriaActivity
import kotlinx.android.synthetic.main.activity_formulario_categoria.*

class FormularioHelper(activity: FormularioCategoriaActivity) {

    private val campoImagemCategoria : ImageView = activity.activity_formulario_imagem_categoria
    private val campoObservacaoCategoria : TextInputLayout = activity.activity_formulario_descricao_categoria
    private val categoria = Categoria()


    fun carregaImagem(caminhoFoto: String) {
        if (caminhoFoto != null) {
            val bitmap: Bitmap = BitmapFactory.decodeFile(caminhoFoto)
            val bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 500, 550, true)
            campoImagemCategoria.setImageBitmap(bitmapReduzido)
            campoImagemCategoria.tag = caminhoFoto
        }
    }

    fun pegaCategoria(): Categoria{
        categoria.caminhoImagem = (campoImagemCategoria.tag as String?).toString()
        categoria.descricao = campoObservacaoCategoria.editText?.text.toString()
        return categoria
    }
}