package com.example.appgestiongastos

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?):
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

    // a continuación se muestra el método para crear una base de datos mediante una consulta sqlite
    override fun onCreate(db: SQLiteDatabase?) {
        // a continuación se muestra una consulta sqlite, donde los nombres de las columnas
        // junto con sus tipos de datos se da
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                    + ID_USUARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOMBRE_USUARIO + " TEXT NOT NULL," +
                    CORREO_USUARIO + " TEXT NOT NULL," +
                    CLAVE_USUARIO + " TEXT NOT NULL" + ");"
                )
       val query2 = ("CREATE TABLE " + TABLE_NAME2 + "("
                    + ID_GASTO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CATEGORIA_GASTO + " TEXT NOT NULL," +
                    FECHA_GASTO + " TEXT NOT NULL," +
                    DESCRIPCION_GASTO + " TEXT NOT NULL," +
                    MONTO_GASTO + " REAL NOT NULL" + ");"
                )
        // estamos llamando a sqlite
        // método para ejecutar nuestra consulta
        db?.execSQL(query)
        db?.execSQL(query2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // este método es para verificar si la tabla ya existe
        val sql = (
                "DROP TABLE IF EXISTS $TABLE_NAME;\n"+
                "DROP TABLE IF EXISTS $TABLE_NAME2;"
                )
        db?.execSQL(sql)
        onCreate(db)
    }

    // Este método es para agregar datos en nuestra base de datos
    fun addUsuario(nomUsu: String,mailUsu:String,passUsu: String){

        // debajo estamos creando
        // una variable de valores de contenido
        val values = ContentValues()
        // estamos insertando nuestros valores
        // en forma de par clave-valor
        values.put(NOMBRE_USUARIO,nomUsu)
        values.put(CORREO_USUARIO,mailUsu)
        values.put(CLAVE_USUARIO,passUsu)

        // aquí estamos creando una variable de escritura de nuestra base de datos ya que queremos
        // insertar un valor en nuestra base de datos
        val db = this.writableDatabase

        // todos los valores se insertan en la base de datos
        db.insert(TABLE_NAME,null,values)

        // finalmente cerramos nuestra base de datos
        db.close()
    }

    // el siguiente método es para obtener todos los datos de nuestra base de datos
    fun getUsuario(usuario: String, password: String): Cursor{

        // aquí estamos creando una variable legible de nuestra base de datos ya que queremos leer el valor de ella
        val db = this.readableDatabase

        // debajo del código devuelve un cursor para leer datos de la base de datos
        return db.rawQuery(
            "SELECT * FROM $TABLE_NAME WHERE $NOMBRE_USUARIO = '$usuario' AND $CLAVE_USUARIO = '$password'" ,
            null
        )
    }

    fun addGasto(catGas: String, fecGas: String, descGas: String, monGas: Double){
        val valuesGasto = ContentValues()

        valuesGasto.put(CATEGORIA_GASTO,catGas)
        valuesGasto.put(FECHA_GASTO,fecGas)
        valuesGasto.put(DESCRIPCION_GASTO,descGas)
        valuesGasto.put(MONTO_GASTO,monGas)

        val db = this.writableDatabase

        db.insert(TABLE_NAME2,null,valuesGasto)

        db.close()
    }

    fun getMonto(): Cursor{
        val db = this.readableDatabase
        return db.rawQuery("SELECT categoriaGasto,SUM(montoGasto) AS Monto FROM $TABLE_NAME2 GROUP BY categoriaGasto",
            null)
    }

    fun getGastos(fechaGas: String): Cursor{
        val db = this.readableDatabase

        return db.rawQuery(
            "SELECT * FROM $TABLE_NAME2 WHERE fechaGasto = '$fechaGas';",null
        )
    }

    fun getMontoTotal(): Cursor{
        val db = this.readableDatabase
        return db.rawQuery(
            "SELECT SUM(montoGasto) FROM $TABLE_NAME2",null
        )
    }

    fun deleteGasto(id: Int){
        val db = this.writableDatabase
        db.execSQL(
            "DELETE FROM $TABLE_NAME2 WHERE idGasto = $id"
        )
    }

    companion object{
        // aquí hemos definido variables para nuestra base de datos

        // a continuación hay una variable para el nombre de la base de datos
        private val DATABASE_NAME = "GestionGastos.db"
        // a continuación se muestra la variable para la versión de la base de datos
        private val DATABASE_VERSION = 1

        // Aqui se definen las variables para el nombre de la tabla y sus columnas
        //Tabla usuarios
        val TABLE_NAME = "Usuarios"
        val ID_USUARIO = "idUsuario"
        val NOMBRE_USUARIO = "nombreUsuario"
        val CORREO_USUARIO = "correoUsuario"
        val CLAVE_USUARIO = "claveUsuario"

        //Tabla Gastos
        val TABLE_NAME2 = "Gastos"
        val ID_GASTO = "idGasto"
        val CATEGORIA_GASTO = "categoriaGasto"
        val FECHA_GASTO = "fechaGasto"
        val DESCRIPCION_GASTO = "descripcionGasto"
        val MONTO_GASTO = "montoGasto"
    }

}