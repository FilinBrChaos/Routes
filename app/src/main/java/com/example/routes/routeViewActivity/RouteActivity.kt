package com.example.routes.routeViewActivity

import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Build
import com.example.routes.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    companion object{
        const val ROUTE_ID = "route_index"
    }
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

        val routeIndex = intent.getIntExtra(ROUTE_ID, 0)

        route = dbManager.getRoute(routeIndex)

        if (route.picturesData.isEmpty()) binding.imagesLayoutBlock.visibility = View.GONE
        else {
            val images = arrayListOf<Bitmap>()
            for (imageName in route.picturesData){
                images.add(imageManager.loadImage(imageName)!!)
            }
            if (images.isNotEmpty()) CardAdapter.drawImageCards(binding.routeImagesLinearLayout, images)
        }

        if (route.routeColors.isEmpty()) binding.routeColorLinearLayout.visibility = View.GONE
        else {
            CardAdapter.drawColorCards(binding.routeColorLinearLayout, route.routeColors)
        }

        title = route.routeName
        binding.creatorTextView.text = route.routeCreator
        binding.dateTextView.text = route.creationDate
        binding.usedWallsTextView.text = route.wallName
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
                    if (dbManager.deleteRoute()) //todo this
                        Toast.makeText(this, "Deleted successfully", Toast.LENGTH_LONG).show()
                    else Toast.makeText(this, "Error, something went wrong while deleting this route", Toast.LENGTH_LONG).show()
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