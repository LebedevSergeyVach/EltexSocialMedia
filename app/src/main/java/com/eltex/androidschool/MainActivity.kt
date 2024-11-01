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
    private lateinit var author: TextView
    private lateinit var avatar: TextView
    private lateinit var dataPublication: TextView
    private lateinit var optionConducting: TextView
    private lateinit var dataEvent: TextView
    private lateinit var content: TextView
    private lateinit var link: TextView
    private lateinit var like: TextView
    private lateinit var peoples: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.event_card_option_1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        author = findViewById(R.id.author)
        avatar = findViewById(R.id.authorInitials)
        dataPublication = findViewById(R.id.dataPlacement)
        optionConducting = findViewById(R.id.optionConducting)
        dataEvent = findViewById(R.id.dataEvent)
        content = findViewById(R.id.content)
        link = findViewById(R.id.link)
        like = findViewById(R.id.like)
        peoples = findViewById(R.id.peoples)

        textDisplayView(author, R.string.testAuthor)
        textDisplayView(avatar, R.string.testAvatar)
        textDisplayView(dataPublication, R.string.testDataPlacement)
        textDisplayView(dataEvent, R.string.testDataEvent)
        textDisplayView(content, R.string.testContent)
        textDisplayView(link, R.string.testLink)
        textDisplayView(like, R.string.testLike)
        textDisplayView(peoples, R.string.testPeoples)
    }


    private fun textDisplayView(textView: TextView, stringIdText: Int) {
        textView.text = getString(stringIdText)
    }

    private fun textDisplayView(
        textView: TextView,
        stringIdText: Int,
        colorIdText: Int,
        textSize: Int
    ) {
        textView.text = getString(stringIdText)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        textView.setTextColor(resources.getColor(colorIdText))
        textView.gravity = Gravity.CENTER
    }
}
