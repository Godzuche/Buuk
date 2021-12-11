package com.godzuche.buuk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.godzuche.buuk.databinding.ActivityIntroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        val view = binding.root
        installSplashScreen()
        setContentView(view)

        auth = Firebase.auth
    }

    private fun registerUser() {
      /*  val email = etEmailRegister.text.toString()
        val password = etPasswordRegister.text.toString()*/

    }
}