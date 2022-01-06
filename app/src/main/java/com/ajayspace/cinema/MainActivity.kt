package com.ajayspace.cinema


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.NavigationUI
import android.view.View
import android.view.View.GONE
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = this.findNavController(R.id.nav_fragment)
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar

        // using toolbar as ActionBar
        setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController)

        //changing toolbar color
        val colorDrawable = ColorDrawable(resources.getColor(R.color.background))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        //adding navigation drawer
        var nav_view = findViewById<NavigationView>(R.id.navView)
        var drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)

        NavigationUI.setupWithNavController(nav_view, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        //setting fragment outside of drawer layout in xml so disabling it wont affect fragment visibility
        drawerLayout.visibility= GONE

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_fragment)
        var drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}