package com.example.routes

import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.result.UserProfile
import com.example.routes.dataStuff.MyColor
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

object AppRuntimeData {
    init { /*this is object initialization DON'T DELETE THIS*/ }
    var currentGeneratedRouteColors: ArrayList<MyColor> = arrayListOf()
    var user: UserProfile? = null
}