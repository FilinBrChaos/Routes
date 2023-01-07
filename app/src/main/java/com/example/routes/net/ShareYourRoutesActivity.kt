package com.example.routes.net

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.example.routes.DrawerLocker
import com.example.routes.MainActivity
import com.example.routes.R
import com.example.routes.RoutesListActivity.RoutesListActivity
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.ActivityShareYourRoutesBinding
import com.example.routes.globalSettings.GlobalSettingsActivity
import com.google.android.material.navigation.NavigationView

class ShareYourRoutesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DrawerLocker {
    private lateinit var binding: ActivityShareYourRoutesBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var dbManager: DbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareYourRoutesBinding.inflate(layoutInflater)
        binding.toolbar.title = "Share your routes"
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.side_bar_share_activity, R.id.side_bar_discover_activity,
            R.id.side_bar_generator_activity, R.id.side_bar_my_routes_activity, R.id.side_bar_global_settings_activity), binding.shareYourRoutesDrawerLayout)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.shareYourRoutesDrawerLayout, binding.toolbar,
            R.string.drawer_open, R.string.drawer_close)
        binding.shareYourRoutesDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        binding.shareYourRoutesNavigationView.setNavigationItemSelectedListener(this)

        dbManager = DbManager(this)
        updateRoutesCardsList()
    }

    private fun updateRoutesCardsList(){
        val routes = dbManager.getAllRoutes()
        CardAdapter.drawRouteCards(binding.routesListLinearLayout, routes, ::shareRouteOpenActivity)
        if (routes.isEmpty()) binding.noSavedRoutesMessage.visibility = View.VISIBLE
        else binding.noSavedRoutesMessage.visibility = View.GONE
    }

    private fun shareRouteOpenActivity(route: RouteDTO){
        val intent = Intent(this, ShareRouteActivity::class.java)
        intent.putExtra(ShareRouteActivity.ROUTE_ID, route.id)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
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
            R.id.side_bar_my_routes_activity -> {
                val intent = Intent(this, RoutesListActivity::class.java)
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

    override fun setDrawerEnabled(enabled: Boolean) {
        val locker = if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED
        else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        binding.shareYourRoutesDrawerLayout.setDrawerLockMode(locker)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = enabled
    }

    override fun onResume() {
        binding.shareYourRoutesDrawerLayout.closeDrawer(GravityCompat.START)
        updateRoutesCardsList()
        super.onResume()
    }
}