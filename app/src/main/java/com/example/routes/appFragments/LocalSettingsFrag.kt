package com.example.routes.appFragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.routes.AppRuntimeData
import com.example.routes.dataStuff.MyColor
import com.example.routes.dataStuff.DbManager
import com.example.routes.databinding.FragmentLocalSettingsBinding

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

        when(sharedSettingsPreferences.getString("selectedWall", binding.wallARadioBtn.id.toString())){
            binding.wallARadioBtn.id.toString() -> binding.wallARadioBtn.isChecked = true
            binding.wallBRadioBtn.id.toString() -> binding.wallBRadioBtn.isChecked = true
            binding.wallCRadioBtn.id.toString() -> binding.wallCRadioBtn.isChecked = true
            else -> {}
        }

        binding.refreshColorsButton.setOnClickListener { refreshColorsAndUpdateRecyclerView() }
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
//            Toast.makeText(activity, binding.radioGroup.checkedRadioButtonId.toString(), Toast.LENGTH_SHORT).show()
            sharedPreferencesEditor.putString("selectedWall", binding.radioGroup.checkedRadioButtonId.toString())
            sharedPreferencesEditor.apply()
            refreshColorsAndUpdateRecyclerView()
            updateRecyclerView()
        }
        refreshColorsAndUpdateRecyclerView()
        updateRecyclerView()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateRecyclerView(){
        if (AppRuntimeData.colorsListInLocalSettings.isNotEmpty())
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = LocalSettingsCardAdapter(AppRuntimeData.colorsListInLocalSettings, updateRecyclerView = { updateRecyclerView() })
            }
    }

    private fun refreshColorsAndUpdateRecyclerView() {
        AppRuntimeData.colorsListInLocalSettings = when(binding.radioGroup.checkedRadioButtonId.toString()){
            binding.wallARadioBtn.id.toString() -> dbManager.getWall(dbManager.wallName_A).colorsOnTheWall as ArrayList<MyColor> /* = java.util.ArrayList<com.example.routes.dataStuff.MyColor> */
            binding.wallBRadioBtn.id.toString() -> dbManager.getWall(dbManager.wallName_B).colorsOnTheWall as ArrayList<MyColor> /* = java.util.ArrayList<com.example.routes.dataStuff.MyColor> */
            binding.wallCRadioBtn.id.toString() -> dbManager.getWall(dbManager.wallName_C).colorsOnTheWall as ArrayList<MyColor> /* = java.util.ArrayList<com.example.routes.dataStuff.MyColor> */
            else -> throw Exception("Database don't contain records relative with this wiew")
        }
        updateRecyclerView()
    }

    override fun onResume() {
        updateRecyclerView()
        super.onResume()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}