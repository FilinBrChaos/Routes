package com.example.routes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.example.routes.RoutesListActivity.RoutesListActivity
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.ActivityMainBinding
import com.example.routes.globalSettings.GlobalSettingsActivity
import com.google.android.material.navigation.NavigationView
import java.util.ArrayList

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarLayout.toolbar)

        AppRuntimeData.globalDbManager = DbManager(this)
        val sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("nightMode", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val jsonStr = sharedPreferences.getString("lastArrayForRandomSequence", "")
        val jsonArr = jsonStr!!.split("/")
        val savedColorsListInLocalSettings: ArrayList<MyColor> = arrayListOf()
        for (colorInStr in jsonArr)
            if (colorInStr != "")
                savedColorsListInLocalSettings.add(MyColor.parseFromString(colorInStr))
        AppRuntimeData.colorsListInLocalSettings = savedColorsListInLocalSettings


        appBarConfiguration = AppBarConfiguration(setOf(R.id.side_bar_share_activity, R.id.side_bar_discover_activity,
        R.id.side_bar_generator_activity, R.id.side_bar_my_routes_activity, R.id.side_bar_global_settings_activity), binding.activityMainDrawerLayout)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.activityMainDrawerLayout, binding.appBarLayout.toolbar,
                    R.string.drawer_open, R.string.drawer_close)
        binding.activityMainDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        binding.activityMainNavigationView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
//            R.id.action_settings -> {
//                val intent = Intent(this, GlobalSettingsActivity::class.java)
//                startActivity(intent)
//                true
//            }
//            R.id.action_home -> {
//                val intent = Intent(this, RoutesListActivity::class.java)
//                startActivity(intent)
//                true
//            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
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
        binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START)
        //TODO make beautiful drawer closing
        super.onResume()
    }
    override fun onPause() {
        //binding.activityMainDrawerLayout.closeDrawer(GravityCompat.START)
        super.onPause()
    }
}