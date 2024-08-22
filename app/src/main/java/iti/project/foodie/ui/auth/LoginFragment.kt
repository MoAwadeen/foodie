package iti.project.foodie.ui.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import iti.project.foodie.R
import iti.project.foodie.data.repository.AuthRepository
import iti.project.foodie.data.source.local.RoomDb
import iti.project.foodie.databinding.FragmentLoginBinding
import iti.project.foodie.ui.viewModel.AuthViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: AuthViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val database = RoomDb.getDataBase(requireContext())
        val userDao = database.UserDao()
        val repository = AuthRepository(userDao)
        userViewModel = AuthViewModel(repository)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButtonLoginFragment.setOnClickListener {
            val email = binding.emailEditTextLoginFragment.text.toString()
            val password = binding.passwordEditTextLoginFragment.text.toString()
            userViewModel.login(email, password) { user ->
                if (user != null) {
                    // Save the logged-in state
                    with(sharedPreferences.edit()) {
                        putBoolean("isLoggedIn", true)
                        apply()
                    }

                    Toast.makeText(context, "User Login successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        R.id.action_loginFragment_to_homeFragment2,
                        null,
                        navOptions {
                            popUpTo(R.id.loginFragment) { inclusive = true }
                        })
                } else {
                    Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.signUpInLoginFragment.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment,
                null,
                navOptions {
                    popUpTo(R.id.loginFragment) { inclusive = true }
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
