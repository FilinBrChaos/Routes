package com.example.routes.saveRouteForm

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.routes.*
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.ImageManager
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.SaveRouteFragmentBinding
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SaveRouteFormFragment : Fragment() {
    private var _binding: SaveRouteFragmentBinding? = null
    private lateinit var dbManager: DbManager
    private lateinit var imageManager: ImageManager
    private val images = arrayListOf<Bitmap>()
    private val resultLauncherImageEditor = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val uri: Uri = result.data!!.data!!

            Toast.makeText(activity, "Photo added", Toast.LENGTH_SHORT).show()

            val bitmap = imageManager.convertUriToBitmap(uri)

            images.add(bitmap)

            updateImageRecyclerView()

//            val data: Uri = result.data!!.data!!
//            val intent = Intent(activity, DsPhotoEditorActivity::class.java)
//            intent.data = data
            //intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "Images")
            //intent.putExtra(DsPhotoEditorConstants.DS_TOOL_BAR_BACKGROUND_COLOR, R.color.dark_primary)
            //intent.putExtra(DsPhotoEditorConstants.DS_MAIN_BACKGROUND_COLOR, R.color.dark_secondary_variant)
//            val toolsToHide = intArrayOf(DsPhotoEditorActivity.TOOL_WARMTH,
//                DsPhotoEditorActivity.TOOL_PIXELATE,
//                DsPhotoEditorActivity.TOOL_CONTRAST,
//                DsPhotoEditorActivity.TOOL_FILTER,
//                DsPhotoEditorActivity.TOOL_SHARPNESS,
//                DsPhotoEditorActivity.TOOL_VIGNETTE,
//                DsPhotoEditorActivity.TOOL_FRAME,
//                DsPhotoEditorActivity.TOOL_ROUND)
//            intent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide)
//            resultLauncherGetImageAfterEditor.launch(intent)
        }
    }
/*
    private val resultLauncherGetImageAfterEditor = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val uri: Uri = result.data!!.data!!
            println(uri.toString() + "Normal uri!!!!!!!!!!!!!!!!")
            //TODO remove this
            Toast.makeText(activity, "Photo added", Toast.LENGTH_SHORT).show()

            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, uri))
            } else {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            }
            images.add(uri)

            updateImageRecyclerView()
        }
    }
*/

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SaveRouteFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        dbManager = DbManager(activity)
        imageManager = ImageManager(requireActivity())
        return binding.root
    }

    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedSettingsPreferences = requireActivity().getSharedPreferences("settings", Context.MODE_PRIVATE)

        binding.saveRouteAddImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncherImageEditor.launch(intent)
        }

        binding.saveRouteButton.setOnClickListener {


            var imagesNames: ArrayList<String> = arrayListOf()
            for (image in images) {
                var name = imageManager.createUniqueName()
                imagesNames.add(name)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    imageManager.createDirectoryAndSaveImage(image, name)
                }
            }

            val currentWall = sharedSettingsPreferences.getString("currentWall", DbManager.TABLE_WALL_A)
            val currentDate = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            dbManager.addRouteRecord(RouteDTO(1,
                binding.routeNameTextField.text.toString(),
                currentWall!!,
                currentDate.format(formatter),
                binding.routeCreatorTextField.text.toString(),
                AppRuntimeData.currentGeneratedRouteColors,
                imagesNames.joinToString(separator = ", ")))

            Toast.makeText(activity, "Route saved", Toast.LENGTH_SHORT).show()
            activity?.onBackPressed()
        }
        super.onViewCreated(view, savedInstanceState)
    }

//    @Suppress("DEPRECATION")
//    @Deprecated("Deprecated in Java")
//    override fun onPrepareOptionsMenu(menu: Menu) {
//        menu.findItem(R.id.action_home)?.isVisible = false
//        super.onPrepareOptionsMenu(menu)
//    }

    fun updateImageRecyclerView(){
        CardAdapter.drawImageCards(binding.saveRouteImagesLinearLayout, images)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}