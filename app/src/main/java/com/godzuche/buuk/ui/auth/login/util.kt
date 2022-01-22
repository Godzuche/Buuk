package com.godzuche.buuk.ui.auth.login

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.godzuche.buuk.data.User
import com.godzuche.buuk.util.Routes
import com.godzuche.buuk.util.UiEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/*fun updateUI(user: FirebaseUser?, navController: NavController) {
    // Check if there is any currently logged in user
    if (user != null) {
        navigateToCoursesScreen(navController)
    }
}*/

fun navigateToExploreScreen(navController: NavController) {
    navController.navigate(LoginFragmentDirections.actionLoginFragmentToExploreFragment())
}

// Using Coroutines
fun firebaseAuthWithGoogle(auth: FirebaseAuth, idToken: String, context: Context, navController: NavController, viewModel: LoginViewModel) {
    val credentials = GoogleAuthProvider.getCredential(idToken, null)
    CoroutineScope(Dispatchers.IO).launch {
        try {
            auth.signInWithCredential(credentials).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Successfully logged in with Firebase", Toast.LENGTH_LONG)
                    .show()
                val firebaseUser = auth.currentUser
                val user: User? = firebaseUser?.let {
                    User(
                        name = it.displayName,
                        email = it.email
                    )
                }

                viewModel.onEvent(LoginEvent.OnGoogleSignIn(user = user, isAnonymous = false))
//                updateUI(firebaseUser, navController)
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                viewModel.onEvent(LoginEvent.OnGoogleSignIn(user = null, isAnonymous = false))
//                updateUI(null, navController)
            }
        }
    }
}

fun onNavigate(event: UiEvent.Navigate, navController: NavController) {
    when (event.route) {
        Routes.EXPLORE-> {
            navigateToExploreScreen(navController)
        }
    }
}