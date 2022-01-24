package com.godzuche.buuk.ui.auth.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.facebook.*
import com.facebook.gamingservices.BuildConfig
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.godzuche.buuk.R
import com.godzuche.buuk.data.User
import com.godzuche.buuk.databinding.FragmentLoginBinding
import com.godzuche.buuk.util.UiEvent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*

//const val REQUEST_CODE_SIGN_IN = 0

@InternalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var fbEmail: String? = null

    //    private val isUserLoggedIn: Boolean = false
    private lateinit var binding: FragmentLoginBinding
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var googleSingInClient: GoogleSignInClient
    private val viewModel: LoginViewModel by viewModels()
    val callbackManager = CallbackManager.Factory.create()
    val facebookLoginManager = LoginManager.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onStart() {
        // Get current user
        val firebaseUser = auth.currentUser
        val user: User? = firebaseUser?.let {
            User(
                name = it.displayName,
                email = it.email
            )
        }

        viewModel.onEvent(LoginEvent.OnGoogleSignIn(user = user, isAnonymous = false))
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect { event ->
                    when (event) {
                        is UiEvent.Navigate -> onNavigate(event, findNavController())
                        else -> Unit
                    }
                }
            }
        }

        // Google SignIn Options
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        // Create Google SignIn Client
        googleSingInClient = GoogleSignIn.getClient(requireContext(), gso)

        binding.apply {
            btAnonymousSignIn.setOnClickListener {
                auth.signInAnonymously()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // navigate to learn
                            viewModel.onEvent(LoginEvent.OnGoogleSignIn(user = null, isAnonymous = true))
                        }
                        else {
                            Log.e("TAG", "Anonymous sign-in failed", task.exception)
                            Toast.makeText(requireContext(),
                                "Anonymous Sign-in failed",
                                Toast.LENGTH_LONG).show()
                        }
                    }
            }
            btGoogleSignIn.setOnClickListener {
                signIn()
            }

            btFbSignIn.setOnClickListener {
                facebookLoginManager.logInWithReadPermissions(this@LoginFragment,
                    callbackManager,
                    listOf("email", "public_profile"))
                facebookLoginManager.registerCallback(callbackManager, object :
                    FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        Log.d("TAG", "facebook:onSuccess:$loginResult")
                        val request: GraphRequest = GraphRequest.newMeRequest(loginResult.accessToken
                        ) { obj, response ->
                            Log.v("TAG", response.toString())
                            // Get user details
                            fbEmail = obj?.getString("email")
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "email")
                        request.parameters = parameters
                        request.executeAsync()

                        handleFacebookAccessToken(loginResult.accessToken)
                    }

                    override fun onCancel() {
                        Log.d("TAG", "facebook:onCancel")
                    }

                    override fun onError(error: FacebookException) {
                        Log.d("TAG", "facebook:onError", error)
                    }
                })
            }

            btLoginWithEmail.setOnClickListener { navigateToEmailLoginScreen() }

            btCreateAccount.setOnClickListener { navigateToSignUpScreen() }
        }

    }

    private fun navigateToEmailLoginScreen() {
        val action = LoginFragmentDirections.actionLoginFragmentToEmailLoginFragment()
        findNavController().navigate(action)
    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }*/

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credential).await()
                withContext(Dispatchers.Main) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
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
            } /*catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", e)
                    Toast.makeText(requireActivity(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    viewModel.onEvent(LoginEvent.OnGoogleSignIn(user = null))
//                updateUI(null, navController)
                }
            } */catch (e: FirebaseAuthUserCollisionException) {
                withContext(Dispatchers.Main){
                    Log.w("TAG", "signInWithCredential:failure", e)
                    Toast.makeText(requireActivity(),
                        "The email, $fbEmail associated with this Facebook account is already in use by another account.",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /*auth.signInWithCredential(credential)
        .addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d("TAG", "signInWithCredential:success")
                val firebaseUser = auth.currentUser
//                    updateUI(user)
                val user: User? = firebaseUser?.let {
                    User(
                        name = it.displayName,
                        email = it.email
                    )
                }

                viewModel.onEvent(LoginEvent.OnGoogleSignIn(user = user))
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "signInWithCredential:failure", task.exception)
                Toast.makeText(requireActivity(), "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                viewModel.onEvent(LoginEvent.OnGoogleSignIn(user = null))
            }
        }*/

private fun navigateToSignUpScreen() {
    val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
    findNavController().navigate(action)
}

private fun signIn() {
    googleSingInClient.signInIntent.also {
//            startActivityForResult(it, REQUEST_CODE_SIGN_IN)
        signInLauncher.launch(it)
    }
}

private val signInLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        this.onSignInResult(result)
    }

private fun onSignInResult(result: ActivityResult) {


    if (result.resultCode == Activity.RESULT_OK) {
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            callbackManager.onActivityResult(Activity.RESULT_OK, result.resultCode, result.data)

        try {
            // Google Sign In was successful, authenticate with Firebase
            val account = task.getResult(ApiException::class.java)!!
            Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(auth,
                account.idToken!!,
                requireContext(),
                findNavController(),
                viewModel)
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w("TAG", "Google sign in failed", e)
        }
    }
}

}