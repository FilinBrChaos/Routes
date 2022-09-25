package com.example.routes.globalSettings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.colorPicker.ColorPickerActivity
import com.example.routes.dataStuff.MyColor
import com.example.routes.dataStuff.DbManager
import com.example.routes.databinding.GlobalSettingsActivityBinding

class GlobalSettingsActivity : AppCompatActivity() {
    private lateinit var binding: GlobalSettingsActivityBinding
    private lateinit var dbManager: DbManager
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var colorPickerCallerButton: Button
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Intent? = result.data
            if (data != null)
                data.apply {
                    val colorName = getStringExtra(ColorPickerActivity.RESULT_COLOR_NAME)
                    val colorValue = getStringExtra(ColorPickerActivity.RESULT_COLOR_VALUE)
                    if (colorName != null && colorValue != null) { addColorRecordToDb(colorName, colorValue) }
                }
            else {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = GlobalSettingsActivityBinding.inflate(layoutInflater)
        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        setContentView(binding.root)
        //binding.recyclerViewWallA.setHasFixedSize(false)
        //binding.recyclerViewWallA.isNestedScrollingEnabled = false
        binding.nightModeSwitch.isChecked = sharedPreferences.getBoolean("nightMode", false)

        binding.addButtonWallA.setOnClickListener{
            colorPickerCallerButton = binding.addButtonWallA
            val intent = Intent(this, ColorPickerActivity::class.java)
            resultLauncher.launch(intent)
        }
        binding.addButtonWallB.setOnClickListener{
            colorPickerCallerButton = binding.addButtonWallB
            val intent = Intent(this, ColorPickerActivity::class.java)
            resultLauncher.launch(intent)
        }
        binding.addButtonWallC.setOnClickListener{
            colorPickerCallerButton = binding.addButtonWallC
            val intent = Intent(this, ColorPickerActivity::class.java)
            resultLauncher.launch(intent)
        }
        dbManager = DbManager(this)

        dbManager.onDbChanged = {
            updateRecyclerView(binding.linearLayoutWallA)
            updateRecyclerView(binding.linearLayoutWallB)
            updateRecyclerView(binding.linearLayoutWallC)
        }
        updateRecyclerView(binding.linearLayoutWallA)
        updateRecyclerView(binding.linearLayoutWallB)
        updateRecyclerView(binding.linearLayoutWallC)
    }

    private fun updateRecyclerView(linearLayout: LinearLayout){
        val colorsOnTheWall: ArrayList<MyColor> = when(linearLayout){
            binding.linearLayoutWallA -> dbManager.getWall(dbManager.wallName_A).colorsOnTheWall
            binding.linearLayoutWallB -> dbManager.getWall(dbManager.wallName_B).colorsOnTheWall
            binding.linearLayoutWallC -> dbManager.getWall(dbManager.wallName_C).colorsOnTheWall
            else -> throw Exception("Database don't contain records relative with this wiew")
        }
        CardAdapter.drawEditableDeletableColorCards(linearLayout, this@GlobalSettingsActivity, colorsOnTheWall, dbManager)
    }

    private fun addColorRecordToDb(colorName: String, colorValue: String){
        when(colorPickerCallerButton){
            binding.addButtonWallA -> dbManager.addColorToWall(dbManager.wallName_A, colorName, colorValue);
            binding.addButtonWallB -> dbManager.addColorToWall(dbManager.wallName_B, colorName, colorValue);
            binding.addButtonWallC -> dbManager.addColorToWall(dbManager.wallName_C, colorName, colorValue);
        }
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
}