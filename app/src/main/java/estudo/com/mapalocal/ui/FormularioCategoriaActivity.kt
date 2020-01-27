package estudo.com.mapalocal.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.TITLE_FORMULARIO_CATEGORIA
import estudo.com.mapalocal.ui.adapter.ActivityCategoriaAdapter
import estudo.com.mapalocal.ui.helper.FormularioCategoriaHelper
import estudo.com.mapalocal.ui.helper.FormularioLocalHelper
import kotlinx.android.synthetic.main.activity_formulario_categoria.*

class FormularioCategoriaActivity : AppCompatActivity() {

    lateinit var helper : FormularioCategoriaHelper
    lateinit var listaIcon : MutableList<Int>

        private lateinit var adapter : ActivityCategoriaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_categoria)
        configuraToolbar()
        helper = FormularioCategoriaHelper()
        listaIcon = helper.listaCate
        adapter = ActivityCategoriaAdapter(listaIcon)
        activity_formulario_recyclerview_categoria.adapter = adapter

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