package iti.project.foodie.ui.recipe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iti.project.foodie.R
import iti.project.foodie.ui.adapters.HorizontalHomeAdapter
import iti.project.foodie.ui.adapters.VerticalHomeAdapter
import iti.project.foodie.ui.auth.AuthActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater : LayoutInflater ,
        container : ViewGroup? ,
        savedInstanceState : Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)

        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontalRecView)
        val arrayList = ArrayList<String>()

        // Add multiple images to arrayList
        arrayList.add("https://www.themealdb.com/images/category/beef.png")
        arrayList.add("https://www.themealdb.com/images/category/chicken.png")
        arrayList.add("https://www.themealdb.com/images/category/dessert.png")
        arrayList.add("https://www.themealdb.com/images/category/lamb.png")
        arrayList.add("https://www.themealdb.com/images/category/miscellaneous.png")
        arrayList.add("https://www.themealdb.com/images/category/pasta.png")
        arrayList.add("https://www.themealdb.com/images/category/pork.png")
        arrayList.add("https://www.themealdb.com/images/category/seafood.png")
        arrayList.add("https://www.themealdb.com/images/category/side.png")
        arrayList.add("https://www.themealdb.com/images/category/starter.png")
        arrayList.add("https://www.themealdb.com/images/category/vegan.png")
        arrayList.add("https://www.themealdb.com/images/category/vegetarian.png")
        arrayList.add("https://www.themealdb.com/images/category/breakfast.png")
        arrayList.add("https://www.themealdb.com/images/category/goat.png")
        arrayList.add("https://www.themealdb.com/images/media/meals/1548772327.jpg")

        val adapter = HorizontalHomeAdapter(requireContext(), arrayList)
        horizontalRecyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : HorizontalHomeAdapter.OnItemClickListener {
            override fun onClick(imageView : ImageView? , path:  String?) {
                // Handle item click here
            }
        })

        val recipeList = listOf(
            Recipe(
                "https://www.themealdb.com/images/category/vegan.png" ,
                "Recipe Title 1" ,
                "Ingredients 1" ,
                "Country 1" ,
                "Category 1" ,
                true
            ),
            Recipe(
                "https://www.themealdb.com/images/category/pasta.png" ,
                "Recipe Title 2" ,
                "Ingredients 2" ,
                "Country 2" ,
                "Category 2" ,
                false
            ),
            Recipe(
                "https://www.themealdb.com/images/category/chicken.png" ,
                "Recipe Title 3" ,
                "Ingredients 3" ,
                "Country 3" ,
                "Category 3" ,
                false
            ),
            Recipe(
                "https://www.themealdb.com/images/category/breakfast.png" ,
                "Recipe Title 4" ,
                "Ingredients 4" ,
                "Country 4" ,
                "Category 4" ,
                false
            ),
            Recipe(
                "https://www.themealdb.com/images/category/dessert.png" ,
                "Recipe Title 5" ,
                "Ingredients 5" ,
                "Country 5" ,
                "Category 5" ,
                false
            ),

            )

        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.verticalRecView)
        verticalRecyclerView.adapter = VerticalHomeAdapter(recipeList)
        verticalRecyclerView.layoutManager = LinearLayoutManager(context)

        // Handle the menu icon click
        val menuIcon = view.findViewById<ImageView>(R.id.menuIcon)
        menuIcon.setOnClickListener {
            showPopupMenu(it)
        }

    }

    @SuppressLint("RestrictedApi")
    private fun showPopupMenu(view : View) {
        val popupMenu = PopupMenu(requireContext() , view)
        popupMenu.menuInflater.inflate(R.menu.home_menu, popupMenu.menu)

        if (popupMenu.menu is MenuBuilder) {
            val menuBuilder = popupMenu.menu as MenuBuilder
            menuBuilder.setOptionalIconsVisible(false)
        }

        for (i in 0 until popupMenu.menu.size()) {
            val menuItem = popupMenu.menu.getItem(i)
            menuItem.icon?.apply {
                // Set the icon to be displayed at the start
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            }
            menuItem.actionView?.apply {
                // Adjust padding
                setPadding(16, 0, 0, 0)
            }
        }

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    Toast.makeText(requireContext() , item.title , Toast.LENGTH_LONG).show()
                    true
                }
                R.id.aboutCreator -> {
                    Toast.makeText(requireContext() , item.title , Toast.LENGTH_LONG).show()
                    true
                }
                R.id.signOut -> {
                    val intent = Intent(requireContext() , AuthActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    data class Recipe(
        val imageUrl : String ,
        val title : String ,
        val ingredients : String ,
        val country : String ,
        val category : String ,
        val isFavorite : Boolean
    )
}


