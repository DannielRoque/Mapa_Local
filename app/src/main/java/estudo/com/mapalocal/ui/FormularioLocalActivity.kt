package estudo.com.mapalocal.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import estudo.com.mapalocal.BuildConfig
import estudo.com.mapalocal.R
import estudo.com.mapalocal.constantes.*
import estudo.com.mapalocal.dao.LocalDAO
import estudo.com.mapalocal.modelo.Categoria
import estudo.com.mapalocal.modelo.Local
import estudo.com.mapalocal.ui.adapter.ActivityLocalAdapter
import estudo.com.mapalocal.ui.adapter.OnItemCLickListener
import estudo.com.mapalocal.ui.adapter.OnItemLongClickListener
import estudo.com.mapalocal.ui.helper.FormularioLocalHelper
import kotlinx.android.synthetic.main.activity_formulario_local.*
import kotlinx.android.synthetic.main.alert_dialog_custom.*
import kotlinx.android.synthetic.main.dialog_edit_delete.*
import java.io.File

class FormularioLocalActivity : AppCompatActivity() {

    private lateinit var caminhoImagem: String
    private lateinit var campo_telefone: EditText
    private lateinit var campo_site: EditText
    private lateinit var campo_Imagem: ImageView
    private lateinit var campo_descricao: EditText
    private lateinit var campo_latitude: TextView
    private lateinit var campo_longitude: TextView
    private lateinit var campo_id_categoria: TextView
    private lateinit var helper: FormularioLocalHelper
    private val dao = LocalDAO(this)
    private var id_icon: Int = 0
    private var idreferenciaEdit : Int = 0
    private lateinit var listaCategoria: MutableList<Categoria>
    private lateinit var adapter: ActivityLocalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario_local)
        configuraToolbar()
        configuracaoGaleriaCamera()
        vaiParaFormularioCategoria()
        configuraInicializacaoDosCampos()
        configuraListaRecyclerView()
        intent?.let { intent ->
            intent.getStringExtra(PATH_FORMULARIO)?.let { jsonData ->
                val locallatlng: LatLng =
                    Gson().fromJson(jsonData, object : TypeToken<LatLng>() {}.type)
                campo_latitude.text = locallatlng.latitude.toString()
                campo_longitude.text = locallatlng.longitude.toString()
            }
        }

        intent?.let { intent ->
            intent.getStringExtra(PATH_LOCAL)?.let { jsonLocal ->
                val local: Local = Gson().fromJson(jsonLocal, object : TypeToken<Local>() {}.type)
                helper.preencheFormulario(local)
                Log.e("ROQUE", "${local.id}")
                idreferenciaEdit = local.id!!
            }
        }
    }

    override fun onResume() {
        configuraListaRecyclerView()
        configuraClickLongo()
        super.onResume()
    }

    private fun configuraListaRecyclerView() {
        listaCategoria = dao.selectAllCategorias()
        adapter = ActivityLocalAdapter(listaCategoria.toMutableList())
        formulario_local_recyclerview.adapter = adapter

        configuraClickItemLista()
    }

    fun configuraClickLongo() {
        adapter.setOnItemLongClickListener(object : OnItemLongClickListener {
            override fun onItemLongClick(view: String, position: Int): Boolean {
                var cont = 0
                val dados: Categoria =
                    Gson().fromJson(view, object : TypeToken<Categoria>() {}.type)
                val dadosLocais: MutableList<Local>? =
                    dao.buscaTodosLocaisClicandoCategoria(dados.descricao)

                val dialog = Dialog(this@FormularioLocalActivity)
                dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(layoutInflater.inflate(R.layout.dialog_edit_delete, null))
                dialog.show()

                val editar = dialog.options_editar
                val deletar = dialog.options_excluir

                editar.setOnClickListener { }

                deletar.setOnClickListener {
                    if ((dados.descricao == "taxi") || (dados.descricao == "barbearia") || (dados.descricao == "mecanico") || (dados.descricao == "aquario") || (dados.descricao == "choperia")) {
                        val alertDialog = AlertDialog.Builder(this@FormularioLocalActivity)
                        dialog.dismiss()
                        alertDialog.setTitle("Informativo")
                        alertDialog.setMessage("Exclusão categoria padrão não permitida!")
                        alertDialog.setPositiveButton("Entendi") { alertdialog, which ->
                            alertdialog.dismiss()
                        }

                        val dialogAl: AlertDialog = alertDialog.create()
                        dialogAl.show()
                    } else {
                        Log.e("teste", "clicklongo $view, $position, $dadosLocais")
                        if (dadosLocais!!.isNotEmpty()) {
                            for (lista in dadosLocais) {
                                cont++
                            }
                        }
                        if (cont > 0) {
                            Toast.makeText(
                                this@FormularioLocalActivity,
                                "Necessário deletar todos Locais no mapa cont $cont",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@FormularioLocalActivity,
                                "Categoria ${dados.descricao} removida cont $cont",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                            dao.deleteCategoria(dados)
                            onResume()
                        }
                    }
                }
                return true
            }
        })

    }

    private fun configuraInicializacaoDosCampos() {
        campo_descricao = activity_local_formulario_descricao
        campo_Imagem = activity_formulario_imagem_local
        campo_latitude = local_campo_latitude
        campo_longitude = local_campo_longitude
        campo_id_categoria = activity_formulario_campo_categoria_id
        campo_telefone = activity_formulario_telefone
        campo_site = activity_formulario_site
        helper = FormularioLocalHelper(this)
        activity_formulario_imagem_local.setImageResource(R.drawable.image_tela_categoria)
    }

    private fun configuraClickItemLista() {
        adapter.setOnItemCLickListener(object : OnItemCLickListener {
            override fun onItemClick(view: String, position: Int) {
                adapter.notifyDataSetChanged()
                val categoria: Categoria =
                    Gson().fromJson(view, object : TypeToken<Categoria>() {}.type)
                id_icon = categoria.id!!
                campo_id_categoria.text = categoria.descricao
            }
        })
    }

    private fun configuracaoGaleriaCamera() {
        activity_formulario_botao_imagem_local.setOnClickListener {
            configuraPermissaoCamera()
        }
    }

    private fun configuraPermissaoCamera() {
        notification.visibility = View.GONE
        if (configuracaoValidacaoPermissao()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), CODE_CAMERA
                )
            }
        } else {
            dialogComGaleriaECamera()
        }
    }

    private fun configuracaoValidacaoPermissao(): Boolean {
        return ((ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED)
                and (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED)
                and (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED))
    }

    private fun dialogComGaleriaECamera() {
        val meuDialog = Dialog(this)
        meuDialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        meuDialog.setContentView(layoutInflater.inflate(R.layout.alert_dialog_custom, null))
        meuDialog.show()

        val camera: ImageView = meuDialog.aler_dialog_camera
        val galeria: ImageView = meuDialog.alert_dialog_galery

        camera.setOnClickListener {
            configuracaoCamera()
            meuDialog.dismiss()
        }

        galeria.setOnClickListener {
            configuracaoGaleria()
            meuDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                helper.carregaImagem(caminhoImagem)
            }
        } else if (requestCode == CODE_GALERY) {
            if (resultCode == Activity.RESULT_OK) {
                val imageUri = data?.data
                val imagePath = getRealPathFromURI(imageUri!!)
                val file = File(imagePath)
                file.let { file ->
                    helper.carregaImagem(file.toString())
                }
            }
        }
    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(idx)
        }
        cursor.close()
        return res!!
    }

    private fun configuracaoGaleria() {
        val intentGallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intentGallery, CODE_GALERY)
    }


    private fun configuracaoCamera() {
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
        button_new_categoria.setOnClickListener {
            val intent = Intent(this, FormularioCategoriaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun configuraToolbar() {
        setSupportActionBar(activity_formulario_local_toolbar)
        supportActionBar?.title = TITLE_FORMULARIO_LOCAL
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_salvar -> {
                notification.visibility = View.GONE
                val local = helper.pegaLocal()
                Log.e("ROQUE", "teste ${local.id} teste ref $idreferenciaEdit")

                when {
                    campo_descricao.text.toString().trim().equals("") -> {
                        configuraErroDescricaoVazia()
                    }
                    local.caminhoImagem.equals(null) -> {
                        activity_local_formulario_descricao_hint.error = null
                        campo_Imagem.setImageResource(R.drawable.notification)
                        notification.visibility = View.VISIBLE
                    }
                    id_icon == 0 -> {
                        Toast.makeText(this, SELECIONA_ICON, Toast.LENGTH_LONG).show()
                    }

                    idreferenciaEdit!=0 -> {
                        Log.e("ROQUE", "dentro alter $idreferenciaEdit")
                        dao.updateLocal(local, idreferenciaEdit)
                        dao.updateLocal_has_categoria(
                            local_descricao = local.descricao,
                            categoria_descricao = campo_id_categoria.text.toString()
                        )
                        Toast.makeText(this, "Alteração realizada com sucesso", Toast.LENGTH_LONG)
                            .show()
                        dao.close()
                        finish()
                    }
                    else -> {
                        Log.e("ROQUE", "dentro insert ${local.descricao}")
                        dao.insertLocal(local)
                        dao.insertLocal_has_Categoria(
                            local_descricao = local.descricao,
                            categoria_descricao = campo_id_categoria.text.toString()
                        )
                        dao.close()
                        finish()
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configuraErroDescricaoVazia() {
        activity_local_formulario_descricao_hint.error = VAZIO
        activity_local_formulario_descricao_hint.requestFocus()
        val focoTeclado: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        focoTeclado.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CODE_CAMERA -> {
                if ((grantResults.isEmpty()) or (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    finish()
                } else {
                    dialogComGaleriaECamera()
                }
            }
        }
    }
}
