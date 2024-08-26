package iti.project.foodie.ui.auth

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import iti.project.foodie.R
import iti.project.foodie.data.repository.AuthRepository
import iti.project.foodie.data.source.local.RoomDb
import iti.project.foodie.data.source.local.User
import iti.project.foodie.databinding.FragmentRegisterBinding
import iti.project.foodie.ui.viewModel.AuthViewModel
import java.util.Calendar

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
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
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)


        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        val database = RoomDb.getDataBase(requireContext())
        val userDao = database.UserDao()
        val repository = AuthRepository(userDao)


        userViewModel = AuthViewModel(repository)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.dateInputEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar[Calendar.MONTH] = Calendar.FEBRUARY  // Set to a specific month, e.g., February
            val specificMonth = calendar.timeInMillis

            val constraintsBuilder = CalendarConstraints.Builder()
                .setOpenAt(specificMonth)
                .build()

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date of Birth")
                .setCalendarConstraints(constraintsBuilder)
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                // Format the selected date and set it to the EditText
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selectedDate
                val formattedDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
                binding.dateInputEditText.setText(formattedDate)
            }

            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }


        binding.signUpButton.setOnClickListener {
            val name = binding.nameInputEditText.text.toString()
            val birthDate = binding.dateInputEditText.text.toString()
            val email = binding.emailInputEditText.text.toString()
            val password = binding.passwordInputEditText.text.toString()


            if (name.isEmpty() || birthDate.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            } else {

                val user = User(name = name, birthDate = birthDate, email = email, password = password)
                userViewModel.insertUser(user) {
                    with(sharedPreferences.edit()) {
                        putString("name", name)
                        putString("birthDate", birthDate)
                        putString("email", email)
                        putBoolean("isLoggedIn", true)
                        apply()
                    }

                    Toast.makeText(context, R.string.register_successfully, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        R.id.action_registerFragment_to_recipeActivity,
                        null,
                        navOptions {
                            popUpTo(R.id.registerFragment) { inclusive = true }
                        }
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
