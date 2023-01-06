package com.example.routes.RoutesListActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.routes.MainActivity
import com.example.routes.R
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.ActivityRoutesListBinding
import com.example.routes.globalSettings.GlobalSettingsActivity
import com.example.routes.net.DiscoverNewRoutesActivity
import com.example.routes.net.ShareYourRoutesActivity
import com.example.routes.routeViewActivity.RouteActivity
import com.google.android.material.navigation.NavigationView

class RoutesListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityRoutesListBinding
    private lateinit var dbManager: DbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutesListBinding.inflate(layoutInflater)
        binding.toolbar.title = "My routes"
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.side_bar_share_activity, R.id.side_bar_discover_activity,
            R.id.side_bar_generator_activity, R.id.side_bar_my_routes_activity, R.id.side_bar_global_settings_activity), binding.routesListDrawerLayout)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.routesListDrawerLayout, binding.toolbar,
            R.string.drawer_open, R.string.drawer_close)
        binding.routesListDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        binding.routesListNavigationView.setNavigationItemSelectedListener(this)


        dbManager = DbManager(this)
        updateRoutesCardsList()
    }

    private fun updateRoutesCardsList(){
        val routes = dbManager.getAllRoutes()
        CardAdapter.drawRouteCards(binding.routesListLinearLayout, routes, ::openRouteActivity)
        if (routes.isEmpty()) binding.noSavedRoutesMessage.visibility = View.VISIBLE
        else binding.noSavedRoutesMessage.visibility = View.GONE
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.side_bar_share_activity -> {
                val intent = Intent(this, ShareYourRoutesActivity::class.java)
                startActivity(intent)
                false
            }
            R.id.side_bar_discover_activity -> {
                val intent = Intent(this, DiscoverNewRoutesActivity::class.java)
                startActivity(intent)
                false
            }
            R.id.side_bar_generator_activity -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                false
            }
            R.id.side_bar_global_settings_activity -> {
                val intent = Intent(this, GlobalSettingsActivity::class.java)
                startActivity(intent)
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

    private fun openRouteActivity(route: RouteDTO){
        val intent = Intent(this@RoutesListActivity, RouteActivity::class.java)
        intent.putExtra(RouteActivity.ROUTE_ID, route.id)
        startActivity(intent)
    }

    override fun onResume() {
        binding.routesListDrawerLayout.closeDrawer(GravityCompat.START)
        updateRoutesCardsList()
        super.onResume()
    }
}