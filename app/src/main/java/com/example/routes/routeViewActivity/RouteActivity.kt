package com.example.routes.routeViewActivity

import android.content.DialogInterface
import com.example.routes.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.routes.appFragments.LocalSettingsCardAdapter
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.ActivityRouteBinding
import com.example.routes.routeGenerator.RouteGenCardAdapter

class RouteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRouteBinding
    private lateinit var dbManager: DbManager
    private lateinit var route: RouteDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DbManager(this)

        val actionBar: ActionBar = supportActionBar!!
        actionBar.setDisplayHomeAsUpEnabled(true)

        route = AppRuntimeData.routeDTOTemporary!!

        if (route.picturesData == "") binding.imagesLayoutBlock.visibility = View.GONE
        title = route.routeName
        binding.creatorTextView.text = route.routeCreator
        binding.dateTextView.text = route.creationDate
        binding.usedWallsTextView.text = route.wallName

        binding.colorsRecyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = RouteGenCardAdapter(route.routeColors)
        }
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