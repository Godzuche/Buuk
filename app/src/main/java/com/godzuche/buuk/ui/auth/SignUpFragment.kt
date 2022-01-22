package com.godzuche.buuk.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.godzuche.buuk.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = Firebase.auth
        binding.apply {
            btCreateNewAccount.setOnClickListener {
                registerUser()
//                updateProfileUserName()
            }
        }
    }

    override fun onStart() {
        // Check if user is signed in (non-null) and update UI accordingly.
        if (auth.currentUser != null) {
            goToHomeFragment()
        }
        super.onStart()
    }

    private fun goToHomeFragment() {
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToExploreFragment())
    }

    /*private fun updateProfileUserName() {
        val username = binding.etName.text.toString()
        auth.currentUser?.let { user ->
//            val photoUri =
            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(username)
//                .setPhotoUri()
                .build()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    user.updateProfile(profileUpdate).await()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(),
                            "Successfully  updated user profile",
                            Toast.LENGTH_LONG).show()
                        goToHomeFragment()

                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }*/

    private fun registerUser() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val username = binding.etName.text.toString()
        val profileUpdate = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
//                .setPhotoUri()
            .build()
        if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    Firebase.auth.currentUser?.updateProfile(profileUpdate)?.await()

                        withContext(Dispatchers.Main) {
                        checkLoggedInState()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun checkLoggedInState() {
        if (auth.currentUser == null) {
            Toast.makeText(requireContext(), "You are not logged in", Toast.LENGTH_LONG).show()
        } else if (auth.currentUser != null && auth.currentUser!!.displayName != null){
            Toast.makeText(requireContext(), "You are now logged in", Toast.LENGTH_LONG).show()
            Toast.makeText(requireContext(),
                "Successfully  updated user profile",
                Toast.LENGTH_LONG).show()
            goToHomeFragment()
        }
    }

}