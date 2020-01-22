package estudo.com.mapalocal.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import estudo.com.mapalocal.modelo.Categoria

class LocalDAO(
    context: Context?
) : SQLiteOpenHelper(context, "LocalFavorito", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val sqlLocal =
            "CREATE TABLE LOCAL(id INTEGER PRIMARY KEY AUTOINCREMENT, caminho_imagem TEXT, descricao TEXT NOT NULL, telefone TEXT, observacao TEXT)"
        val sqlCategoria =
            "CREATE TABLE CATEGORIA(id INTEGER PRIMARY KEY AUTOINCREMENT, caminho_imagem TEXT, descricao TEXT NOT NULL)"
        val sqlLocalCategoria =
            "CREATE TABLE LOCAL_HAS_CATEGORIA(local_categoria_id INTEGER PRIMARY KEY AUTOINCREMENT,local_id, categoria_id, CONSTRAINT local_id_local FOREIGN KEY (local_id) REFERENCES LOCAL(local_id), CONSTRAINT categoria_id_categoria FOREIGN KEY (categoria_id) REFERENCES CATEGORIA(categoria_id))"

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

    fun insertCategoria(categoria: Categoria) {
        val insertCategoria = ""
    }

    fun updateCategoria() {

    }

    fun selectCategoria(descricao: String) {
        val sql = "SELECT descricao FROM sqlCategoria WHERE descricao = "
    }

    fun selectAllCategorias() {
        val sqlAllCategoria = "SELECT * FROM sqlCategoria"

    }
}