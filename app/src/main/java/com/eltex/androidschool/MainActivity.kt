package com.eltex.androidschool

import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var helloAppText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        helloAppText = findViewById(R.id.hello_app_text)
        textDisplayView(helloAppText, R.string.eltex, 50)
    }

    private fun textDisplayView(
        textView: TextView,
        stringIdText: Int,
        sizeText: Int
    ) {
        textView.text = getString(stringIdText)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeText.toFloat())
        textView.gravity = Gravity.CENTER
    }
}
