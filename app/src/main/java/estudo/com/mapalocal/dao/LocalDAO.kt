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

    override fun onCreate(db: SQLiteDatabase) {
        val sqlLocal =
            "CREATE TABLE IF NOT EXISTS LOCAL(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, caminhoImagem TEXT, descricao TEXT NOT NULL UNIQUE, telefone TEXT, latlng TEXT)"
        val sqlCategoria =
            "CREATE TABLE IF NOT EXISTS CATEGORIA(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, caminhoIcone TEXT, descricao TEXT NOT NULL UNIQUE)"
        val sqlLocalCategoria =
            "CREATE TABLE IF NOT EXISTS LOCAL_HAS_CATEGORIA(local_descricao TEXT NOT NULL, categoria_descricao TEXT NOT NULL, PRIMARY KEY(local_descricao, categoria_descricao), FOREIGN KEY (local_descricao) REFERENCES LOCAL(local_id), FOREIGN KEY (categoria_descricao) REFERENCES CATEGORIA(categoria_id))"

        val sqlInsertCategoria =
            "INSERT INTO CATEGORIA(caminhoIcone, descricao) VALUES(2131165360,'taxi'),(2131165341,'barbearia'),(2131165321,'mecanico'),(2131165332,'aquario'),(2131165281,'choperia')"

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

    fun delete(categoria: Categoria) {
        val db: SQLiteDatabase = writableDatabase
        val params: Array<String> = arrayOf(categoria.id.toString())
        db.delete("CATEGORIA", "id = ?", params)
    }

    fun selectCategoria() {
        val sql = "SELECT descricao FROM sqlCategoria WHERE descricao = "
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

    fun insertLocal(local: Local) {
        val db: SQLiteDatabase = writableDatabase
        val dados: ContentValues = pegaLocal(local)
        db.insert("LOCAL", null, dados)
    }

    private fun pegaLocal(local: Local): ContentValues {
        val dados = ContentValues()
        dados.put("id", local.id)
        dados.put("caminhoImagem", local.caminhoImagem)
        dados.put("descricao", local.descricao)
        dados.put("telefone", local.telefone)
        dados.put("latlng", local.latLng)
        return dados
    }

    fun updateLocal(local: Local) {
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = pegaLocal(local)
        val params: Array<String> = arrayOf(local.id.toString())
        db.update("LOCAL", values, "id =?", params)
    }

    fun deleteLocal(local: Local) {
        val db: SQLiteDatabase = writableDatabase
        val params: Array<String> = arrayOf(local.id.toString())
        db.delete("LOCAL", "id =?", params)
    }

    fun selectAllLocal(): MutableList<Local> {
        val db: SQLiteDatabase = writableDatabase
        val sql = "SELECT * FROM LOCAL"
        val cursor: Cursor = db.rawQuery(sql, null)
        val locais: MutableList<Local> = arrayListOf()
        if (!cursor.equals(null)) {
            while (cursor.moveToNext()) {
                val local = Local()
                local.id = (cursor.getInt(cursor.getColumnIndex("id")))
            }
        }
        cursor.close()
        return locais
    }

    //configuracao banco local_has_categoria abaixo

    fun insertLocal_has_Categoria(local_descricao: String, categoria_descricao: String) {
        val db: SQLiteDatabase = writableDatabase
        val dado = ContentValues()
        dado.put("local_descricao", local_descricao)
        dado.put("categoria_descricao", categoria_descricao)
        db.insert("LOCAL_HAS_CATEGORIA", null, dado)
        Log.e("teste", "executou $dado")
    }

    fun deleteLocal_has_Categoria(local_id: Int, categoria_id: Int) {

    }

    fun buscaTodosLocaisClicandoCategoria(descricaoSelecionada: String): MutableList<Local>? {
        Log.e("teste", "descricao entrada $descricaoSelecionada")
        if (descricaoSelecionada.equals(null)) return null
        val db: SQLiteDatabase = readableDatabase
        val sql ="SELECT l.* FROM LOCAL_HAS_CATEGORIA as lc INNER JOIN LOCAL as l ON lc.local_descricao = l.descricao INNER JOIN CATEGORIA as c ON lc.categoria_descricao = c.descricao WHERE c.descricao = '$descricaoSelecionada'"
        Log.e("teste", "abaixo select $sql")
        val cursor: Cursor = db.rawQuery(sql, null)
        val locaisSelecionados: MutableList<Local> = arrayListOf()
        if (!cursor.equals(null)) {
            while (cursor.moveToNext()) {
                val local = Local()
                local.id = (cursor.getInt(cursor.getColumnIndex("id")))
                local.caminhoImagem = (cursor.getString(cursor.getColumnIndex("caminhoImagem")))
                local.descricao = (cursor.getString(cursor.getColumnIndex("descricao")))
                local.telefone = (cursor.getString(cursor.getColumnIndex("telefone")))
                local.latLng = (cursor.getString(cursor.getColumnIndex("latlng")))
                locaisSelecionados.add(local)
            }
        }
        cursor.close()
                Log.e("teste", "locais selecionados $locaisSelecionados")
        return locaisSelecionados
    }
}