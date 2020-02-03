package estudo.com.mapalocal.ui.adapter

interface OnItemLongClickListener {
        fun onItemLongClick(
            view: String,
            position: Int
        ) : Boolean
}