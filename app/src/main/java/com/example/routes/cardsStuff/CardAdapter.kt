package com.example.routes.cardsStuff

import android.app.Activity
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.routes.dataStuff.DbManager
import com.example.routes.dataStuff.MyColor
import com.example.routes.dataStuff.RouteDTO
import com.example.routes.databinding.*

class CardAdapter {
    companion object {
        private fun createCheckableColorCardViewHolder(parent: ViewGroup): CheckableColorCardViewHolder {
            val from = LayoutInflater.from(parent.context)
            val binding = CheckableColorCardBinding.inflate(from, parent, false)
            return CheckableColorCardViewHolder(binding)
        }

        fun drawCheckableColorCards(parentElement: LinearLayout, colors: ArrayList<MyColor>) {
            parentElement.removeAllViews()
            val cards: ArrayList<CheckableColorCardViewHolder> = arrayListOf()
            for (i in 0 until colors.size) {
                cards.add(createCheckableColorCardViewHolder(parentElement))
                cards[i].bindCard(colors[i])
                parentElement.addView(cards[i].binding.root)
            }
        }

        private fun createImageCardViewHolder(parent: ViewGroup): ImageCardViewHolder {
            val from = LayoutInflater.from(parent.context)
            val binding = ImageCardBinding.inflate(from, parent, false)
            return ImageCardViewHolder(binding)
        }

        fun drawImageCards(parentElement: LinearLayout, images: ArrayList<Bitmap>) {
            parentElement.removeAllViews()
            val cards: ArrayList<ImageCardViewHolder> = arrayListOf()
            for (i in 0 until images.size) {
                cards.add(createImageCardViewHolder(parentElement))
                cards[i].bindCard(images[i])
                parentElement.addView(cards[i].binding.root)
            }
        }

        private fun createEditableDeletableColorCardViewHolder(parent: ViewGroup): EditableDeletableCardViewHolder {
            val from = LayoutInflater.from(parent.context)
            val binding = EditDeleteColorCardBinding.inflate(from, parent, false)
            return EditableDeletableCardViewHolder(binding)
        }

        fun drawEditableDeletableColorCards(
            parentElement: LinearLayout,
            callerActivity: Activity,
            colors: ArrayList<MyColor>,
            dbManager: DbManager
        ) {
            parentElement.removeAllViews()
            val cards: ArrayList<EditableDeletableCardViewHolder> = arrayListOf()
            for (i in 0 until colors.size) {
                cards.add(createEditableDeletableColorCardViewHolder(parentElement))
                cards[i].bindCard(callerActivity, colors[i], dbManager)
                parentElement.addView(cards[i].binding.root)
            }
        }

        private fun createRouteCardViewHolder(parent: ViewGroup): RouteCardViewHolder {
            val from = LayoutInflater.from(parent.context)
            val binding = RouteCardBinding.inflate(from, parent, false)
            return RouteCardViewHolder(binding)
        }

        fun drawRouteCards(
            parentElement: LinearLayout,
            routes: ArrayList<RouteDTO>,
            startRouteActivity: () -> Unit
        ) {
            parentElement.removeAllViews()
            val cards: ArrayList<RouteCardViewHolder> = arrayListOf()
            for (i in 0 until routes.size) {
                cards.add(createRouteCardViewHolder(parentElement))
                cards[i].bindCard(startRouteActivity, routes[i])
                parentElement.addView(cards[i].binding.root)
            }
        }

        private fun createColorCardViewHolder(parent: ViewGroup): ColorCardViewHolder{
            val from = LayoutInflater.from(parent.context)
            val binding = ColorCardBinding.inflate(from, parent, false)
            return ColorCardViewHolder(binding)
        }

        fun drawColorCards(parentElement: LinearLayout, colors: ArrayList<MyColor>){
            parentElement.removeAllViews()
            val cards: ArrayList<ColorCardViewHolder> = arrayListOf()
            for (i in 0 until colors.size) {
                cards.add(createColorCardViewHolder(parentElement))
                cards[i].bindCard(colors[i])
                parentElement.addView(cards[i].binding.root)
            }
        }
    }
}