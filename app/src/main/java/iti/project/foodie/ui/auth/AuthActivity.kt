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
        Thread.sleep(3000)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            startActivity(Intent(this, RecipeActivity::class.java))
            finish()
        } else {
            enableEdgeToEdge()
            setContentView(R.layout.activity_auth)
        }
    }
}
