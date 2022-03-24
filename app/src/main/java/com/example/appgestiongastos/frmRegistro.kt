package com.example.appgestiongastos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isNotEmpty
import kotlinx.android.synthetic.main.activity_frm_login.*
import kotlinx.android.synthetic.main.activity_frm_registro.*

class frmRegistro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frm_registro)

        registrarUsuario()

        getSupportActionBar()?.hide()
    }

    fun registrarUsuario(){
        btnAceptar.setOnClickListener(){
            val db = DBHelper(this,null)

            val nomUsu = txtUsuario.editText?.text.toString()
            val mailUsu = txtCorreo.editText?.text.toString()
            val passUsu = txtClave.editText?.text.toString()

            if (nomUsu == "" && mailUsu == "" && passUsu ==""){
                abrirMensajeVaciosRegistro()
            }else{
                db.addUsuario(nomUsu,mailUsu,passUsu)

                abrirMensajeRegistro()
            }
        }
    }

    fun abrirMensajeVaciosRegistro(){
        val alertaMensaje = AlertDialog.Builder(this)
            .setTitle("Error!! Campos Vacios")
            .setIcon(R.drawable.campos_vacios)
            .setMessage("Debe completar los campos para continuar con el proceso")
            .setPositiveButton("OK"){_,_->limpiarTextosRegistro()}
        val mostrarAlerta = alertaMensaje.create()
        mostrarAlerta.show()
    }

    fun abrirMensajeRegistro(){
        val alertaMensaje = AlertDialog.Builder(this)
            .setTitle("Registro Correcto")
            .setIcon(R.drawable.success)
            .setMessage("Se proceso su registro correctamente")
            .setPositiveButton("OK"){_,_->limpiarTextosRegistro()}
        val mostrarAlerta = alertaMensaje.create()
        mostrarAlerta.show()
    }

    fun limpiarTextosRegistro(){
        txtUsuario.editText?.text = null
        txtCorreo.editText?.text = null
        txtClave.editText?.text = null
        txtUsuario.editText?.clearFocus()
        txtCorreo.editText?.clearFocus()
        txtClave.editText?.clearFocus()
    }
}