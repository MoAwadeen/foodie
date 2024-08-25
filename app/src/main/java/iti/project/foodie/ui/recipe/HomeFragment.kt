package iti.project.foodie.ui.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.project.foodie.R
import iti.project.foodie.data.source.remote.model.Category
import iti.project.foodie.data.source.remote.model.Meal
import iti.project.foodie.ui.adapters.HorizontalHomeAdapter
import iti.project.foodie.ui.adapters.VerticalHomeAdapter
import iti.project.foodie.ui.viewModel.HomeViewModel

class HomeFragment : Fragment(), VerticalHomeAdapter.OnItemClickListener, HorizontalHomeAdapter.OnItemClickListener {

    private lateinit var navController: NavController
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
        val position = horizontalAdapter.categoryList.indexOf(category)
        horizontalAdapter.setSelectedPosition(position)
        homeMvvm.getMealsByCategory(category.strCategory)
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemClick(meal: Meal) {
        val bundle = Bundle().apply {
            putString("mealId", meal.idMeal) // Pass the meal ID here
        }
        navController.navigate(R.id.recipeDetailFragment, bundle)
        horizontalAdapter.notifyDataSetChanged() // Pass the bundle when navigating
    }

    override fun observeRandomMeal() {
    }
}