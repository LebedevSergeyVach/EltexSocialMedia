package com.eltex.androidschool.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eltex.androidschool.R
import com.eltex.androidschool.ui.common.EdgeToEdgeHelper


class MainActivity : AppCompatActivity(R.layout.main_activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        EdgeToEdgeHelper.applyingIndentationOfSystemFields(findViewById(android.R.id.content))
    }
}
