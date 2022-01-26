package com.bereality.baseview

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.selected_text).isSelected = true
        findViewById<TextView>(R.id.pressed_text).setOnClickListener {
            it.isSelected = !it.isSelected
        }
    }
}