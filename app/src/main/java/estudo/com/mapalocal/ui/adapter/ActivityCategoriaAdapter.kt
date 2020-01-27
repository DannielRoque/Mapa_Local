package estudo.com.mapalocal.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import estudo.com.mapalocal.R
import kotlinx.android.synthetic.main.item_categoria.view.*

class ActivityCategoriaAdapter(
    private val listaIconCategoria: MutableList<Int>
) : RecyclerView.Adapter<ActivityCategoriaAdapter.ActivityCategoriaViewHolder>() {

    lateinit var onItemCLickListener: OnItemCLickListener

    override fun getItemCount(): Int {
        return listaIconCategoria.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityCategoriaViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val inflado = layoutInflater.inflate(R.layout.item_categoria, parent, false)
        return ActivityCategoriaViewHolder(inflado)
    }

    override fun onBindViewHolder(holder: ActivityCategoriaViewHolder, position: Int) {
        holder.bind(listaIconCategoria[position])
    }

    fun setOnClickListener(onItemCLickListener: OnItemCLickListener) {
        this.onItemCLickListener = onItemCLickListener
    }

    inner class ActivityCategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val campoImagem = itemView.item_categoria_imagem
        private val objetoJson = Gson()

        fun bind(icones: Int) {
            itemView.setOnClickListener {
                Log.e("teste", "icone $icones")
                val arquivoJson = objetoJson.toJson(icones)
                onItemCLickListener.onItemClick(arquivoJson, layoutPosition)
            }
            campoImagem.setImageResource(icones)
        }
    }
}