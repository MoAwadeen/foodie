package iti.project.foodie.ui.auth

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import iti.project.foodie.R
import iti.project.foodie.databinding.FragmentIntroBinding

class IntroFragment : Fragment() {
    private lateinit var binding: FragmentIntroBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", android.content.Context.MODE_PRIVATE)
        if (sharedPreferences.getString("email", null) != null){
            findNavController().navigate(R.id.action_introFragment_to_homeFragment2)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButtonIntroFragment.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_loginFragment)
        }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_registerFragment)
        }
    }
}
