package com.example.routes.net

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.dataStuff.SendJSONToApi
import com.example.routes.databinding.ActivityRouteBinding
import com.example.routes.databinding.ActivityShareRouteBinding
import com.example.routes.routeViewActivity.RouteActivity
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class ShareRouteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShareRouteBinding
    private lateinit var dbManager: DbManager
    private lateinit var route: RouteDTO

    companion object{
        const val ROUTE_ID = "route_index"
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DbManager(this)
        val routeIndex = intent.getIntExtra(RouteActivity.ROUTE_ID, 0)
        route = dbManager.getRoute(routeIndex)

        title = route.routeName

        binding.shareRouteButton.setOnClickListener {
            val obj = JSONObject()

            Log.e("TAG", "it works")

            try {
                obj.put("id", "unique")
                obj.put("name", "data from phone")
                obj.put("status", "hooray")

                if (SendJSONToApi().checkNetworkConnection(this)){
                    lifecycleScope.launch {
                        val result = SendJSONToApi().httpPost("https://kw0u7qqd7l.execute-api.eu-central-1.amazonaws.com/insert_route", obj)
                        binding.textView4.text = result
                    }
                }

            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}