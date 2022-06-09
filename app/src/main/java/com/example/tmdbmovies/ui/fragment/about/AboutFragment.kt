package com.example.tmdbmovies.ui.fragment.about

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tmdbmovies.R
import com.example.tmdbmovies.databinding.FragmentAboutBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AboutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutFragment : Fragment(R.layout.fragment_about) {
    //View Binding
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        val view = binding.root

        animationView()
        return view
    }

    private fun animationView() {
        binding.imageViewAbout.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.avatar_about_animation)
        binding.textAbout.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_about_animation)
        binding.text2About.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_about_animation)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

//    override fun onStop() {
//        super.onStop()
//        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
//    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}