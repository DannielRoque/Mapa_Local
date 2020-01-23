package estudo.com.mapalocal.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import estudo.com.mapalocal.BuildConfig
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.CODE_CAMERA
import estudo.com.mapalocal.constantes.TITLE_FORMULARIO_CATEGORIA
import estudo.com.mapalocal.modelo.Categoria
import estudo.com.mapalocal.ui.helper.FormularioHelper
import kotlinx.android.synthetic.main.activity_formulario_categoria.*
import java.io.File

class FormularioCategoriaActivity : AppCompatActivity() {

    private lateinit var caminhoImagem: String
    private lateinit var helper: FormularioHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_categoria)
        configuraToolbar()
        helper = FormularioHelper(this)
        configuracaoCamera()
    }

    private fun configuracaoCamera() {
        activity_formulario_botao_imagem_categoria.setOnClickListener {

            val options = arrayOf("Galeria", "Camera")
//            val view: View = window.decorView
//            val viewCriada = LayoutInflater.from(this)
//                .inflate(R.layout.alert_dialog_custom, view as ViewGroup, false)
            val builder = AlertDialog.Builder(this)
                .setTitle("Escolha")
//                .setView(viewCriada)
            builder.setItems(options) { _, item ->
                if (options[item].equals("Galeria")) {
                    Toast.makeText(this@FormularioCategoriaActivity, "Galeria", Toast.LENGTH_LONG)
                        .show()
                } else if (options[item].equals("Camera")) {
                    //abaixo acesso a camera
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    caminhoImagem =
                        getExternalFilesDir(null).toString() + "/" + System.currentTimeMillis() + ".jpg"
                    val arquivoFoto = File(caminhoImagem)
                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        FileProvider.getUriForFile(
                            this@FormularioCategoriaActivity,
                            BuildConfig.APPLICATION_ID + ".provider",
                            arquivoFoto
                        )
                    )
                    startActivityForResult(intent, CODE_CAMERA)

                }
            }

            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                helper.carregaImagem(caminhoImagem)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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
                val categoria : Categoria = helper.pegaCategoria()

                if(categoria.descricao.isEmpty()){
                    activity_formulario_descricao_categoria.error = "Preencha o campo para prosseguir"
                    activity_formulario_descricao_categoria.requestFocus()
                    val focoTeclado : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    focoTeclado.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
                }else{
                    activity_formulario_descricao_categoria.error = null
                }
                if (!categoria.caminhoImagem.equals(null)){
                    Log.e("Teste vazio", "${categoria.caminhoImagem}")
                }else{
                    Log.e("Teste cheio", "${categoria.caminhoImagem}")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA), CODE_CAMERA
                )
            }
        }
        super.onResume()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CODE_CAMERA -> {
                Log.e("Teste", "Camera $CODE_CAMERA")
                if ((grantResults.isEmpty()) or (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    finish()
                }
            }
        }
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