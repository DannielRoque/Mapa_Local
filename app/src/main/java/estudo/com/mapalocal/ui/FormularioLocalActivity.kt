package estudo.com.mapalocal.ui

import android.Manifest
import android.annotation.SuppressLint
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
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import estudo.com.mapalocal.BuildConfig
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.*
import estudo.com.mapalocal.modelo.Local
import estudo.com.mapalocal.ui.helper.FormularioLocalHelper
import kotlinx.android.synthetic.main.activity_formulario_local.*
import java.io.File

class FormularioLocalActivity : AppCompatActivity() {

    private lateinit var myBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var caminhoImagem: String
    private lateinit var helper: FormularioLocalHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_local)
        configuraToolbar()
        configuraButtonSheet()
        configuracaoGaleriaCamera()
        vaiParaFormularioCategoria()
        helper = FormularioLocalHelper(this)
    }

    private fun configuracaoGaleriaCamera() {

        activity_formulario_botao_imagem_local.setOnClickListener {
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
            } else {
                val options = arrayOf(GALERIA, CAMERA)
                val builder = AlertDialog.Builder(this).setTitle(TITLE_ALERT_CAMERA)
                configuraAlertDialog(builder, options)
                builder.show()
            }
        }
    }

    private fun configuraAlertDialog(
        builder: AlertDialog.Builder,
        options: Array<String>
    ) {
        builder.setItems(options) { _, item ->
            if (options[item].equals(GALERIA)) {
                configuracaoGaleria()
            } else if (options[item].equals(CAMERA)) {
                configuracaoCamera()
            }
        }
    }

    private fun configuracaoGaleria() {
        val intentGallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intentGallery, CODE_GALERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                helper.carregaImagem(caminhoImagem)
            }
        } else if (requestCode == CODE_GALERY) {
            if (resultCode == Activity.RESULT_OK) {
                activity_formulario_imagem_local.setImageURI(data?.data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun configuracaoCamera() {
        //abaixo acesso a camera
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        caminhoImagem =
            getExternalFilesDir(null).toString() + "/" + System.currentTimeMillis() + ".jpg"
        val arquivoFoto = File(caminhoImagem)
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                arquivoFoto
            )
        )
        startActivityForResult(intent, CODE_CAMERA)
    }

    private fun vaiParaFormularioCategoria() {
        activity_formulario_categoria_fab.setOnClickListener {
            val intent = Intent(this, FormularioCategoriaActivity::class.java)
            startActivity(intent)
        }
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
        }

        activity_formulario_botao_categoria_down.setOnClickListener {
            myBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            activity_formulario_botao_categoria.visibility = View.VISIBLE
            activity_formulario_botao_categoria_down.visibility = View.GONE
        }

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
                val local: Local = helper.pegaLocal()
                if (local.descricao.isEmpty()) {
                    activity_formulario_campo_categoria_descricao.error =
                        "Preencha o campo para prosseguir"
                    activity_formulario_campo_categoria_descricao.requestFocus()
                    val focoTeclado: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    focoTeclado.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
                } else {
                    activity_formulario_campo_categoria_descricao.error = null
                }
                if (!local.caminhoImagem.equals(null)) {
                    Log.e("Teste vazio", "${local.caminhoImagem}")
                } else {
                    Log.e("Teste cheio", "${local.caminhoImagem}")
                }
            }
        }
        return super.onOptionsItemSelected(item)
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
