package com.example.routes.saveRouteForm

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.routes.AppRuntimeData
import com.example.routes.DrawerLocker
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.SaveRouteFragmentBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SaveRouteFormFragment : Fragment() {
    private var _binding: SaveRouteFragmentBinding? = null
    private lateinit var dbManager: DbManager
    private val images = arrayListOf<Bitmap>()

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SaveRouteFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        dbManager = DbManager(requireActivity())
        return binding.root
    }

    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedSettingsPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        binding.saveRouteButton.setOnClickListener {
            val currentWall = sharedSettingsPreferences.getString("currentWall", DbManager.WALLS_NAMES[0])
            val currentDate = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

            dbManager.saveRoute(RouteDTO(binding.routeNameTextField.text.toString(),
            currentWall!!, currentDate.format(formatter), binding.routeCreatorTextField.text.toString(),
            AppRuntimeData.currentGeneratedRouteColors))

            Toast.makeText(activity, "Route saved", Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        }
        super.onViewCreated(view, savedInstanceState)
        (activity as DrawerLocker).setDrawerEnabled(false)
    }

//    @Suppress("DEPRECATION")
//    @Deprecated("Deprecated in Java")
//    override fun onPrepareOptionsMenu(menu: Menu) {
//        menu.findItem(R.id.action_home)?.isVisible = false
//        super.onPrepareOptionsMenu(menu)
//    }

    override fun onDestroyView() {
        (activity as DrawerLocker).setDrawerEnabled(false)
        _binding = null
        super.onDestroyView()
    }
}