package estudo.com.mapalocal.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import estudo.com.mapalocal.R
import estudo.com.mapalocal.modelo.Categoria
import estudo.com.mapalocal.modelo.Local
import kotlinx.android.synthetic.main.item_categoria.view.*
import kotlinx.android.synthetic.main.item_categoria.view.item_categoria_imagem
import kotlinx.android.synthetic.main.item_categoria_com_descricao.view.*

class ActivityLocalAdapter(
    private val listaLocal: MutableList<Categoria>
) : RecyclerView.Adapter<ActivityLocalAdapter.ActivityLoacalViewHolder>() {

    private lateinit var onItemCLickListener: OnItemCLickListener

    override fun getItemCount(): Int {
        return listaLocal.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityLoacalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflado = layoutInflater.inflate(R.layout.item_categoria_com_descricao, parent, false)
        return ActivityLoacalViewHolder(inflado)
    }

    override fun onBindViewHolder(holder: ActivityLoacalViewHolder, position: Int) {
        holder.bind(listaLocal[position])
    }

    fun OnItemCLickListener(onItemCLickListener: OnItemCLickListener) {
        this.onItemCLickListener = onItemCLickListener
    }

    inner class ActivityLoacalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val campo_imagem = itemView.item_categoria_imagem
        private val campo_descricao = itemView.item_categoria_descricao
        private val objetoParaEnvio = Gson()

        fun bind(categoria: Categoria) {
            itemView.setOnClickListener {
                val arquivo = objetoParaEnvio.toJson(categoria)
                onItemCLickListener.onItemClick(arquivo, layoutPosition)
            }
            categoria.caminhoIcone?.let { campo_imagem.setImageResource(it) }
            categoria.descricao?.let { campo_descricao.text = it }
            Log.e("teste", "caminho imagem ${categoria.caminhoIcone}")
        }
    }
}
