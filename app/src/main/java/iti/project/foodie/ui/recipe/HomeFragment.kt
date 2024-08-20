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
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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


    private val verticalRecipeList = mutableListOf<Meal>()
    private lateinit var verticalAdapter: VerticalHomeAdapter
    private lateinit var horizontalAdapter: HorizontalHomeAdapter

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
        horizontalAdapter = HorizontalHomeAdapter(requireContext(), arrayListOf())
        horizontalRecyclerView.adapter = horizontalAdapter
        horizontalRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Fetch and observe data for the horizontal RecyclerView
        homeMvvm.getHorizontalMeals()
        observeHorizontalMeals()

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

    private fun observeRandomMeals() {
        homeMvvm.observeRandomMealLiveData().observe(viewLifecycleOwner, Observer { meal ->
            meal?.let {
                verticalRecipeList.add(it)
                verticalAdapter.notifyItemInserted(verticalRecipeList.size - 1)
            }
        })
    }

    private fun observeHorizontalMeals() {
        homeMvvm.observeHorizontalMealsLiveData().observe(viewLifecycleOwner, Observer { meals ->
            meals?.let {
                horizontalAdapter.updateData(it.map { meal -> meal.strMealThumb })
            }
        })
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
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)
                    Toast.makeText(requireContext(), item.title, Toast.LENGTH_LONG).show()
                    true
                }
                R.id.aboutCreator -> {
                    navController.navigate(R.id.creatorsFragment)
                    Toast.makeText(requireContext(), item.title, Toast.LENGTH_LONG).show()
                    true
                }
                R.id.signOut -> {
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun onItemClick(recipe: Meal) {
        navController.navigate(R.id.recipeDetailFragment)
        Toast.makeText(requireContext(), "Recipe Detailed Fragment", Toast.LENGTH_LONG).show()
    }

    override fun observeRandomMeal() {
        TODO("Not yet implemented")
    }
}
