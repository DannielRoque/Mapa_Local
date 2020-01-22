package estudo.com.mapalocal.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.CODE_CAMERA
import estudo.com.mapalocal.constantes.TITLE_FORMULARIO_CATEGORIA
import kotlinx.android.synthetic.main.activity_formulario_categoria.*
import java.io.File

class FormularioCategoriaActivity : AppCompatActivity() {

    private lateinit var caminhoImagem : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_categoria)
        configuraToolbar()

        activity_formulario_botao_imagem_categoria.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            caminhoImagem = getExternalFilesDir(null).toString()+"/"+ System.currentTimeMillis()+".jpg"
            val arquivoFoto = File(caminhoImagem)
        }
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
        when(item.itemId){
            R.id.menu_salvar -> {
                Toast.makeText(this, "Clique salvar", Toast.LENGTH_LONG).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
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
        when(requestCode){
            CODE_CAMERA->{
                Log.e("Teste", "Camera $CODE_CAMERA")
                if ((grantResults.isEmpty()) or (grantResults[0] != PackageManager.PERMISSION_GRANTED)){
                    finish()
                }
            }
        }
    }
}
