package com.godzuche.buuk.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.godzuche.buuk.R
import com.godzuche.buuk.databinding.FragmentLoginBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    lateinit var auth: FirebaseAuth

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        auth = Firebase.auth
        binding.apply {
            btCreateAccount.setOnClickListener { gotoSignUpFragment() }
        }
    }

    private fun gotoSignUpFragment() {
        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
        navController.navigate(action)
    }

    override fun onStart() {
        if (auth.currentUser != null) {
            goToHomeFragment()
        }
        super.onStart()
    }

    private fun goToHomeFragment() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToActionLearn())
    }

}