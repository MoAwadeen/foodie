package iti.project.foodie.ui.recipe

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import iti.project.foodie.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", android.content.Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display user information when the view is created
        displayUserInfo()

    }

    // Method to retrieve and display user information
    private fun displayUserInfo() {
        // Retrieve user information from SharedPreferences
        val name = sharedPreferences.getString("name", "")
        val email = sharedPreferences.getString("email", "")
        val birthDate = sharedPreferences.getString("birthDate", "")

        // Display user information in the ProfileFragment's UI
        binding.profileNameInputEditText.setText(name)
        binding.profileEmailInputEditText.setText(email)
        binding.profileDateInputEditText.setText(birthDate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}