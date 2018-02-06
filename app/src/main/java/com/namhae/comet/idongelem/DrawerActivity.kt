package com.namhae.comet.idongelem

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.app_bar_drawer.*

open class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_call -> {
                val intent = Intent(this@DrawerActivity, CallActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_meal -> {
                val intent = Intent(this@DrawerActivity, MealActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_notice -> {
                val intent = Intent(this@DrawerActivity, BoardActivity::class.java)
                intent.putExtra("value", "1")
                startActivity(intent)

            }
            R.id.nav_comm -> {

                val intent = Intent(this@DrawerActivity, BoardActivity::class.java)
                intent.putExtra("value", "4")
                startActivity(intent)

            }
            R.id.nav_album -> {

                val intent = Intent(this@DrawerActivity, AlbumActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_class -> {
                val intent = Intent(this@DrawerActivity, StudentClassActivity::class.java)
                startActivity(intent)

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
