package com.example.routes.net

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.routes.AppRuntimeData
import com.example.routes.R
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.ImageManager
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.dataStuff.SendJSONToApi
import com.example.routes.databinding.ActivityRouteBinding
import com.example.routes.databinding.ActivityShareRouteBinding
import com.example.routes.routeViewActivity.RouteActivity
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.time.Duration

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
        loadingEnd()

        dbManager = DbManager(this)
        val routeIndex = intent.getIntExtra(RouteActivity.ROUTE_ID, 0)
        route = dbManager.getRoute(routeIndex)

        title = route.routeName

        //!!!!!!!!!!!!!!!
        //check if user is already posted that route before accessing him the share button
        //!!!!!!!!!!!!!!!


        binding.shareRouteButton.setOnClickListener {
            val accountUtils = AppRuntimeData.accountUtils
            if (accountUtils != null && accountUtils.isUserLoggedIn()){
                val obj = JSONObject()

                try {
                    obj.put("id", "unique")
                    obj.put("userId", "something")
                    obj.put("routeName", route.routeName)
                    obj.put("routeCreator", route.routeCreator)
                    obj.put("creationDate", route.creationDate)

                    loadingStart()
                    if (SendJSONToApi().checkNetworkConnection(this)){
                        lifecycleScope.launch {
                            val result = SendJSONToApi().httpPost("https://kw0u7qqd7l.execute-api.eu-central-1.amazonaws.com/insert_route", obj)
                            loadingEnd()
                            if (result == "OK") setResultStatusOk()
                            else setResultStatusError()
                        }
                    }

                } catch (e: Exception) { e.printStackTrace() }

            } else {
                Toast.makeText(this, "Some problems with user account, try relogin or try later", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun loadingStart() {
        binding.activityShareRouteProgressBar.visibility = View.VISIBLE
    }

    fun loadingEnd() {
        binding.activityShareRouteProgressBar.visibility = View.GONE
    }

    fun setResultStatusOk() {
        binding.activityShareRouteProgressBar.visibility = View.GONE
        binding.activityShareRouteStatusIcon.visibility = View.VISIBLE
        ImageManager.drawInImageView(applicationContext, R.drawable.ic_status_ok, binding.activityShareRouteStatusIcon)
    }

    fun setResultStatusError() {
        binding.activityShareRouteProgressBar.visibility = View.GONE
        binding.activityShareRouteStatusIcon.visibility = View.VISIBLE
        ImageManager.drawInImageView(applicationContext, R.drawable.ic_status_error, binding.activityShareRouteStatusIcon)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}