package com.example.routes.localSettingsFragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.routes.DrawerLocker
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.MyColor
import com.example.routes.dataStuff.WallDTO
import com.example.routes.databinding.FragmentLocalSettingsBinding

class LocalSettingsFrag : Fragment() {
    private var _binding: FragmentLocalSettingsBinding? = null
    private lateinit var walls: ArrayList<WallDTO>
    private lateinit var dbManager: DbManager
    private lateinit var sharedSettingsPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocalSettingsBinding.inflate(inflater, container, false)
        dbManager = DbManager(activity)
        walls = arrayListOf(dbManager.getWall(DbManager.WALLS_NAMES[0]))
        return binding.root
    }

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedSettingsPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedSettingsPreferences.edit()

        val lastUsedWall = sharedSettingsPreferences.getString("currentWall", DbManager.WALLS_NAMES[0])
        when(lastUsedWall){
            DbManager.WALLS_NAMES[0] -> binding.wallARadioBtn.isChecked = true
            DbManager.WALLS_NAMES[1] -> binding.wallBRadioBtn.isChecked = true
            DbManager.WALLS_NAMES[2] -> binding.wallCRadioBtn.isChecked = true
            else -> { binding.wallARadioBtn.isChecked = true }
        }
        updateRecyclerView(lastUsedWall!!)

        (activity as DrawerLocker).setDrawerEnabled(false)

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            var currentWall = when (binding.radioGroup.checkedRadioButtonId.toString()) {
                binding.wallARadioBtn.id.toString() -> DbManager.WALLS_NAMES[0]
                binding.wallBRadioBtn.id.toString() -> DbManager.WALLS_NAMES[1]
                binding.wallCRadioBtn.id.toString() -> DbManager.WALLS_NAMES[2]
                else -> ""
            }
            sharedPreferencesEditor.putString("currentWall", currentWall)
            sharedPreferencesEditor.apply()
            updateRecyclerView(currentWall)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateRecyclerView(wall: String){
        val colorsToDraw = when(wall){
            DbManager.WALLS_NAMES[0] -> dbManager.getWall(DbManager.WALLS_NAMES[0]).colorsOnTheWall
            DbManager.WALLS_NAMES[1] -> dbManager.getWall(DbManager.WALLS_NAMES[1]).colorsOnTheWall
            DbManager.WALLS_NAMES[2] -> dbManager.getWall(DbManager.WALLS_NAMES[2]).colorsOnTheWall
            else -> arrayListOf()
        }
        val checkChangedHandler = {color: MyColor ->
            color.isCheckedInLocalSettings = !color.isCheckedInLocalSettings
            val result = dbManager.updateColorOnTheWall(color)
        }
        CardAdapter.drawCheckableColorCards(binding.localSettingsLinearLayout, colorsToDraw, checkChangedHandler)
        if (colorsToDraw.isEmpty()) binding.noAddedColorsMessage.visibility = View.VISIBLE
        else binding.noAddedColorsMessage.visibility = View.GONE
    }

    override fun onResume() {
//        walls = arrayListOf(dataManager.getWall(DataManager.WALL_A_NAME)!!, dataManager.getWall(DataManager.WALL_B_NAME)!!, dataManager.getWall(DataManager.WALL_C_NAME)!!)
//        updateRecyclerView(sharedSettingsPreferences.getString("currentWall", DataManager.WALL_A_NAME)!!)
        super.onResume()
    }

    override fun onDestroyView() {
        (activity as DrawerLocker).setDrawerEnabled(true)

        _binding = null
        super.onDestroyView()
    }
}