package com.example.routes.appFragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.routes.colorPicker.ColorPickerActivity
import com.example.routes.databinding.RoutesListFragmentBinding

class RoutesListFrag : Fragment() {

    private var _binding: RoutesListFragmentBinding? = null
    companion object{
        private const val REQUEST_RESULT = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RoutesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        var resultLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()){ result ->
//            if (result.resultCode == Activity.RESULT_OK){
//                val data: Intent? = result.data
//                if (data != null)
//                    data.apply {
//                        val colorName = getStringExtra(ColorPickerActivity.RESULT_COLOR_NAME)
//                        val colorValue = getStringExtra(ColorPickerActivity.RESULT_COLOR_VALUE)
//                        binding.colorSquare.setBackgroundColor(android.graphics.Color.parseColor(colorName))
//                        //binding.colorSquare.background = ColorDrawable(color!!.toColorInt())
//                    }
//                else {}
//            }
//        }
//        binding.colorPickerBtn.setOnClickListener {
//            val intent = Intent(activity, ColorPickerActivity::class.java)
//            resultLauncher.launch(intent)
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}