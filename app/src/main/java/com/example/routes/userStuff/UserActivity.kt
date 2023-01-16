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
import com.bumptech.glide.Glide
import com.example.routes.AppRuntimeData
import com.example.routes.R
import com.example.routes.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateUi()

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
                    AppRuntimeData.accountUtils?.accountManager?.clearCredentials()
                    updateUi()
                    // The user has been logged out!
                }

                override fun onFailure(error: AuthenticationException) {
                    Log.e("User activity", "Logout failure")
                }
            })
    }

    private fun loginWithBrowser(account: Auth0) {
        val callback : Callback<Credentials, AuthenticationException> = object : Callback<Credentials, AuthenticationException> {
            override fun onFailure(error: AuthenticationException) {
                //when authorization failed
            }

            override fun onSuccess(result: Credentials) {
                AppRuntimeData.accountUtils?.accountManager?.saveCredentials(result)
                updateUi()
            }
        }

        WebAuthProvider
            .login(account)
            .withScheme(getString(R.string.scheme_auth0))
            .withScope("openid profile email")
            .start(this, callback)
    }

    fun updateUi() {
        val accountUtils = AppRuntimeData.accountUtils
        if (accountUtils != null) {
            if (accountUtils.isUserLoggedIn()) {
                accountUtils.actionUsingUserCredentials { credentials ->
                    binding.activityUserUsername.text = credentials.user.name
                    binding.activityUserUserEmail.text = credentials.user.email
                    //Glide.with(this).load(credentials.user.pictureURL).into(binding.activityUserUserIcon)
                    binding.activityUserUserIcon
                }
                binding.activityUserLoginBlock.visibility = View.GONE
                binding.activityUserLogoutBlock.visibility = View.VISIBLE
            } else {
                binding.activityUserUsername.text = getString(R.string.user_name)
                binding.activityUserUserEmail.text = getString(R.string.useremail_gmail_com)

                binding.activityUserLogoutBlock.visibility = View.GONE
                binding.activityUserLoginBlock.visibility = View.VISIBLE
            }

        }
    }

    override fun onResume() {
        updateUi()
        super.onResume()
    }
}
