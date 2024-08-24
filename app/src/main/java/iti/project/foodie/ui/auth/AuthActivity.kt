package iti.project.foodie.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import iti.project.foodie.R
import iti.project.foodie.ui.recipe.RecipeActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // If the user is logged in, navigate directly to the home screen
            startActivity(Intent(this, RecipeActivity::class.java))
            finish() // Finish the AuthActivity to prevent going back to it
        } else {
            enableEdgeToEdge()
            setContentView(R.layout.activity_auth)
        }
    }
}
