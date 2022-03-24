package com.example.appgestiongastos

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.mikephil.charting.animation.Easing.EaseInOutQuad
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.ArrayList

class frmPrincipal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle
    private var exitEnable = false
    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frm_principal)

        pieChart = findViewById(R.id.pieChartGastos)
        configurarPieChart()
        cargarDatosPieChart()
        obtenerTotalGastos()

        abrirMenuDrawer()
    }

    fun obtenerTotalGastos() {
        val db = DBHelper(this, null)

        val cursor = db.getMontoTotal()

        try {
            if (cursor != null) {
                cursor.moveToFirst()
                do {
                    val montoTotal = cursor.getString(0)
                    txtGastosTotalesMes.text = "S/. " + montoTotal
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
        } catch (ex: Exception) {
            Toast.makeText(this, "Error $ex", Toast.LENGTH_LONG).apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }
    }

    override fun onBackPressed() {
        if (exitEnable){
            super.onBackPressed()
        }
        exitEnable = true
        Toast.makeText(this,"Presione atras una vez mas para salir",Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { exitEnable = false }, 2000)
    }

    fun abrirMenuDrawer(){
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toogle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerView = navigationView.getHeaderView(0)
        val textView = headerView.findViewById<TextView>(R.id.txtUsuarioConectado)

        val bundle: Bundle? = intent.extras
        var vtUsuario = bundle!!.getString("vtUsuario")
        textView.setText(vtUsuario)
    }

    fun abrirAgregarGastos(){
        val vlAgregarGastos = Intent(this, frmAgregarGasto::class.java)
        startActivity(vlAgregarGastos)
    }

    fun abrirGastosPorCategoria(){
        val vlAbrirGastosPorCategoria = Intent(this, frmGastosPorCategoria::class.java)
        startActivity(vlAbrirGastosPorCategoria)
    }

    fun abrirListaGastos(){
        val vlAbrirListaGastos = Intent(this,frmListaGastos::class.java)
        startActivity(vlAbrirListaGastos)
    }

    fun cerrarSesion(){
        val alertaMensaje = AlertDialog.Builder(this)
            .setTitle("Gestion de Gastos")
            .setIcon(R.drawable.warning)
            .setMessage("¿Esta seguro de cerrar la sesion?")
            .setPositiveButton("SI"){_,_->finishAffinity()}
            .setNegativeButton("NO"){_,_->}
        val alertaDialogo = alertaMensaje.create()
        alertaDialogo.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_item_one ->abrirAgregarGastos()
            R.id.nav_item_two ->abrirGastosPorCategoria()
            R.id.nav_item_three ->abrirListaGastos()
            R.id.nav_item_seven ->cerrarSesion()
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onPostCreate(savedInstanceState, persistentState)
        toogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toogle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configurarPieChart(){
        pieChart.setDrawHoleEnabled(true)
        //pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(10f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setCenterText("Gasto por Categoría")
        pieChart.setCenterTextSize(25f)
        pieChart.getDescription().setEnabled(false)

        var l: Legend = pieChart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setDrawInside(false)
        l.setEnabled(true)
    }

    private fun cargarDatosPieChart(){
        val entries = ArrayList<PieEntry>()
        val db = DBHelper(this,null)
        val cursor = db.getMonto()

        try {
            if (cursor!=null){
                cursor.moveToFirst()
                do {
                    val categoria = cursor.getString(0)
                    val monto = cursor.getString(1)

                    entries.add(PieEntry(monto.toFloat(),categoria))

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
        //colores para el grafico
        val colors = ArrayList<Int>()
        for (i in ColorTemplate.MATERIAL_COLORS){
            colors.add(i)
        }

        for (i in ColorTemplate.VORDIPLOM_COLORS){
            colors.add(i)
        }

        //Para el dataset
        val dataSet = PieDataSet(entries,"Categorías")
        dataSet.setColors(colors)

        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)

        pieChart.setData(data)
        pieChart.invalidate()

        pieChart.animateY(1400,EaseInOutQuad)
    }
}
