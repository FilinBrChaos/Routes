package com.example.routes.net

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.ui.AppBarConfiguration
import com.example.routes.DrawerLocker
import com.example.routes.MainActivity
import com.example.routes.R
import com.example.routes.RoutesListActivity.RoutesListActivity
import com.example.routes.databinding.ActivityShareYourRoutesBinding
import com.example.routes.globalSettings.GlobalSettingsActivity
import com.google.android.material.navigation.NavigationView

class ShareYourRoutesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, DrawerLocker {
    private lateinit var binding: ActivityShareYourRoutesBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration

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
        TODO("Not yet implemented")
    }
}