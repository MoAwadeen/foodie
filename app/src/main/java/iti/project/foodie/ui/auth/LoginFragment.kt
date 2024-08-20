package iti.project.foodie.ui.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // When you call this method(requireActivity()) inside a Fragment, it returns the current Activity that is hosting the Fragment
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        //requireContext() is used to get the Context that the Fragment is currently associated with
        val database =
            RoomDb.getDataBase(requireContext())
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
            binding.signUpInLoginFragment.setOnClickListener {
                findNavController().navigate(
                    R.id.action_loginFragment_to_registerFragment,
                    null,
                    navOptions {
                        popUpTo(R.id.loginFragment) { inclusive = true }
                    })
            }


        }

    }
//    onDestroyView() helps to prevent memory leaks
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}






