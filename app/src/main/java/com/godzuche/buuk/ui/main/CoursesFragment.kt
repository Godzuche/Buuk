package com.godzuche.buuk.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.godzuche.buuk.R
import com.godzuche.buuk.databinding.FragmentCoursesBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class CoursesFragment : Fragment() {
    private lateinit var binding: FragmentCoursesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val t = binding.t
        t.text = Firebase.auth.currentUser?.displayName
    }

}