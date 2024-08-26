package iti.project.foodie.ui.recipe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import iti.project.foodie.R
import iti.project.foodie.ui.auth.AuthActivity

class RecipeActivity : AppCompatActivity() {

    private lateinit var menuIcon : ImageView
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recipe)

        ViewCompat.setOnApplyWindowInsetsListener(/* view = */ findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.recipeNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setupWithNavController(navController)

        menuIcon = findViewById(R.id.menuIcon)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment , R.id.searchFragment , R.id.favoriteFragment , R.id.profileFragment -> {
                    bottomNavigationView.visibility = View.VISIBLE
                    menuIcon.visibility = View.VISIBLE
                }
                R.id.creatorsFragment-> {
                    bottomNavigationView.visibility = View.GONE
                    menuIcon.visibility = View.VISIBLE
                }
                else -> {
                    bottomNavigationView.visibility = View.GONE
                    menuIcon.visibility = View.GONE
                }
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeIcon -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.searchIcon -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.favouriteIcon -> {
                    navController.navigate(R.id.favoriteFragment)
                    true
                }
                R.id.profileIcon -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                else -> false
            }
        }

        menuIcon.setOnClickListener {
            showPopupMenu(it)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.home_menu, popupMenu.menu)

        for (i in 0 until popupMenu.menu.size()) {
            val menuItem = popupMenu.menu.getItem(i)
            val spannableTitle = SpannableString(menuItem.title)
            spannableTitle.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)),
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
                    true
                }
                R.id.signOut -> {
                    showSignOutDialog()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun signOutUser() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("isLoggedIn", false)
            clear()
            apply()
        }

        // Start the AuthActivity and clear the stack
        val intent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)

        Toast.makeText(this, R.string.signed_out_successfully, Toast.LENGTH_SHORT).show()
    }

    private fun showSignOutDialog() {
        val blurOverlay = findViewById<View>(R.id.blurOverlay)
        blurOverlay?.visibility = View.VISIBLE

        val builder = AlertDialog.Builder(this)
        builder.setMessage(R.string.sign_out_confirmation)
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                blurOverlay?.visibility = View.GONE
            }
            .setPositiveButton(R.string.sign_out) { _, _ ->
                signOutUser()
            }
        val alertDialog = builder.create()
        alertDialog.show()

        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setTextColor(ContextCompat.getColor(this, R.color.light_orange))
        negativeButton.setTextColor(ContextCompat.getColor(this, R.color.light_purple))

        alertDialog.setOnDismissListener {
            blurOverlay?.visibility = View.GONE
        }
    }
}
