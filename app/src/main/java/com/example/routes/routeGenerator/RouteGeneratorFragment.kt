package com.example.routes.routeGenerator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.routes.AppRuntimeData
import com.example.routes.R
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.RouteGeneratorFragmentBinding
import kotlin.collections.ArrayList
import kotlin.random.Random

class RouteGeneratorFragment : Fragment() {

    private var _binding: RouteGeneratorFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = RouteGeneratorFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateColorsListRecyclerView(AppRuntimeData.currentGeneratedRouteColors)
        binding.randomButton.setOnClickListener {
            val myRandomList = getAllCheckedColors(AppRuntimeData.colorsListInLocalSettings)
            if (myRandomList.size <= 1) {
                Toast.makeText(activity, "There is not enough colors to create random sequence", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            val randomSequence: ArrayList<MyColor> = arrayListOf()

            for (i in 1..20){
                var tempColor = myRandomList[Random.nextInt(myRandomList.size)].clone() as MyColor
                tempColor.colorName = i.toString() + ") " + tempColor.colorName
                randomSequence.add(tempColor)
            }

            updateColorsListRecyclerView(randomSequence)
            AppRuntimeData.currentGeneratedRouteColors = randomSequence
        }

        binding.saveRouteButton.setOnClickListener { findNavController().navigate(R.id.action_RouteGeneratorFrag_to_SaveRouteFrag) }
//        binding.saveRouteButton.setOnClickListener { DbManager(activity).deleteFrom(DbManager.TABLE_ROUTES) }
        binding.localSettingsButton.setOnClickListener { findNavController().navigate(R.id.action_RouteGeneratorFrag_to_localSettingsFrag) }
    }

    private fun getAllCheckedColors(colors: ArrayList<MyColor>): ArrayList<MyColor>{
        val result: ArrayList<MyColor> = arrayListOf()
        for (color in colors) if (color.isCheckedInLocalSettings) result.add(color)
        return result
    }

    private fun updateColorsListRecyclerView(randomSequence: ArrayList<MyColor>) {
//        if (randomSequence.isNotEmpty())
        CardAdapter.drawColorCards(binding.routeGeneratorLinearLayout, randomSequence)
    }

    override fun onResume() {
        updateColorsListRecyclerView(AppRuntimeData.currentGeneratedRouteColors)
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}