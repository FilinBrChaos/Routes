package com.example.routes.RoutesListActivity

import androidx.recyclerview.widget.RecyclerView
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.RouteCardBinding

class RoutesListCardViewHolder(private val routeCardBinding: RouteCardBinding): RecyclerView.ViewHolder(routeCardBinding.root) {
    fun bindCard(route: RouteDTO){
        routeCardBinding.routeName.text = route.routeName
        routeCardBinding.routeCreator.text = route.routeCreator
    }
}