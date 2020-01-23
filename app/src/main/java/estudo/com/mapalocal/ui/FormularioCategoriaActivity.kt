package estudo.com.mapalocal.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


//abaixo acesso a camera
//val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//caminhoImagem =
//getExternalFilesDir(null).toString() + "/" + System.currentTimeMillis() + ".jpg"
//val arquivoFoto = File(caminhoImagem)
//intent.putExtra(
//MediaStore.EXTRA_OUTPUT,
//FileProvider.getUriForFile(
//this,
//BuildConfig.APPLICATION_ID + ".provider",
//arquivoFoto
//)
//)
//startActivityForResult(intent, CODE_CAMERA)