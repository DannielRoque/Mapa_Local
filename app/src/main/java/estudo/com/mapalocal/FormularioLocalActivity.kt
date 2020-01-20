package estudo.com.mapalocal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import estudo.com.mapalocal.constantes.TITLE_FORMULARIO_LOCAL
import kotlinx.android.synthetic.main.activity_formulario_local.*

class FormularioLocalActivity : AppCompatActivity() {

    private lateinit var myBottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_local)
        configuraToolbar()
        configuraButtonSheet()
    }

    private fun configuraToolbar() {
        setSupportActionBar(activity_formulario_local_toolbar)
        supportActionBar?.title = TITLE_FORMULARIO_LOCAL
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun configuraButtonSheet() {
        val bottomsheet: View = bottom_sheet
        myBottomSheetBehavior = BottomSheetBehavior.from(bottomsheet)
        activity_formulario_botao_categoria.setOnClickListener {
            myBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
