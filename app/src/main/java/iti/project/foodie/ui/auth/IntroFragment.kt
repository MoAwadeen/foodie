package iti.project.foodie.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import iti.project.foodie.R

class IntroFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loginButton=view.findViewById<MaterialButton>(R.id.loginButtonIntroFragment)
        val signUpButton=view.findViewById<MaterialButton>(R.id.signUpButton)
        loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_loginFragment)
        }
        signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_introFragment_to_registerFragment)

        }

    }

}