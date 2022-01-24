package com.godzuche.buuk.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.godzuche.buuk.R
import com.godzuche.buuk.databinding.FragmentCoursesBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class CoursesFragment : Fragment() {
    private lateinit var binding: FragmentCoursesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable options menu
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_top_app_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_settings -> {
                //
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}