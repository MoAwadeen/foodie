package iti.project.foodie.ui.recipe

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import iti.project.foodie.R
import iti.project.foodie.databinding.FragmentProfileBinding
import iti.project.foodie.ui.auth.AuthActivity

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

        // Handle the menu icon click
        val menuIcon = view.findViewById<ImageView>(R.id.menuIcon)
        menuIcon.setOnClickListener {
            showPopupMenu(it)
        }
    }

    // Method to retrieve and display user information
    private fun displayUserInfo() {
        // Retrieve user information from SharedPreferences
        val name = sharedPreferences.getString("name", "")
        val email = sharedPreferences.getString("email", "")
        val birthDate = sharedPreferences.getString("birthDate", "")

        // Display user information in the ProfileFragment's UI
        binding.profileNameInputEditText.setText(name)
        binding.root.setTextDirection(email)
        binding.profileNameInputEditText.setText(birthDate)
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.home_menu, popupMenu.menu) // Use the menu resource XML

        for (i in 0 until popupMenu.menu.size()) {
            val menuItem = popupMenu.menu.getItem(i)
            val spannableTitle = SpannableString(menuItem.title)
            spannableTitle.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.purple)),
                0,
                spannableTitle.length,
                0
            )
            menuItem.title = spannableTitle
        }

        // Change the background of the PopupMenu
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menuPopupHelper = popup.get(popupMenu)

        menuPopupHelper.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(menuPopupHelper, true)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.aboutCreator -> {
                    findNavController().navigate(R.id.creatorsFragment) // Update with your navigation action
                    Toast.makeText(requireContext(), item.title, Toast.LENGTH_LONG).show()
                    true
                }

                R.id.signOut -> {
                    showSignOutDialog(view)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showSignOutDialog(view: View) {
        // Get the blur overlay view
        val blurOverlay = view.findViewById<View>(R.id.blurOverlay)
        // Show the blur overlay
        blurOverlay?.visibility = View.VISIBLE

        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are You Sure You Want To Sign Out?")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                blurOverlay?.visibility = View.GONE // Hide the blur overlay when canceled
            }
            .setPositiveButton("Sign Out") { _, _ ->
                // Clear SharedPreferences and navigate to AuthActivity
                sharedPreferences.edit().clear().apply()

                val intent = Intent(requireContext(), AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear activity stack
                startActivity(intent)

                Toast.makeText(requireContext(), "Signed out successfully", Toast.LENGTH_SHORT).show()
            }
        val alertDialog = builder.create()
        alertDialog.show()
        val userName = "John Doe" // Replace this with the actual user's name
        val profileNameEditText: TextInputEditText = findViewById(R.id.profileName)
        profileNameEditText.setText(userName)


        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_orange))
        negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_purple))

        alertDialog.setOnDismissListener {
            blurOverlay?.visibility = View.GONE
        }
    }

    private fun findViewById(profileName: Int): TextInputEditText {
        TODO("Not yet implemented")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun ConstraintLayout.setTextDirection(email: String?) {
    TODO("Not yet implemented")
}
