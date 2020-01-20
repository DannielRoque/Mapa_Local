package estudo.com.mapalocal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.TITLE_FORMULARIO_CATEGORIA
import kotlinx.android.synthetic.main.activity_formulario_categoria.*

class FormularioCategoriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_categoria)
        configuraToolbar()
    }

    private fun configuraToolbar() {
        setSupportActionBar(activity_formulario_toolbar_categoria)
        supportActionBar?.title = TITLE_FORMULARIO_CATEGORIA
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
