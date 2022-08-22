package com.example.routes.routeGenerator

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.routes.dataStuff.MyColor
import com.example.routes.databinding.CardForRandomRouteBinding

class RouteGenCardViewHolder(private val cardForRandomRouteBinding: CardForRandomRouteBinding): RecyclerView.ViewHolder(cardForRandomRouteBinding.root) {
    fun bindCard(color: MyColor){
        cardForRandomRouteBinding.cardColor.setBackgroundColor(Color.parseColor(color.colorValue))
        cardForRandomRouteBinding.colorName.text = color.colorName
    }
}