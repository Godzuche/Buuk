package com.godzuche.buuk.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.godzuche.buuk.data.BuukRepository
import com.godzuche.buuk.util.Routes
import com.godzuche.buuk.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: BuukRepository
) : ViewModel() {

    /*val _user = MutableStateFlow<User?>(null)
    val user : StateFlow<User?> = _user*/

    // User state
    val user = repository.getUser()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.OnGoogleSignIn -> {
                viewModelScope.launch {
                    event.user?.let{
                        repository.saveUser(event.user)
                        sendUiEvent(UiEvent.Navigate(Routes.EXPLORE))
                    } ?: run{
                        if (event.isAnonymous){
                            sendUiEvent(UiEvent.Navigate(Routes.EXPLORE))
                        }
                    }
                }
            }
            is LoginEvent.OnFacebookSignIn -> {
                viewModelScope.launch {
                    repository.saveUser(event.user)
                }
                sendUiEvent(UiEvent.Navigate(Routes.EXPLORE))
            }
            is LoginEvent.OnEmailSignIn -> {
                sendUiEvent(UiEvent.Navigate(Routes.EMAIL_LOGIN))
            }
            is LoginEvent.OnCreateAccount -> {
                sendUiEvent(UiEvent.Navigate(Routes.CREATE_ACCOUNT))
            }
            is LoginEvent.OnSignUpLater -> {
                sendUiEvent(UiEvent.Navigate(Routes.EXPLORE))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}