package com.example.routes.cardsStuff

abstract class IMyCardViewHolder<B>(val binding: B) {
    abstract fun <T>bindCard(t: T)
}