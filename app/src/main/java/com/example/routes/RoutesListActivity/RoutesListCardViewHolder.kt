package com.example.routes.RoutesListActivity

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.AppRuntimeData
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.RouteCardBinding
import com.example.routes.routeViewActivity.RouteActivity

class RoutesListCardViewHolder(private val routeCardBinding: RouteCardBinding): RecyclerView.ViewHolder(routeCardBinding.root) {
    fun bindCard(startRouteActivity: () -> Unit, route: RouteDTO){
        routeCardBinding.routeName.text = route.routeName
        routeCardBinding.routeCreator.text = route.routeCreator
        routeCardBinding.creationDateTextView.text = route.creationDate
        routeCardBinding.wholeCard.setOnClickListener {
            AppRuntimeData.routeDTOTemporary = route
            startRouteActivity()
        }
    }
}