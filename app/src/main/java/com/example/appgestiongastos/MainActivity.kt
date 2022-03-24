package com.example.appgestiongastos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Aqui se especifica el tiempo de carga del Splash Screen
    private val SPLASH_TIME_OUT: Long = 2000 //2seg

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            /*Este metodo se ejecutara una vez que termine el temporizador declarado al inicio
            para posteriormente inicial el formulario de login
             */
            startActivity(Intent(this,frmLogin::class.java))

            //cerrar este activity
            finish()
        },SPLASH_TIME_OUT)

    }
}