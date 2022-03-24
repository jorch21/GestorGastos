package com.example.appgestiongastos

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_frm_agregar_gasto.*

class frmAgregarGasto : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frm_agregar_gasto)

        cargarComboBox()
        abrirCalendario()
        agregarGasto()
    }

    fun agregarGasto(){
        btnAgregarGasto.setOnClickListener(){
            val db = DBHelper(this,null)

            val catGas = txtCombo.editText?.text.toString()
            val fecGas = txtFecha.text.toString()
            val descGas = txtDescripcion.text.toString()
            val monGas = txtGasto.text.toString().toDouble()

            if (catGas=="" && fecGas=="" && descGas=="" && monGas==0.00){
                abrirMensajeVaciosGastos()
            }else{
                db.addGasto(catGas,fecGas,descGas,monGas)

                abrirMensajeGastos()
            }
        }
    }

    fun abrirMensajeVaciosGastos(){
        val alertaMensaje = AlertDialog.Builder(this)
            .setTitle("Error!! Campos Vacios")
            .setIcon(R.drawable.campos_vacios)
            .setMessage("Debe completar los campos para continuar con el proceso")
            .setPositiveButton("OK"){_,_->limpiarTextosGastos()}
        val mostrarAlerta = alertaMensaje.create()
        mostrarAlerta.show()
    }

    fun abrirMensajeGastos(){
        val alertaMensaje = AlertDialog.Builder(this)
            .setTitle("Registro Correcto")
            .setIcon(R.drawable.success)
            .setMessage("Se proceso su registro correctamente")
            .setPositiveButton("OK"){_,_->volverPrincipal()}
        val mostrarAlerta = alertaMensaje.create()
        mostrarAlerta.show()
    }

    fun limpiarTextosGastos(){
        txtCombo.editText?.text = null
        txtFecha.text = null
        txtDescripcion.text = null
        txtGasto.setText("0.00")
        txtCombo.editText?.clearFocus()
        txtFecha.clearFocus()
        txtDescripcion.clearFocus()
        txtGasto.clearFocus()
    }

    fun volverPrincipal(){
        val refresh = Intent(this,frmPrincipal::class.java)
        startActivity(refresh)
        finish()
    }

    fun abrirCalendario(){
        txtFecha.setOnClickListener{
            showDatePickerDialog()
        }
    }


    fun cargarComboBox(){
        val items = listOf("Comida","Cuentas y Pagos","Hogar","Transporte","Ropa","Salud e Higiene","Compras","Recreaciones")
        val adapter = ArrayAdapter(this,R.layout.list_item, items)
        (txtCombo.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun showDatePickerDialog(){
        val newFragment = DatePickerFragment.newInstance(DatePickerDialog.OnDateSetListener{_,year,month,day ->
            val selectedDate = "${year}-${month+1}-${day.toString()}"
            txtFecha.setText(selectedDate)
        })

        newFragment.show(supportFragmentManager,"datePicker")
    }

}