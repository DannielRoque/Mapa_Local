package estudo.com.mapalocal.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import estudo.com.mapalocal.R
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
            activity_formulario_botao_categoria.visibility = View.GONE
            activity_formulario_botao_categoria_down.visibility = View.VISIBLE

            activity_formulario_botao_categoria_down.setOnClickListener {
                myBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                activity_formulario_botao_categoria.visibility = View.VISIBLE
                activity_formulario_botao_categoria_down.visibility = View.GONE

                myBottomSheetBehavior.setBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOfSheet: Float) {}

                    @SuppressLint("SwitchIntDef")
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        when (newState) {
                            BottomSheetBehavior.STATE_HIDDEN -> {
                                activity_formulario_botao_categoria.visibility = View.VISIBLE
                                activity_formulario_botao_categoria_down.visibility = View.GONE
                            }
                            BottomSheetBehavior.STATE_DRAGGING -> {
                                activity_formulario_botao_categoria.visibility = View.VISIBLE
                                activity_formulario_botao_categoria_down.visibility = View.GONE
                            }
                        }
                    }
                })
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_salvar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_salvar -> {
                Toast.makeText(this, "Clique salvar", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
