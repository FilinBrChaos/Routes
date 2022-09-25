package com.example.routes.cardsStuff

import com.example.routes.AppRuntimeData
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.RouteCardBinding

class RouteCardViewHolder(val binding: RouteCardBinding) {
    fun bindCard(startRouteActivity: () -> Unit, route: RouteDTO){
        binding.routeName.text = route.routeName
        binding.routeCreator.text = route.routeCreator
        binding.creationDateTextView.text = route.creationDate
        binding.wholeCard.setOnClickListener {
            AppRuntimeData.routeDTOTemporary = route
            startRouteActivity()
        }
    }
}