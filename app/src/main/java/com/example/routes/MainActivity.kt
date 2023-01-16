package com.example.routes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.example.routes.RoutesListActivity.RoutesListActivity
import com.example.routes.databinding.ActivityMainBinding
import com.example.routes.globalSettings.GlobalSettingsActivity
import com.example.routes.net.DiscoverNewRoutesActivity
import com.example.routes.net.ShareYourRoutesActivity
import com.example.routes.userStuff.AccountUtils
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DrawerLocker {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("nightMode", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.appBarLayout.toolbar.title = "Generator"
        setContentView(binding.root)
        setSupportActionBar(binding.appBarLayout.toolbar)

        val account = Auth0(getString(R.string.client_id_auth0), getString(R.string.domain_auth0))
        val apiClient = AuthenticationAPIClient(account)
        AppRuntimeData.accountUtils = AccountUtils(account, CredentialsManager(apiClient, SharedPreferencesStorage(this)))


        appBarConfiguration = AppBarConfiguration(setOf(R.id.side_bar_share_activity, R.id.side_bar_discover_activity,
        R.id.side_bar_generator_activity, R.id.side_bar_my_routes_activity, R.id.side_bar_global_settings_activity), binding.activityMainDrawerLayout)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.activityMainDrawerLayout, binding.appBarLayout.toolbar,
                    R.string.drawer_open, R.string.drawer_close)
        binding.activityMainDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        binding.activityMainNavigationView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onResume() {
        setDrawerEnabled(true)
        binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START)
        super.onResume()
    }

    override fun setDrawerEnabled(enabled: Boolean) {
        val locker = if (enabled) DrawerLayout.LOCK_MODE_UNLOCKED
                     else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        binding.activityMainDrawerLayout.setDrawerLockMode(locker)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = enabled
    }
}