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
            "CREATE TABLE IF NOT EXISTS LOCAL(id INTEGER PRIMARY KEY AUTOINCREMENT, telefone TEXT, observacao TEXT, latlng TEXT)"
        val sqlCategoria =
            "CREATE TABLE IF NOT EXISTS CATEGORIA(id INTEGER PRIMARY KEY AUTOINCREMENT, caminho_imagem TEXT, descricao TEXT NOT NULL)"
        val sqlLocalCategoria =
            "CREATE TABLE IF NOT EXISTS LOCAL_HAS_CATEGORIA(local_id INT NOT NULL, categoria_id INT NOT NULL, CONSTRAINT PK_local_has_categoria PRIMARY KEY (local_id, categoria_id), CONSTRAINT FK_local_id FOREIGN KEY (local_id) REFERENCES LOCAL(local_id), CONSTRAINT FK_categoria_id FOREIGN KEY (categoria_id) REFERENCES CATEGORIA(categoria_id))"

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

    fun insertCategoria() {
        val insertCategoria = ""
    }

    fun updateCategoria() {

    }

    fun selectCategoria() {
        val sql = "SELECT descricao FROM sqlCategoria WHERE descricao = "
    }

    fun selectAllCategorias() {
        val sqlAllCategoria = "SELECT * FROM sqlCategoria"

    }
}