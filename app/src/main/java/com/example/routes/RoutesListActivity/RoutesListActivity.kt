package com.example.routes.RoutesListActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.routes.AppRuntimeData
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.ActivityRoutesListBinding
import com.example.routes.routeViewActivity.RouteActivity

class RoutesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoutesListBinding
    private lateinit var dbManager: DbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbManager = DbManager(this)
        updateRoutesCardsList()
    }

    private fun updateRoutesCardsList(){
        //val routes = dataManager.getAllRoutes()
        //val routes = arrayListOf<RouteDTO>()
        val routes = dbManager.getAllRoutes()
        if (routes.isNotEmpty())
            CardAdapter.drawRouteCards(binding.routesListLinearLayout, routes, ::openRouteActivity)
    }

    private fun openRouteActivity(route: RouteDTO){
        val intent = Intent(this@RoutesListActivity, RouteActivity::class.java)
        intent.putExtra(RouteActivity.ROUTE_ID, route.id)
        startActivity(intent)
    }

    override fun onResume() {
        updateRoutesCardsList()
        super.onResume()
    }
}