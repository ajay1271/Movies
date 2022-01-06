package com.ajayspace.cinema


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

import androidx.appcompat.app.ActionBarDrawerToggle


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_fragment) as NavHostFragment
        val navController = this.findNavController(R.id.nav_fragment)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        // using toolbar as ActionBar

        // using toolbar as ActionBar
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController)

        val colorDrawable = ColorDrawable(resources.getColor(R.color.background))

        supportActionBar?.setBackgroundDrawable(colorDrawable)

        var nav_view = findViewById<NavigationView>(R.id.navView)
        var drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        NavigationUI.setupWithNavController(nav_view, navController)

        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)





    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_fragment)
        var drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}