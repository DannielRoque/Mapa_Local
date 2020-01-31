package estudo.com.mapalocal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import estudo.com.mapalocal.R
import estudo.com.mapalocal.modelo.Categoria
import kotlinx.android.synthetic.main.item_categoria.view.*

class ActivityHomeAdapter(
    private val lista: MutableList<Categoria>
) : RecyclerView.Adapter<ActivityHomeAdapter.ActivityHomeViewHolder>() {

    lateinit var onItemCLickListener: OnItemCLickListener
    lateinit var onItemLongClickListener: OnItemLongClickListener

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityHomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutInflado = layoutInflater.inflate(R.layout.item_categoria, parent, false)
        return ActivityHomeViewHolder(layoutInflado)
    }

    override fun onBindViewHolder(holder: ActivityHomeViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    fun setOnItemClickListener(onItemCLickListener: OnItemCLickListener) {
        this.onItemCLickListener = onItemCLickListener
    }

    fun setOnLongClickListener(onItemLongClickListener: OnItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener
    }

    inner class ActivityHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val campoImagem = itemView.item_categoria_imagem
        private val objetoJson = Gson()

        fun bind(categoria: Categoria) {

            itemView.setOnClickListener {
                val arquivo = objetoJson.toJson(categoria)
                onItemCLickListener.onItemClick(arquivo, layoutPosition)
            }

            itemView.setOnLongClickListener {
                val objetoLong = objetoJson.toJson(categoria)
                onItemLongClickListener.onItemLongClick(objetoLong, layoutPosition)
            }

            with(categoria) {
                caminhoIcone?.let { campoImagem.setImageResource(it) }
            }
        }
    }
}