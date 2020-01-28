package estudo.com.mapalocal.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.TITLE_FORMULARIO_CATEGORIA
import estudo.com.mapalocal.constantes.VAZIO
import estudo.com.mapalocal.dao.LocalDAO
import estudo.com.mapalocal.modelo.Categoria
import estudo.com.mapalocal.ui.adapter.ActivityCategoriaAdapter
import estudo.com.mapalocal.ui.adapter.OnItemCLickListener
import estudo.com.mapalocal.ui.helper.FormularioCategoriaHelper
import kotlinx.android.synthetic.main.activity_formulario_categoria.*

class FormularioCategoriaActivity : AppCompatActivity() {

    lateinit var helper: FormularioCategoriaHelper
    lateinit var listaIcon: MutableList<Int>
    private lateinit var campo_imagem: ImageView
    private lateinit var campo_descricao: TextInputLayout
    var icon: Int = 0
    private lateinit var adapter: ActivityCategoriaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_categoria)
        configuraToolbar()
        helper = FormularioCategoriaHelper()
        listaIcon = helper.listaCate
        adapter = ActivityCategoriaAdapter(listaIcon)
        campo_imagem = activity_formulario_categoria_imagem
        campo_descricao = activity_formulario_categoria_descricao
        campo_imagem.setImageResource(R.drawable.image_tela_categoria)
        activity_formulario_recyclerview_categoria.adapter = adapter
        configuraCliqueIcone()
    }

    private fun configuraCliqueIcone() {
        adapter.setOnClickListener(object : OnItemCLickListener {
            override fun onItemClick(view: String, position: Int) {
                icon = Gson().fromJson(view, object : TypeToken<Int>() {}.type)
                Log.e("Teste", " $view")
                campo_imagem.setImageResource(icon)
            }
        })
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
                notification.visibility = View.GONE
                if (campo_descricao.editText!!.text.toString().isNotEmpty() &&
                    !campo_descricao.editText!!.text.toString().trim().equals("")
                ) {
                    if (icon == 0) {
                        campo_imagem.setImageResource(R.drawable.notification)
                        notification.visibility = View.VISIBLE
                    } else {
                        val dao = LocalDAO(this)
                        val categoria = Categoria(
                            caminhoIcone = icon,
                            descricao = campo_descricao.editText!!.text.toString()
                        )
                        dao.insertCategoria(categoria)
                        finish()
                    }
                } else {
                    campo_descricao.error = VAZIO
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}