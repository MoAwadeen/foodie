package iti.project.foodie.ui.recipe

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.Meal
import iti.project.foodie.databinding.FragmentHomeBinding
import iti.project.foodie.ui.adapters.HorizontalHomeAdapter
import iti.project.foodie.ui.adapters.VerticalHomeAdapter
import iti.project.foodie.ui.auth.AuthActivity
import iti.project.foodie.ui.viewModel.HomeViewModel

class HomeFragment : Fragment(), VerticalHomeAdapter.OnItemClickListener {

    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm: HomeViewModel
    private lateinit var verticalAdapter: VerticalHomeAdapter
    private lateinit var horizontalAdapter: HorizontalHomeAdapter
    private val verticalRecipeList = mutableListOf<Meal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeMvvm = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = NavHostFragment.findNavController(this)

        // Setup Horizontal RecyclerView
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontalRecView)
        horizontalAdapter = HorizontalHomeAdapter(requireContext(), mutableListOf())
        horizontalRecyclerView.adapter = horizontalAdapter
        horizontalRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Fetch and observe data for the horizontal RecyclerView
        homeMvvm.getCategories() // Fetch categories data
        observeCategories()

        // Setup Vertical RecyclerView
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.verticalRecView)
        verticalAdapter = VerticalHomeAdapter(verticalRecipeList, this)
        verticalRecyclerView.adapter = verticalAdapter
        verticalRecyclerView.layoutManager = LinearLayoutManager(context)

        // Fetch and observe data for the vertical RecyclerView
        repeat(5) {
            homeMvvm.getRandomMeal()
        }
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

    private fun observeRandomMeals() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner) { meal ->
            meal?.let {
                verticalRecipeList.add(it)
                verticalAdapter.notifyItemInserted(verticalRecipeList.size - 1)
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

    private fun showSignOutDialog(view: View) {
        // Get the blur overlay view
        val blurOverlay = view.findViewById<View>(R.id.blurOverlay)
        // Show the blur overlay
        blurOverlay?.visibility = View.VISIBLE

        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are You Sure You Want To Sign Out?")
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Sign Out") { _, _ ->
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
            }
        val alertDialog = builder.create()
        alertDialog.show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_orange))
        negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_purple))

        alertDialog.setOnDismissListener {
            blurOverlay?.visibility = View.GONE
        }
    }

    override fun onItemClick(meal: Meal) {
        navController.navigate(R.id.recipeDetailFragment)
        Toast.makeText(requireContext(), "Recipe Detailed Fragment", Toast.LENGTH_LONG).show()
    }

    override fun observeRandomMeal() {
        TODO("Not yet implemented")
    }
}
