package estudo.com.mapalocal.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import estudo.com.mapalocal.R
import estudo.com.mapalocal.modelo.Local
import kotlinx.android.synthetic.main.item_categoria.view.*

class ActivityLocalAdapter(
    private val listaLocal: MutableList<Local>
) : RecyclerView.Adapter<ActivityLocalAdapter.ActivityLoacalViewHolder>() {

    private lateinit var onItemCLickListener: OnItemCLickListener

    override fun getItemCount(): Int {
        return listaLocal.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityLoacalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflado = layoutInflater.inflate(R.layout.item_categoria, parent, false)
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
        private val objetoParaEnvio = Gson()

        fun bind(local: Local) {
            itemView.setOnClickListener {
                val arquivo = objetoParaEnvio.toJson(local)
                onItemCLickListener.onItemClick(arquivo, layoutPosition)
            }

//            Picasso.with(itemView.context).load(local.imagem)
        }
    }
}
