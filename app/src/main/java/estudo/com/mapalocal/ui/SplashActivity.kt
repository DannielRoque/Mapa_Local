package estudo.com.mapalocal.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import estudo.com.mapalocal.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fromLogo: Animation = AnimationUtils.loadAnimation(this, R.anim.fromlogo)
        val logo: TextView = splah_texto_logo

        logo.animation = fromLogo

        Handler().postDelayed(Runnable {
            startActivity(Intent(baseContext, MainActivity::class.java))
            finish()
        }, 5000)
    }
}
