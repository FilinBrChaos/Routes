package com.example.routes.userStuff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

class UserActivity : AppCompatActivity() {
    companion object {
        fun actionUsingUserAccount(updateUI: (user: UserProfile) -> Unit, account: Auth0, accessToken: String) {
            val client = AuthenticationAPIClient(account)

            val callback: Callback<UserProfile, AuthenticationException> = object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(error: AuthenticationException) {
                    Log.e("User activity", "fail to get the user profile")
                }

                override fun onSuccess(result: UserProfile) {
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
        binding.activityUserLoggedInBlock.visibility = View.GONE

        val account = Auth0(getString(R.string.client_id_auth0), getString(R.string.domain_auth0))

        binding.activityUserLoginBtn.setOnClickListener {
            loginWithBrowser(account)
        }

        binding.activityUserLogOutBtn.setOnClickListener {
            logout(account)
        }
    }

    private fun logout(account: Auth0){
        WebAuthProvider.logout(account)
            .withScheme(getString(R.string.scheme_auth0))
            .start(this, object: Callback<Void?, AuthenticationException> {
                override fun onSuccess(payload: Void?) {
                    // The user has been logged out!
                }

                override fun onFailure(error: AuthenticationException) {
                    // Something went wrong!
                }
            })
    }

    private fun loginWithBrowser(account: Auth0) {
        val callback : Callback<Credentials, AuthenticationException> = object : Callback<Credentials, AuthenticationException> {
            override fun onFailure(error: AuthenticationException) {
                //when authorization failed
            }

            override fun onSuccess(result: Credentials) {
                //when authorization success
            }
        }

        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.scheme_auth0))
            .withScope("openid profile email")
            .start(this, callback)
    }
}
