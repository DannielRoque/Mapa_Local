package estudo.com.mapalocal.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import estudo.com.mapalocal.modelo.Categoria

class LocalDAO(
    context: Context?
) : SQLiteOpenHelper(context, "LocalFavorito", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val sqlLocal =
            "CREATE TABLE IF NOT EXISTS LOCAL(id INTEGER PRIMARY KEY AUTOINCREMENT, caminhoImagem TEXT, descricao TEXT UNIQUE, telefone TEXT, latlng TEXT)"
        val sqlCategoria =
            "CREATE TABLE IF NOT EXISTS CATEGORIA(id INTEGER PRIMARY KEY AUTOINCREMENT, caminhoIcone TEXT, descricao TEXT NOT NULL UNIQUE)"
        val sqlLocalCategoria =
            "CREATE TABLE IF NOT EXISTS LOCAL_HAS_CATEGORIA(local_id INT NOT NULL, categoria_id INT NOT NULL, PRIMARY KEY(local_id, categoria_id), FOREIGN KEY (local_id) REFERENCES LOCAL(local_id), FOREIGN KEY (categoria_id) REFERENCES CATEGORIA(categoria_id))"

        db.execSQL(sqlLocal)
        db.execSQL(sqlCategoria)
        db.execSQL(sqlLocalCategoria)
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
        val db : SQLiteDatabase =writableDatabase
        val values : ContentValues = pega_categoria(categoria)

        val params : Array<String> = arrayOf(categoria.id.toString())
        db.update("CATEGORIA", values ,"id=?", params)
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
}