package com.bereality.baseview

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.widget.LiveColumnView


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.pressed_drawable).setOnClickListener {
            (it as TextView).setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.test_selector2,
                0,
                0,
                0
            )
        }
        findViewById<TextView>(R.id.selected_text).isSelected = true
        findViewById<TextView>(R.id.pressed_text).setOnClickListener {
            it.isSelected = !it.isSelected
        }
        val liveColumnView = findViewById<LiveColumnView>(R.id.live_column_view)
        liveColumnView.setOnClickListener {
            liveColumnView.setGradientColors(Color.RED, Color.WHITE)
        }
    }
}