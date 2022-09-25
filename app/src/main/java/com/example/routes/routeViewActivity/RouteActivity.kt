package com.example.routes.routeViewActivity

import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Build
import com.example.routes.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.ImageManager
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.ActivityRouteBinding

class RouteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRouteBinding
    private lateinit var dbManager: DbManager
    private lateinit var route: RouteDTO

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DbManager(this)
        val imageManager = ImageManager(this)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)

        route = AppRuntimeData.routeDTOTemporary!!

        if (route.picturesData == "") binding.imagesLayoutBlock.visibility = View.GONE
        else {
            val images = arrayListOf<Bitmap>()
            val imgNames = route.picturesData.split(", ")
            for (imageName in imgNames){
                images.add(imageManager.getSavedImage(imageName))
            }
            //TODO make parsing in routeDTO or something
            CardAdapter.drawImageCards(binding.routeImagesLinearLayout, images)
        }

        title = route.routeName
        binding.creatorTextView.text = route.routeCreator
        binding.dateTextView.text = route.creationDate
        binding.usedWallsTextView.text = route.wallName

        CardAdapter.drawColorCards(binding.routeColorLinearLayout, route.routeColors)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_for_route_activity, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_edit ->{
                //TODO
                true
            }
            R.id.action_delete ->{
                var dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("Confirm delete")
                dialogBuilder.setMessage("Are you sure to delete this route record?")
                dialogBuilder.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, _ ->
                    dbManager.removeRouteRecord(route.id)
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_LONG).show()
                    this.finish()
                    dialog.cancel()
                })
                dialogBuilder.setNegativeButton("No", DialogInterface.OnClickListener{ dialog, _ ->
                    dialog.cancel()
                })
                var alert = dialogBuilder.create()
                alert.show()


                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}