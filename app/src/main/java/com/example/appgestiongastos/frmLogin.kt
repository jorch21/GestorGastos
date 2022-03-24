package com.example.appgestiongastos

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_frm_login.*
import kotlinx.android.synthetic.main.dialog_recuperar_clave.*

class frmLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frm_login)

        //abrirPrincipal()
        abrirRegistro()
        abrirDialogCorreo()
        iniciarSesion()

        getSupportActionBar()?.hide()
    }

    fun abrirPrincipal(){
        var vlUsuario = txtNombreUsuario.editText?.text.toString()
        val vlAbrirPrincipal = Intent(this,frmPrincipal::class.java)
        vlAbrirPrincipal.putExtra("vtUsuario",vlUsuario)
        startActivity(vlAbrirPrincipal)
        finish()
    }

    fun abrirRegistro(){
        btnRegistro.setOnClickListener(){
            val vlAbrirRegistro = Intent(this,frmRegistro::class.java)
            startActivity(vlAbrirRegistro)
        }
    }

    fun abrirDialogCorreo(){
        btnRecuperar.setOnClickListener(){
            ejecutarDialog()
        }
    }

    fun ejecutarDialog(){
        AlertDialog.Builder(this)
            .setView(R.layout.dialog_recuperar_clave)
            .setPositiveButton("Enviar"){_,_->
                Toast.makeText(this,"Correo Enviado",Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    fun iniciarSesion(){
        btnIniciarSesion.setOnClickListener(){
            val usuario = txtNombreUsuario.editText?.text.toString()
            val password = txtPassword.editText?.text.toString()
            val db = DBHelper(this,null)

            val cursor = db.getUsuario(usuario,password)

            if (usuario.isNotEmpty() && password.isNotEmpty()){
                try {
                    if (cursor.moveToFirst()){
                        val user = cursor.getString(1)
                        val pass = cursor.getString(3)

                        if (usuario.equals(user) && password.equals(pass)){
                            abrirMensajeBienvenida()
                        }
                    }else{
                        abrirMensajeError()
                    }
                }catch (ex:Exception){
                    Toast.makeText(this,"Error $ex",Toast.LENGTH_SHORT).apply {
                        setGravity(Gravity.CENTER,0,0)
                        show()
                    }
                }
            }else{
                abrirMensajeVacios()
            }

            cursor.close()
        }
    }

    fun abrirMensajeError(){
        val alertaMensaje = AlertDialog.Builder(this)
            .setTitle("Error al Iniciar Sesion")
            .setIcon(R.drawable.error)
            .setMessage("Los datos ingresados son incorrectos")
            .setPositiveButton("OK"){_,_->limpiarTextos()}
        val mostrarAlerta = alertaMensaje.create()
        mostrarAlerta.show()
    }

    fun abrirMensajeBienvenida(){
        val alertaMensaje = AlertDialog.Builder(this)
            .setTitle("Datos Correctos")
            .setIcon(R.drawable.success)
            .setMessage("Bienvenido a la app")
            .setPositiveButton("OK"){_,_->abrirPrincipal()}
        val mostrarAlerta = alertaMensaje.create()
        mostrarAlerta.show()
    }

    fun abrirMensajeVacios(){
        val alertaMensaje = AlertDialog.Builder(this)
            .setTitle("Error!! Campos Vacios")
            .setIcon(R.drawable.campos_vacios)
            .setMessage("Debe completar los campos para continuar con el proceso")
            .setPositiveButton("OK"){_,_->limpiarTextos()}
        val mostrarAlerta = alertaMensaje.create()
        mostrarAlerta.show()
    }

    fun limpiarTextos(){
        txtNombreUsuario.editText?.text = null
        txtPassword.editText?.text = null
        txtNombreUsuario.editText?.clearFocus()
        txtPassword.editText?.clearFocus()
    }
}