package com.example.appgestiongastos

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_frm_lista_gastos.*
import kotlin.math.sin

class frmListaGastos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frm_lista_gastos)

        abrirCalendario()
    }

    fun abrirCalendario(){
        txtFechaConsulta.setOnClickListener{
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog(){
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener{ _, year, month, day ->
            val selectedDate = "${year}-${month+1}-${day.toString()}"
            txtFechaConsulta.setText(selectedDate)
            val listView = findViewById<ListView>(R.id.listView)
            val custom_list_data = ArrayList<CList>()
            val custom_list = CListAdapter(this,custom_list_data)

            val db = DBHelper(this,null)

            val cursor = db.getGastos(selectedDate)

            try {
                if (cursor!=null){
                    cursor.moveToFirst()
                    do{
                        val idgasto = cursor.getString(0)
                        val categoria = cursor.getString(1)
                        val fecha = cursor.getString(2)
                        val descripcion = cursor.getString(3)
                        val gasto = cursor.getString(4)

                        custom_list_data.add(CList(
                            "Categoria: $categoria","Fecha: $fecha",
                            "Descripcion: $descripcion","Gasto: S/. $gasto"
                        ))
                        listView.adapter = custom_list

                        listView.onItemClickListener = AdapterView.OnItemClickListener{parent,view,position,id ->
                            val singleItems = arrayOf("Actualizar","Eliminar")
                            //val checkedItem = 0

                            MaterialAlertDialogBuilder(this)
                                .setTitle("Seleccionar una opción:")
                                .setNeutralButton("Cancelar"){dialog,which->dialog.cancel()}
                                .setSingleChoiceItems(singleItems,-1){dialog,which->
                                    //Toast.makeText(this,"Seleccionaste ${singleItems[which]}",Toast.LENGTH_SHORT).show()
                                    when(singleItems[which]){
                                        "Actualizar" -> {Toast.makeText(this,"Actualizado",Toast.LENGTH_SHORT).show()}
                                        "Eliminar" -> {
                                            eliminarGasto(idgasto.toInt())
                                        }
                                    }
                                    dialog.dismiss()
                                }
                                .show()
                        }

                    }while (cursor.moveToNext())

                }
                cursor.close()
                db.close()
            }catch (ex:Exception){
                abrirMensajeConsulta()
                listView.adapter = null
            }
        })

        newFragment.show(supportFragmentManager,"datePicker")
    }

    fun abrirMensajeConsulta(){
        val alertaMensaje = AlertDialog.Builder(this)
            .setTitle("Consulta Gastos")
            .setIcon(R.drawable.warning)
            .setMessage("No se encontraron datos con la fecha indicada")
            .setPositiveButton("OK"){_,_->}
        val mostrarAlerta = alertaMensaje.create()
        mostrarAlerta.show()
    }

    fun mensajeGasto(){
        val alertaMensaje = AlertDialog.Builder(this)
            .setTitle("Eliminacion Correcta")
            .setIcon(R.drawable.success)
            .setMessage("Registro Eliminado correctamente")
            .setPositiveButton("OK"){_,_->volverPrincipal()}
        val mostrarAlerta = alertaMensaje.create()
        mostrarAlerta.show()
    }

    fun volverPrincipal(){
        val refresh = Intent(this,frmPrincipal::class.java)
        startActivity(refresh)
        finish()
    }

    fun eliminarGasto(id: Int){
        try {
            val db = DBHelper(this,null)
            db.deleteGasto(id)
            mensajeGasto()
        }catch (ex:Exception){
            Toast.makeText(this,"Error $ex",Toast.LENGTH_SHORT).show()
        }
    }
}