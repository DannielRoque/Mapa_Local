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
        val sqlLocal = "CREATE TABLE local(`id`,`descricao`, `telefone`, `observacao`)"

        val sqlCategoria = ""

        db.execSQL(sqlLocal)
        db.execSQL(sqlCategoria)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val localSql = "DROP TABLE IF EXISTS local"

        val categoriaSql = "DROP TABLE IF EXISTS categoria"

        db.execSQL(localSql)
        db.execSQL(categoriaSql)
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