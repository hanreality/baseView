package com.bereality.baseview

import android.os.Bundle
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val chronometer = findViewById<MyChronometer>(R.id.test)
        chronometer.setOnChronometerTickListener {
            println("onChronometerTick")
        }
        chronometer.format = "%s"
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
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
    }
}