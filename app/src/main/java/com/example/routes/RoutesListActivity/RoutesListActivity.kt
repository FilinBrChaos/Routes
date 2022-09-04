package com.example.routes.RoutesListActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.routes.AppRuntimeData
import com.example.routes.dataStuff.DbManager
import com.example.routes.databinding.ActivityRoutesListBinding

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

    fun updateRoutesCardsList(){
        val routes = AppRuntimeData.globalDbManager!!.getAllRoutesRecords()
        // TODO: move adapter and view holder in this folder
        if (routes.isNotEmpty()){
            binding.routesListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = RoutesListCardAdapter(routes)
            }
        }
    }
}