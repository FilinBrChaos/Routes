package com.example.routes.RoutesListActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.routes.AppRuntimeData
import com.example.routes.cardsStuff.CardAdapter
import com.example.routes.dataStuff.DbManager
import com.example.routes.databinding.ActivityRoutesListBinding
import com.example.routes.routeViewActivity.RouteActivity

class RoutesListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoutesListBinding
    private lateinit var dbManager: DbManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutesListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //dbManager = DbManager(this)
        updateRoutesCardsList()
    }

    private fun updateRoutesCardsList(){
        val routes = AppRuntimeData.globalDbManager!!.getAllRoutesRecords()
        if (routes.isNotEmpty())
            CardAdapter.drawRouteCards(binding.routesListLinearLayout, routes, startRouteActivity = {
                val intent = Intent(this@RoutesListActivity, RouteActivity::class.java)
                startActivity(intent)
            })
    }

    override fun onResume() {
        updateRoutesCardsList()
        super.onResume()
    }
}