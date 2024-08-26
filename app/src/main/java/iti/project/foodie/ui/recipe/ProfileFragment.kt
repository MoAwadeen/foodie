package iti.project.foodie.ui.recipe

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import iti.project.foodie.data.repository.AuthRepository
import iti.project.foodie.data.source.local.RoomDb
import iti.project.foodie.databinding.FragmentProfileBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var authRepository: AuthRepository
    private var name: String? = "User Name"
    private var birthDate: String? = "1/1/2025"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", android.content.Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "email@gmail.com")

        val userDb = RoomDb.getDataBase(requireContext())
        val userDao = userDb.UserDao()
        authRepository = AuthRepository(userDao)

        CoroutineScope(Dispatchers.IO).launch {
            // Fetch data from the repository
            name = email?.let { authRepository.getCurrentUserName(it) }
            birthDate = email?.let { authRepository.getCurrentUserBirthDate(it) }

            // Update the UI on the main thread
            withContext(Dispatchers.Main) {
                binding.userName.text = name
                binding.userBirthDte.text = birthDate
                binding.userEmail.text = email
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
