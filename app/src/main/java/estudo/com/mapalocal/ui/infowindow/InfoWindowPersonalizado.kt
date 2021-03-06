package estudo.com.mapalocal.ui.infowindow

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import estudo.com.mapalocal.R
import estudo.com.mapalocal.dao.LocalDAO
import estudo.com.mapalocal.modelo.Local
import kotlinx.android.synthetic.main.info_window.view.*
import kotlin.math.ln

class InfoWindowPersonalizado(val context: Context) :
    GoogleMap.InfoWindowAdapter {

    val dao = LocalDAO(context)
    lateinit var datain: Local

    override fun getInfoWindow(marker: Marker?): View? {
        return null
    }

    override fun getInfoContents(marker: Marker?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.info_window, null)

        val campo_titulo = view.info_titulo
        val campo_telefone = view.info_telefone
        val campo_imagem = view.info_imagem
        val campo_site = view.info_site



        campo_titulo.text = marker?.title
        campo_telefone.text = marker?.snippet

        val teste = dao.selectLocalComDescricao(marker!!.title)

        for (dadoLista in teste) {
            datain = dadoLista
            Log.e("ROQUE", "dentro infowindow  $dadoLista")


        campo_site.text = dadoLista.site
        val arquivoFoto = dadoLista.caminhoImagem
        if (arquivoFoto != null) {
            val bitmap = BitmapFactory.decodeFile(arquivoFoto)
            if (bitmap != null) {
                val bitmapreduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
                campo_imagem.setImageBitmap(bitmapreduzido)
                campo_imagem.setImageBitmap(bitmap)
                campo_imagem.scaleType = ImageView.ScaleType.FIT_XY
            }
        }
    }
        return view
    }

}