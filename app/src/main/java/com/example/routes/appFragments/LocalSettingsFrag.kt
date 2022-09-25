package com.example.routes.appFragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.routes.AppRuntimeData
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.dataStuff.MyColor
import com.example.routes.dataStuff.DbManager
import com.example.routes.databinding.FragmentLocalSettingsBinding
import java.lang.StringBuilder

class LocalSettingsFrag : Fragment() {
    private var _binding: FragmentLocalSettingsBinding? = null
    private lateinit var dbManager: DbManager
    private lateinit var sharedSettingsPreferences: SharedPreferences
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLocalSettingsBinding.inflate(inflater, container, false)
        dbManager = DbManager(activity)
        return binding.root
    }

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedSettingsPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)
        sharedPreferencesEditor = sharedSettingsPreferences.edit()

        when(sharedSettingsPreferences.getString("currentWall", DbManager.TABLE_WALL_A)){
            DbManager.TABLE_WALL_A -> binding.wallARadioBtn.isChecked = true
            DbManager.TABLE_WALL_B -> binding.wallBRadioBtn.isChecked = true
            DbManager.TABLE_WALL_C -> binding.wallCRadioBtn.isChecked = true
            else -> {}
        }

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            var currentWall = when (binding.radioGroup.checkedRadioButtonId.toString()){
                binding.wallARadioBtn.id.toString() -> DbManager.TABLE_WALL_A
                binding.wallBRadioBtn.id.toString() -> DbManager.TABLE_WALL_B
                binding.wallCRadioBtn.id.toString() -> DbManager.TABLE_WALL_C
                else -> ""
            }
            sharedPreferencesEditor.putString("currentWall", currentWall)
            sharedPreferencesEditor.apply()
            refreshColorsAndUpdateRecyclerView()
            updateRecyclerView()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateRecyclerView(){
        val cardAdapter = CardAdapter()
        if (AppRuntimeData.colorsListInLocalSettings.isNotEmpty())
            CardAdapter.drawCheckableColorCards(binding.localSettingsLinearLayout, AppRuntimeData.colorsListInLocalSettings)
    }

    private fun refreshColorsAndUpdateRecyclerView() {
        AppRuntimeData.colorsListInLocalSettings = when(binding.radioGroup.checkedRadioButtonId.toString()){
            binding.wallARadioBtn.id.toString() -> dbManager.getWall(dbManager.wallName_A).colorsOnTheWall as ArrayList<MyColor> /* = java.util.ArrayList<com.example.routes.dataStuff.MyColor> */
            binding.wallBRadioBtn.id.toString() -> dbManager.getWall(dbManager.wallName_B).colorsOnTheWall as ArrayList<MyColor> /* = java.util.ArrayList<com.example.routes.dataStuff.MyColor> */
            binding.wallCRadioBtn.id.toString() -> dbManager.getWall(dbManager.wallName_C).colorsOnTheWall as ArrayList<MyColor> /* = java.util.ArrayList<com.example.routes.dataStuff.MyColor> */
            else -> throw Exception("Database don't contain records relative with this view")
        }
        updateRecyclerView()
    }

    fun saveLastArrayForRandomSequence(){
        var stringBuilder = StringBuilder()

        for (color in AppRuntimeData.colorsListInLocalSettings)
            stringBuilder.append(color.toString() + "/")
        sharedPreferencesEditor.putString("lastArrayForRandomSequence", stringBuilder.toString())
        sharedPreferencesEditor.apply()
    }

    override fun onResume() {
        updateRecyclerView()
        super.onResume()
    }

    override fun onPause() {
        saveLastArrayForRandomSequence()
        super.onPause()
    }

    override fun onDestroyView() {
        saveLastArrayForRandomSequence()
        _binding = null
        super.onDestroyView()
    }
}