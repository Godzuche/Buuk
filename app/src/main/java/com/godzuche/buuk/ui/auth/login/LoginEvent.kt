package com.godzuche.buuk.ui.auth.login

import com.godzuche.buuk.data.User

sealed class LoginEvent{
    data class OnGoogleSignIn(val user: User?, val isAnonymous: Boolean): LoginEvent()
    data class OnFacebookSignIn(val user: User): LoginEvent()
    data class OnEmailSignIn(val user: User): LoginEvent()
    data class OnCreateAccount(val user: User): LoginEvent()
    object OnSignUpLater: LoginEvent()
}
