package com.eltex.androidschool

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.activity.PostActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@MainActivity, PostActivity::class.java))
//        startActivity(Intent(this@MainActivity, EventActivity::class.java))

        finish()
    }
}
