package com.eltex.androidschool

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        startActivity(Intent(this@MainActivity, MainPostActivity::class.java))
        startActivity(Intent(this@MainActivity, MainEventActivity::class.java))

        finish()
    }
}
