package com.eltex.androidschool.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.eltex.androidschool.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val fragmentName = intent?.getStringExtra(MainActivity.FRAGMENT)

        val mainIntent = Intent(this@SplashActivity, MainActivity::class.java).apply {
            putExtra(MainActivity.FRAGMENT, fragmentName)
        }

        startActivity(mainIntent)
        finish()
    }
}
