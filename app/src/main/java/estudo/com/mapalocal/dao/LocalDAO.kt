package estudo.com.mapalocal.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import estudo.com.mapalocal.modelo.Categoria

class LocalDAO(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        val sqlLocal = "CREATE TABLE LOCAL(id, caminho_imagem, descricao, telefone, observacao)"
        val sqlCategoria = "CREATE TABLE CATEGORIA(id, caminho_imagem, descricao)"
        val sqlLocalCategoria = "CREATE TABLE LOCAL_HAS_CATEGORIA(local_id, categoria_id, CONSTRAINT local_id_local FOREIGN KEY (local_id) REFERENCES LOCAL(local_id), CONSTRAINT categoria_id_categoria FOREIGN KEY (categoria_id) REFERENCES CATEGORIA(categoria_id))"

        db.execSQL(sqlLocal)
        db.execSQL(sqlCategoria)
        db.execSQL(sqlLocalCategoria)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val localSql = "DROP TABLE IF EXISTS LOCAL"
        val categoriaSql = "DROP TABLE IF EXISTS CATEGORIA"
        val localCategoria = "DROP TABLE IF EXISTS LOCAL_HAS_CATEGORIA"

        db.execSQL(localSql)
        db.execSQL(categoriaSql)
        db.execSQL(localCategoria)
    }

    fun insertCategoria(categoria: Categoria){
        val insertCategoria = ""
    }

    fun updateCategoria(){

    }

    fun selectCategoria(descricao : String){
        val sql = "SELECT descricao FROM sqlCategoria WHERE descricao = "
    }

    fun selectAllCategorias(){
        val sqlAllCategoria = "SELECT * FROM sqlCategoria"

    }
}