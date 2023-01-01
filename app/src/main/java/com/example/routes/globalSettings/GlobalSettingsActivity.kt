package com.example.routes.globalSettings

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.navigation.ui.AppBarConfiguration
import com.example.routes.MainActivity
import com.example.routes.R
import com.example.routes.RoutesListActivity.RoutesListActivity
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.colorPicker.ColorPickerActivity
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.MyColor
import com.example.routes.dataStuff.WallDTO
import com.example.routes.databinding.GlobalSettingsActivityBinding
import com.google.android.material.navigation.NavigationView

class GlobalSettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var binding: GlobalSettingsActivityBinding
    private  lateinit var dbManager: DbManager

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var colorPickerCallerButton: Button
    private lateinit var updatedColor: MyColor
    private val colorPickerCreateNewColorResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            if (data != null)
                data.apply {
                    val colorName = getStringExtra(ColorPickerActivity.RESULT_COLOR_NAME)
                    val colorValue = getStringExtra(ColorPickerActivity.RESULT_COLOR_VALUE)
                    if (colorName != null && colorValue != null) { saveColor(colorName, colorValue) }
                }
            else {}
        }
    }

    private val colorPickerUpdateColorResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            if (data != null)
                data.apply {
                    val colorName = getStringExtra(ColorPickerActivity.RESULT_COLOR_NAME)
                    val colorValue = getStringExtra(ColorPickerActivity.RESULT_COLOR_VALUE)
                    if (colorName != null && colorValue != null) { updateColor(colorName, colorValue) }
                }
            else {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GlobalSettingsActivityBinding.inflate(layoutInflater)
        binding.toolbar.title = "Global settings"
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.nightModeSwitch.isChecked = sharedPreferences.getBoolean("nightMode", false)

        binding.addButtonWallA.setOnClickListener{
            colorPickerCallerButton = binding.addButtonWallA
            val intent = Intent(this, ColorPickerActivity::class.java)
            colorPickerCreateNewColorResultLauncher.launch(intent)
        }
        binding.addButtonWallB.setOnClickListener{
            colorPickerCallerButton = binding.addButtonWallB
            val intent = Intent(this, ColorPickerActivity::class.java)
            colorPickerCreateNewColorResultLauncher.launch(intent)
        }
        binding.addButtonWallC.setOnClickListener{
            colorPickerCallerButton = binding.addButtonWallC
            val intent = Intent(this, ColorPickerActivity::class.java)
            colorPickerCreateNewColorResultLauncher.launch(intent)
        }

        dbManager = DbManager(this)

        updateRecyclerView(binding.linearLayoutWallA)
        updateRecyclerView(binding.linearLayoutWallB)
        updateRecyclerView(binding.linearLayoutWallC)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.side_bar_share_activity, R.id.side_bar_discover_activity,
            R.id.side_bar_generator_activity, R.id.side_bar_my_routes_activity, R.id.side_bar_global_settings_activity), binding.globalSettingsDrawerLayout)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.globalSettingsDrawerLayout, binding.toolbar,
            R.string.drawer_open, R.string.drawer_close)
        binding.globalSettingsDrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        binding.globalSettingsNavigationView.setNavigationItemSelectedListener(this)
    }

    private fun updateRecyclerView(linearLayout: LinearLayout){
        val colorsOnTheWall = when(linearLayout){
            binding.linearLayoutWallA -> dbManager.getWall(DbManager.WALLS_NAMES[0]).colorsOnTheWall
            binding.linearLayoutWallB -> dbManager.getWall(DbManager.WALLS_NAMES[1]).colorsOnTheWall
            binding.linearLayoutWallC -> dbManager.getWall(DbManager.WALLS_NAMES[2]).colorsOnTheWall
            else -> { throw Exception("No colors on that wall") }
        }
        val editButtonHandler = { color: MyColor ->
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Confirm edit")
            dialogBuilder.setMessage("You want to edit this item")
            dialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, _ ->
                updatedColor = color
                val intent = Intent(this, ColorPickerActivity::class.java)
                intent.putExtra(ColorPickerActivity.INPUT_COLOR_NAME_PLACEHOLDER, color.colorName)
                intent.putExtra(ColorPickerActivity.INPUT_COLOR_VALUE, color.colorValue)
                colorPickerUpdateColorResultLauncher.launch(intent)
                updateRecyclerView(linearLayout)
                dialog.cancel()
            })
            dialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener{ dialog, _ ->
                dialog.cancel()
            })
            val alert = dialogBuilder.create()
            alert.show()
        }
        val deleteButtonHandler = { color: MyColor ->
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Confirm delete")
            dialogBuilder.setMessage("Are you sure to delete this item")
            dialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, _ ->
                dbManager.removeColorFromTheWall(color.id)
                updateRecyclerView(linearLayout)
                dialog.cancel()
            })
            dialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener{ dialog, _ ->
                dialog.cancel()
            })
            val alert = dialogBuilder.create()
            alert.show()
        }

        CardAdapter.drawEditableDeletableColorCards(linearLayout, colorsOnTheWall, deleteButtonHandler, editButtonHandler)
    }

    private fun saveColor(colorName: String, colorValue: String){
        when(colorPickerCallerButton){
            binding.addButtonWallA -> dbManager.addColorToWall(MyColor(DbManager.WALLS_NAMES[0], colorName, colorValue, false))
            binding.addButtonWallB -> dbManager.addColorToWall(MyColor(DbManager.WALLS_NAMES[1], colorName, colorValue, false))
            binding.addButtonWallC -> dbManager.addColorToWall(MyColor(DbManager.WALLS_NAMES[2], colorName, colorValue, false))
        }
    }

    private fun updateColor(colorName: String, colorValue: String){
        updatedColor.colorName = colorName
        updatedColor.colorValue = colorValue
        dbManager.updateColorOnTheWall(updatedColor)
    }

    fun changeColorMode(view: View) {
        var editor = sharedPreferences.edit()
        if (binding.nightModeSwitch.isChecked){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            editor.putBoolean("nightMode", true)
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            editor.putBoolean("nightMode", false)
        }
        editor.apply()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        binding.globalSettingsDrawerLayout.closeDrawer(GravityCompat.START)
        updateRecyclerView(binding.linearLayoutWallA)
        updateRecyclerView(binding.linearLayoutWallB)
        updateRecyclerView(binding.linearLayoutWallC)
        super.onResume()
    }
}