package estudo.com.mapalocal.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import estudo.com.mapalocal.modelo.Categoria
import estudo.com.mapalocal.modelo.Local

class LocalDAO(
    context: Context?
) : SQLiteOpenHelper(context, "LocalFavorito", null, 1) {
    lateinit var dado: Categoria

    override fun onCreate(db: SQLiteDatabase) {
        val sqlLocal =
            "CREATE TABLE IF NOT EXISTS LOCAL(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, caminhoImagem TEXT, descricao TEXT NOT NULL UNIQUE, telefone TEXT NOT NULL, site TEXT NOT NULL, latitude TEXT, longitude TEXT)"
        val sqlCategoria =
            "CREATE TABLE IF NOT EXISTS CATEGORIA(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, caminhoIcone TEXT, descricao TEXT NOT NULL UNIQUE)"
        val sqlLocalCategoria =
            "CREATE TABLE IF NOT EXISTS LOCAL_HAS_CATEGORIA(local_descricao TEXT NOT NULL, categoria_descricao TEXT NOT NULL, PRIMARY KEY(local_descricao, categoria_descricao), FOREIGN KEY (local_descricao) REFERENCES LOCAL(local_id), FOREIGN KEY (categoria_descricao) REFERENCES CATEGORIA(categoria_id))"

        val sqlInsertCategoria =
            "INSERT INTO CATEGORIA(caminhoIcone, descricao) VALUES(2131165389,'taxi'),(2131165343,'barbearia'),(2131165322,'mecanico'),(2131165334,'aquario'),(2131165281,'choperia')"

        db.execSQL(sqlLocal)
        db.execSQL(sqlCategoria)
        db.execSQL(sqlLocalCategoria)
        db.execSQL(sqlInsertCategoria)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val localSql =
            "DROP TABLE IF EXISTS LOCAL"
        val categoriaSql =
            "DROP TABLE IF EXISTS CATEGORIA"
        val localCategoria =
            "DROP TABLE IF EXISTS LOCAL_HAS_CATEGORIA"

        db.execSQL(localSql)
        db.execSQL(categoriaSql)
        db.execSQL(localCategoria)
    }

    //configuracao banco categoria abaixo

    fun insertCategoria(categoria: Categoria) {
        val db: SQLiteDatabase = writableDatabase
        val dados: ContentValues = pega_categoria(categoria)
        db.insert("CATEGORIA", null, dados)
    }

    private fun pega_categoria(categoria: Categoria): ContentValues {
        val dados = ContentValues()
        dados.put("id", categoria.id)
        dados.put("caminhoIcone", categoria.caminhoIcone)
        dados.put("descricao", categoria.descricao)
        return dados
    }

    fun updateCategoria(categoria: Categoria) {
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = pega_categoria(categoria)

        val params: Array<String> = arrayOf(categoria.id.toString())
        db.update("CATEGORIA", values, "id=?", params)
    }

    fun deleteCategoria(categoria: Categoria) {
        val db: SQLiteDatabase = writableDatabase
        val params: Array<String> = arrayOf(categoria.id.toString())
        db.delete("CATEGORIA", "id = ?", params)
    }

    fun selectCategoria(dadoPassado: String): MutableList<Categoria> {
        Log.e("teste", "dentro selectCategoria $dadoPassado")
        val db: SQLiteDatabase = readableDatabase
        val sql =
            "SELECT c.* FROM local_has_categoria as lc INNER JOIN CATEGORIA as c ON lc.categoria_descricao = c.descricao INNER JOIN LOCAL as l ON lc.local_descricao = l.descricao WHERE l.descricao = '$dadoPassado' "
        val cursor: Cursor = db.rawQuery(sql, null)
        val lista: MutableList<Categoria> = arrayListOf()
        if (!cursor.equals(null)) {
            while (cursor.moveToNext()) {
                val categoria = Categoria()
                lista.add(categoria)
            }
        }

        Log.e("teste", "selectCategoria")
        cursor.close()
        return lista

    }

    fun selectAllCategorias(): MutableList<Categoria> {
        val sql = "SELECT * FROM CATEGORIA"
        val db: SQLiteDatabase = readableDatabase
        val cursor: Cursor = db.rawQuery(sql, null)
        val categorias: MutableList<Categoria> = arrayListOf()

        if (!cursor.equals(null)) {
            while (cursor.moveToNext()) {
                val categoria = Categoria()
                categoria.id = (cursor.getInt(cursor.getColumnIndex("id")))
                categoria.caminhoIcone = (cursor.getInt(cursor.getColumnIndex("caminhoIcone")))
                categoria.descricao = (cursor.getString(cursor.getColumnIndex("descricao")))
                categorias.add(categoria)
            }
        }
        cursor.close()
        return categorias
    }

    //configuracao banco Local abaixo

    fun insertLocal(local: Local): Long {
        val idDoItemIncluido: Long
        val db: SQLiteDatabase = writableDatabase
        val dados: ContentValues = pegaLocal(local)
        idDoItemIncluido = db.insert("LOCAL", null, dados)
        Log.e("teste", "dados local $dados")
        return idDoItemIncluido
    }

    private fun pegaLocal(local: Local): ContentValues {
        val dados = ContentValues()
        dados.put("caminhoImagem", local.caminhoImagem)
        dados.put("descricao", local.descricao)
        dados.put("telefone", local.telefone)
        dados.put("site", local.site)
        dados.put("latitude", local.latitude)
        dados.put("longitude", local.longitude)
        return dados
    }

    fun updateLocal(local: Local, id: Int) {
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = pegaLocal(local)
        val params: Array<String> = arrayOf(id.toString())
        db.update("LOCAL", values, "id=?", params)
        Log.e("ROQUE", "dentro update ${params.first()}")
    }

    fun deleteLocal(local: Local) {
        val db: SQLiteDatabase = writableDatabase
        val params: Array<String> = arrayOf(local.toString())
        db.delete("LOCAL", "id =?", params)
    }

    fun selectLocal(descricao: String): MutableList<Local> {
        val db: SQLiteDatabase = readableDatabase
        val sql = "SELECT * FROM LOCAL WHERE descricao LIKE '%$descricao%'"
        val cursor: Cursor = db.rawQuery(sql, null)
        val localSearch: MutableList<Local> = arrayListOf()
        if (!cursor.equals(null)) {
            while (cursor.moveToNext()) {
                val local = Local()
                configuraLocal(local, cursor)
                localSearch.add(local)
            }
        }
        cursor.close()
        Log.e("teste", "$localSearch")
        return localSearch
    }

    fun selectLocalComDescricao(descricao: String): MutableList<Local> {
        val db: SQLiteDatabase = readableDatabase
        val sql = "SELECT * FROM LOCAL WHERE descricao LIKE '$descricao'"
        val cursor: Cursor = db.rawQuery(sql, null)
        val localSearch: MutableList<Local> = arrayListOf()
        if (!cursor.equals(null)) {
            while (cursor.moveToNext()) {
                val local = Local()
                configuraLocal(local, cursor)
                localSearch.add(local)
            }
        }
        cursor.close()
        Log.e("teste", "$localSearch")
        return localSearch
    }

    fun selectAllLocal(): MutableList<Local> {
        val db: SQLiteDatabase = writableDatabase
        val sql = "SELECT * FROM LOCAL"
        val cursor: Cursor = db.rawQuery(sql, null)
        val locais: MutableList<Local> = arrayListOf()
        if (!cursor.equals(null)) {
            while (cursor.moveToNext()) {
                val local = Local()
                configuraLocal(local, cursor)
                locais.add(local)
            }
        }
        cursor.close()
        return locais
    }

    private fun configuraLocal(
        local: Local,
        cursor: Cursor
    ) {
        local.id = (cursor.getInt(cursor.getColumnIndex("id")))
        local.caminhoImagem = (cursor.getString(cursor.getColumnIndex("caminhoImagem")))
        local.descricao = (cursor.getString(cursor.getColumnIndex("descricao")))
        local.telefone = (cursor.getString(cursor.getColumnIndex("telefone")))
        local.site = (cursor.getString(cursor.getColumnIndex("site")))
        local.latitude = (cursor.getString(cursor.getColumnIndex("latitude")))
        local.longitude = (cursor.getString(cursor.getColumnIndex("longitude")))
    }

    //configuracao banco local_has_categoria abaixo

    fun insertLocal_has_Categoria(local_descricao: String, categoria_descricao: String) {
        val db: SQLiteDatabase = writableDatabase
        val dado = ContentValues()
        dado.put("local_descricao", local_descricao)
        dado.put("categoria_descricao", categoria_descricao)
        db.insert("LOCAL_HAS_CATEGORIA", null, dado)
        Log.e("teste", "executou $dado $local_descricao")
    }

    fun deleteLocal_has_Categoria(local_id: String) {
        val db: SQLiteDatabase = writableDatabase
        val params: Array<String> = arrayOf(local_id)
        db.delete("LOCAL_HAS_CATEGORIA", "local_descricao = ?", params)
        Log.e("teste", "banco $local_id")
    }

    fun updateLocal_has_categoria(local_descricao: String, categoria_descricao: String) {
        val db: SQLiteDatabase = writableDatabase
        val dado = ContentValues()
        dado.put("local_descricao", local_descricao)
        dado.put("categoria_descricao", categoria_descricao)
        val params: Array<String> = arrayOf(local_descricao)
        db.update("LOCAL_HAS_CATEGORIA", dado, "local_descricao = ?", params)
    }

    fun buscaTodosLocaisClicandoCategoria(descricaoSelecionada: String): MutableList<Local>? {
        Log.e("teste", "descricao entrada $descricaoSelecionada")
        if (descricaoSelecionada.equals(null)) return null
        val db: SQLiteDatabase = readableDatabase
        val sql =
            "SELECT l.* FROM LOCAL_HAS_CATEGORIA as lc INNER JOIN LOCAL as l ON lc.local_descricao = l.descricao INNER JOIN CATEGORIA as c ON lc.categoria_descricao = c.descricao WHERE c.descricao = '$descricaoSelecionada'"
        val cursor: Cursor = db.rawQuery(sql, null)
        val locaisSelecionados: MutableList<Local> = arrayListOf()
        if (!cursor.equals(null)) {
            while (cursor.moveToNext()) {
                val local = Local()
                configuraLocal(local, cursor)
                locaisSelecionados.add(local)
            }
        }
        cursor.close()
        return locaisSelecionados
    }

    fun buscaTodasCategoriasOndeLocal(descricaoLocal: String): MutableList<Categoria>? {
        if (descricaoLocal.equals(null)) return null
        val db: SQLiteDatabase = readableDatabase
        val sql =
            "SELECT c.* FROM LOCAL_HAS_CATEGORIA as lc INNER JOIN CATEGORIA as c ON lc.categoria_descricao = c.descricao INNER JOIN LOCAL as l ON lc.local_descricao = l.descricao WHERE l.descricao = '$descricaoLocal'"
        val cursor: Cursor = db.rawQuery(sql, null)
        val categoriasSelecionadas: MutableList<Categoria> = arrayListOf()
        if (!cursor.equals(null)) {
            while (cursor.moveToNext()) {
                val categoria = Categoria()
                categoria.id = (cursor.getInt(cursor.getColumnIndex("id")))
                categoria.caminhoIcone = (cursor.getInt(cursor.getColumnIndex("caminhoIcone")))
                categoria.descricao = (cursor.getString(cursor.getColumnIndex("descricao")))
                categoriasSelecionadas.add(categoria)
            }
        }
        cursor.close()
        return categoriasSelecionadas
    }

    fun deleteLocal_has_Categoria_Todos(local_id: MutableList<Local>) {
        val db: SQLiteDatabase = writableDatabase
        for (local in local_id) {
            val localDescricao = local.descricao
            val params: Array<String> = arrayOf(localDescricao)
            db.delete("LOCAL_HAS_CATEGORIA", "local_descricao = ?", params)
            Log.e("teste", "banco $local_id")
        }
    }

    fun deleteLocalLista(local: MutableList<Local>) {
        val db: SQLiteDatabase = writableDatabase
        for (listaLocal in local) {
            val localId = listaLocal.id
            val params: Array<String> = arrayOf(localId.toString())
            db.delete("LOCAL", "id =?", params)
            Log.e("teste", "delete local $localId")
        }
    }
}