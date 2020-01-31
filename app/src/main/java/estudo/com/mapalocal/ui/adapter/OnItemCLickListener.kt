package estudo.com.mapalocal.ui.adapter

interface OnItemCLickListener{
    fun onItemClick(
        view : String,
        position : Int
    )
}

interface OnItemLongClickListener{
    fun onItemLongClick(
        view: String,
        position: Int
    ) : Boolean
}