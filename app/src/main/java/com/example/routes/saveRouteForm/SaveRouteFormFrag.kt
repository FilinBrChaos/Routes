package com.example.routes.saveRouteForm

import com.example.routes.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.res.colorResource
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.example.routes.AppRuntimeData
import com.example.routes.R
import com.example.routes.colorPicker.ColorPickerActivity
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.SaveRouteFragmentBinding

class SaveRouteFormFrag : Fragment() {
    private var _binding: SaveRouteFragmentBinding? = null
    private lateinit var dbManager: DbManager
    private val images = arrayListOf<Uri>()
    private val resultLauncherImageEditor = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data: Uri = result.data!!.data!!
            val intent = Intent(activity, DsPhotoEditorActivity::class.java)
            intent.data = data
            intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Images")
            //intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, R.color.dark_primary)
            //intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, R.color.dark_secondary_variant)
            val toolsToHide = intArrayOf(DsPhotoEditorActivity.TOOL_WARMTH,
                DsPhotoEditorActivity.TOOL_PIXELATE,
                DsPhotoEditorActivity.TOOL_CONTRAST,
                DsPhotoEditorActivity.TOOL_FILTER,
            DsPhotoEditorActivity.TOOL_SHARPNESS,
            DsPhotoEditorActivity.TOOL_VIGNETTE,
            DsPhotoEditorActivity.TOOL_FRAME,
            DsPhotoEditorActivity.TOOL_ROUND)
            intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide)
            resultLauncher.launch(intent)
        }
    }
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val uri: Uri = result.data!!.data!!
            Toast.makeText(activity, "Photo saved", Toast.LENGTH_SHORT).show()
            images.add(uri)
            updateImageRecyclerView()
        }
    }

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SaveRouteFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        dbManager = DbManager(activity)
        return binding.root
    }

    private val binding get() = _binding!!

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_home)?.isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    fun updateImageRecyclerView(){
        binding.imageRecyclerView.apply {
            //layoutManager = LinearLayoutManager(activity)
            adapter = SaveRouteCardAdapter(images, resultLauncher)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedSettingsPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        binding.button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncherImageEditor.launch(intent)
        }

        binding.saveRouteButton.setOnClickListener {
            val currentWall = sharedSettingsPreferences.getString("currentWall", DbManager.TABLE_WALL_A)
            dbManager.addRouteRecord(RouteDTO(1,
                binding.routeNameTextField.text.toString(),
                currentWall!!,
                binding.routeCreatorTextField.text.toString(),
                "01.02.2022",
                AppRuntimeData.colorsListInLocalSettings,
                "pictures data"))
            // TODO: make date and pictures data
            // TODO: close fragment after saving route and show toast with status message
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}