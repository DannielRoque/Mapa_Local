package estudo.com.mapalocal.ui.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import estudo.com.mapalocal.R
import estudo.com.mapalocal.ui.MainActivity
import kotlinx.android.synthetic.main.marker_customizado.view.*

class ActivityHelper(private val activity: MainActivity) {

    fun bitmapDescriptor(context: Context, @DrawableRes resId: Int): Bitmap {

        val marker: View = LayoutInflater.from(context).inflate(R.layout.marker_customizado, null)

        val campo_icone_marker: ImageView = marker.my_card_image_marker
        campo_icone_marker.setImageResource(resId)

        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)

        val bitmap: Bitmap = Bitmap.createBitmap(
            marker.measuredWidth,
            marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)
        return bitmap
    }

    fun String.startsWith(
        prefix : String,
        ignoreCase: Boolean = false
    ): Boolean{
        return true
    }
}