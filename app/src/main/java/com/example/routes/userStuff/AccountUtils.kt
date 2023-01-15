package com.example.routes.userStuff

import com.auth0.android.Auth0
import com.auth0.android.authentication.storage.CredentialsManager

class AccountUtils(
    var userAccount: Auth0,
    var accountManager: CredentialsManager
)