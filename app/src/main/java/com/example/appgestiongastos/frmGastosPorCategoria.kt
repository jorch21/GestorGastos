package com.example.appgestiongastos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_frm_gastos_por_categoria.*

class frmGastosPorCategoria : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frm_gastos_por_categoria)

        cargarMontosCategorias()
    }

    fun cargarMontosCategorias(){

        val db = DBHelper(this,null)

        val cursor = db.getMonto()

        val listView = findViewById<ListView>(R.id.listaCategorias) as ListView
        val categorias = arrayOf("Comida","Cuentas y Pagos","Hogar","Transporte","Ropa","Salud e Higiene","Compras","Recreaciones")
        //val adp: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,categorias)
        val adp: ArrayAdapter<String> = ArrayAdapter(this,R.layout.list_item,categorias)
        listView.adapter = adp
        val listView1 = findViewById<ListView>(R.id.listaMontos) as ListView
        val montos = arrayOf(0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00)
        //val adp1: ArrayAdapter<Double> = ArrayAdapter(this,android.R.layout.simple_list_item_1,montos)
        val adp1: ArrayAdapter<Double> = ArrayAdapter(this,R.layout.list_gastos,montos)
        listView1.adapter = adp1

        try {
            if (cursor!=null){
                cursor.moveToFirst()
                do {
                    val categoria = cursor.getString(0)
                    val monto = cursor.getString(1)

                    when(categoria){
                        categorias[0]->{montos[0] += monto.toDouble()}
                        categorias[1]->{montos[1] += monto.toDouble()}
                        categorias[2]->{montos[2] += monto.toDouble()}
                        categorias[3]->{montos[3] += monto.toDouble()}
                        categorias[4]->{montos[4] += monto.toDouble()}
                        categorias[5]->{montos[5] += monto.toDouble()}
                        categorias[6]->{montos[6] += monto.toDouble()}
                        categorias[7]->{montos[7] += monto.toDouble()}
                    }
                }while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
        }catch (ex:Exception){
            Toast.makeText(this,"Error $ex", Toast.LENGTH_LONG).apply {
                setGravity(Gravity.CENTER,0,0)
                show()
            }
        }

    }

}