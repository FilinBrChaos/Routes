package com.example.routes.globalSettings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.routes.AppRuntimeData
import com.example.routes.R
import com.example.routes.databinding.ActivityUserBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserActivity : AppCompatActivity() {
    companion object {
        var account: Auth0? = null
        var accessToken: String? = null

        fun getUserProfile(updateUI: (user: UserProfile) -> Unit, account: Auth0, accessToken: String) {
            val client = AuthenticationAPIClient(account)

            val callback: Callback<UserProfile, AuthenticationException> = object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    Log.e("User activity", "fail to get the user profile")
                }

                override fun onSuccess(result: UserProfile) {
                    AppRuntimeData.user = result
                    updateUI(result)
                }
            }

            client.userInfo(accessToken).start(callback)
        }
    }

    private lateinit var binding: ActivityUserBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        account = Auth0(getString(R.string.client_id_auth0), getString(R.string.domain_auth0))

        binding.loginButton.setOnClickListener {
            loginWithBrowser()
        }
    }

    private fun loginWithBrowser() {
        val callback : Callback<Credentials, AuthenticationException> = object : Callback<Credentials, AuthenticationException> {
            override fun onFailure(error: AuthenticationException) {
                //when authorization failed
            }

            override fun onSuccess(result: Credentials) {
                AppRuntimeData.accessTokenAuth0 = result.accessToken
                 result.refreshToken
            }
        }

        if (account != null) {
            WebAuthProvider
                .login(account!!)
                .withScheme("demo")
                .withScope("openid profile email")
                .start(this, callback)
        } else {
            Log.e("User activity", "fail to login, account object is null")
        }
    }
}
