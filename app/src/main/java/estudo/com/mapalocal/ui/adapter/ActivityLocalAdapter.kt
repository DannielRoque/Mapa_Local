package estudo.com.mapalocal.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import estudo.com.mapalocal.R
import estudo.com.mapalocal.modelo.Categoria
import kotlinx.android.synthetic.main.item_categoria.view.item_categoria_imagem
import kotlinx.android.synthetic.main.item_categoria_com_descricao.view.*

class ActivityLocalAdapter(
    private val listaLocal: MutableList<Categoria>
) : RecyclerView.Adapter<ActivityLocalAdapter.ActivityLoacalViewHolder>() {

    private lateinit var onItemCLickListener: OnItemCLickListener
    private lateinit var onItemLongClickListener: OnItemLongClickListener

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
        if (listaLocal[position].selecionado.equals(false)) {
            listaLocal[position].selecionado = true
        }
    }

    fun setOnItemCLickListener(onItemCLickListener: OnItemCLickListener) {
        this.onItemCLickListener = onItemCLickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    inner class ActivityLoacalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val campo_imagem = itemView.item_categoria_imagem
        private val campo_descricao = itemView.item_categoria_descricao
        private val objetoParaEnvio = Gson()

        fun bind(
            categoria: Categoria
        ) {
            Log.e("teste", "fora click")
            itemView.setOnClickListener {
                val arquivo = objetoParaEnvio.toJson(categoria)
                onItemCLickListener.onItemClick(arquivo, layoutPosition)
                Log.e("teste", " dentro click ${categoria.selecionado}")
            }

            itemView.setOnLongClickListener {
                val arquivoLong = objetoParaEnvio.toJson(categoria)
                onItemLongClickListener.onItemLongClick(arquivoLong, layoutPosition)
            }

            categoria.caminhoIcone?.let { campo_imagem.setImageResource(it) }
            categoria.descricao.let { campo_descricao.text = it }
            Log.e("teste", "fora tudo ${categoria.selecionado}")
        }
    }
}
