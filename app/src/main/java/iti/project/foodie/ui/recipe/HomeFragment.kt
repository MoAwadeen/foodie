package iti.project.foodie.ui.recipe

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.Category
import iti.project.foodie.data.source.remote.model.Meal
import iti.project.foodie.databinding.FragmentHomeBinding
import iti.project.foodie.ui.adapters.HorizontalHomeAdapter
import iti.project.foodie.ui.adapters.VerticalHomeAdapter
import iti.project.foodie.ui.auth.AuthActivity
import iti.project.foodie.ui.viewModel.HomeViewModel

class HomeFragment : Fragment(), VerticalHomeAdapter.OnItemClickListener, HorizontalHomeAdapter.OnItemClickListener {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var verticalAdapter: VerticalHomeAdapter
    private lateinit var horizontalAdapter: HorizontalHomeAdapter
    private val verticalRecipeList = mutableListOf<Meal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)

        // Setup Horizontal RecyclerView
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontalRecView)
        horizontalAdapter = HorizontalHomeAdapter(requireContext(), mutableListOf(), this)
        horizontalRecyclerView.adapter = horizontalAdapter
        horizontalRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Fetch and observe data for the horizontal RecyclerView
        homeMvvm.getCategories()
        observeCategories()

        // Setup Vertical RecyclerView
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.verticalRecView)
        verticalAdapter = VerticalHomeAdapter(verticalRecipeList, this)
        verticalRecyclerView.adapter = verticalAdapter
        verticalRecyclerView.layoutManager = LinearLayoutManager(context)

        // Observe random meals
        observeRandomMeals()

        // Handle the menu icon click
        val menuIcon = view.findViewById<ImageView>(R.id.menuIcon)
        menuIcon.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun observeCategories() {
        homeMvvm.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categories?.let {
                horizontalAdapter.updateData(it)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeRandomMeals() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner) { meals ->
            verticalRecipeList.clear()
            meals?.let {
                verticalRecipeList.addAll(it)
                verticalAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onItemClick(category: Category) {
        homeMvvm.getMealsByCategory(category.strCategory ?: "")
        observeMealsByCategory()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeMealsByCategory() {
        homeMvvm.observeMealsByCategoryLiveData().observe(viewLifecycleOwner) { meals ->
            verticalRecipeList.clear()
            meals?.let {
                verticalRecipeList.addAll(it)
                verticalAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.home_menu, popupMenu.menu)

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
                    navController.navigate(R.id.creatorsFragment)
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
    private fun signOutUser() {
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", false)
            apply()
        }

        // Start the IntroActivity and clear the stack
        val intent = Intent(requireContext(), AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)

        Toast.makeText(requireContext(), "Signed out successfully", Toast.LENGTH_SHORT).show()
    }




    private fun showSignOutDialog(view: View) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are You Sure You Want To Sign Out?")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Sign Out") { _, _ ->
                signOutUser()
            }
        val alertDialog = builder.create()
        alertDialog.show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_orange))
        negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_purple))
    }

    override fun onItemClick(meal: Meal) {
        val bundle = Bundle().apply {
            putString("mealId", meal.idMeal) // Pass the meal ID here
        }
        navController.navigate(R.id.recipeDetailFragment, bundle) // Pass the bundle when navigating
    }

    override fun observeRandomMeal() {
    }
}