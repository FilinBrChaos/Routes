package com.example.routes.routeGenerator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.routes.AppRuntimeData
import com.example.routes.R
import com.example.routes.appFragments.LocalSettingsCardAdapter
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.RouteGeneratorFragmentBinding
import java.util.ArrayList
import kotlin.random.Random

class RouteGeneratorFrag : Fragment() {

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

//        if (AppRuntimeData.generatedColorsInColorsView.length > 1) binding.outputTextview.text =
//            AppRuntimeData.generatedColorsInColorsView

        binding.randomButton.setOnClickListener {
            val myRandomList = AppRuntimeData.colorsListInLocalSettings
            val randomSequence: ArrayList<MyColor> = arrayListOf()
            val random = Random

            for (i in 1..20){
                var tempColor = myRandomList[Random.nextInt(myRandomList.size - 1)].clone() as MyColor
                tempColor.colorName = i.toString() + ") " + tempColor.colorName
                randomSequence.add(tempColor)
            }

            if (AppRuntimeData.colorsListInLocalSettings.isNotEmpty())
                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = RouteGenCardAdapter(randomSequence)

            }
//            AppRuntimeData.generatedColorsInColorsView = textResult.toString()
        }

        binding.saveRouteButton.setOnClickListener { findNavController().navigate(R.id.action_RandomGeneratorFrag_to_RoutesListFrag) }
        binding.localSettingsButton.setOnClickListener { findNavController().navigate(R.id.action_RouteGeneratorFrag_to_localSettingsFrag) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}