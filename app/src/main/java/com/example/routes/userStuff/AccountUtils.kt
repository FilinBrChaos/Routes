package com.example.routes.userStuff

import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.routes.AppRuntimeData

class AccountUtils(
    var userAccount: Auth0,
    var accountManager: CredentialsManager
) {
//    fun actionUsingUserProfile(action: (user: UserProfile) -> Unit) {
//        val client = AuthenticationAPIClient(userAccount)
//
//        val callback: Callback<UserProfile, AuthenticationException> = object :
//            Callback<UserProfile, AuthenticationException> {
//            override fun onFailure(error: AuthenticationException) {
//                Log.e("User activity", "fail to get the user profile")
//            }
//
//            override fun onSuccess(result: UserProfile) {
//                action(result)
//            }
//        }
//
//        client.userInfo(accessToken).start(callback)
//    }

    fun actionUsingUserCredentials(action: (credentials: Credentials) -> Unit) {
        val callback : Callback<Credentials, CredentialsManagerException> = object : Callback<Credentials, CredentialsManagerException> {
            override fun onFailure(error: CredentialsManagerException) {
                Log.e("AccountUtils.actionUsingUserCredentials", "Failed to get credentials")
            }

            override fun onSuccess(result: Credentials) {
                action(result)
            }
        }
        accountManager.getCredentials(callback)
    }

    fun actionIfUserLoggedIn(action: () -> Unit) {
        if (isUserLoggedIn()) {
            action()
        } else {
            Log.e("AccountUtils.actionIfUserLoggedIn", "User isn't logged in")
        }
    }

    fun isUserLoggedIn(): Boolean {
        return accountManager.hasValidCredentials()
    }
}