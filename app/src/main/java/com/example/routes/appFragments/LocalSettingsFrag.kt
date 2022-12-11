package com.example.routes.appFragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.dataStuff.DbManager
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
//            DataManager.WALL_A_NAME -> binding.wallARadioBtn.isChecked = true
//            DataManager.WALL_B_NAME -> binding.wallBRadioBtn.isChecked = true
//            DataManager.WALL_C_NAME -> binding.wallCRadioBtn.isChecked = true
            else -> { binding.wallARadioBtn.isChecked = true }
        }
        updateRecyclerView(lastUsedWall!!)
//todo this
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            var currentWall = when (binding.radioGroup.checkedRadioButtonId.toString()){
//                binding.wallARadioBtn.id.toString() -> DataManager.WALL_A_NAME
//                binding.wallBRadioBtn.id.toString() -> DataManager.WALL_B_NAME
//                binding.wallCRadioBtn.id.toString() -> DataManager.WALL_C_NAME
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
            DbManager.WALLS_NAMES[0] -> walls[0].colorsOnTheWall
//            DataManager.WALL_B_NAME -> walls[1].colorsOnTheWall
//            DataManager.WALL_C_NAME -> walls[2].colorsOnTheWall
            else -> arrayListOf()
        }
        if (colorsToDraw.isNotEmpty())
            CardAdapter.drawCheckableColorCards(binding.localSettingsLinearLayout, colorsToDraw)
    }

    private fun saveAllWalls(){
//        dataManager.saveWall(walls[0])
//        dataManager.saveWall(walls[1])
//        dataManager.saveWall(walls[2])
    }

    override fun onResume() {
//        walls = arrayListOf(dataManager.getWall(DataManager.WALL_A_NAME)!!, dataManager.getWall(DataManager.WALL_B_NAME)!!, dataManager.getWall(DataManager.WALL_C_NAME)!!)
//        updateRecyclerView(sharedSettingsPreferences.getString("currentWall", DataManager.WALL_A_NAME)!!)
        super.onResume()
    }

    override fun onPause() {
        saveAllWalls()
        super.onPause()
    }

    override fun onDestroyView() {
        saveAllWalls()
        _binding = null
        super.onDestroyView()
    }
}