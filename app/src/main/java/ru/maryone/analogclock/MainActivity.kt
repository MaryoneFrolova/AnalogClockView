package ru.maryone.analogclock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.maryone.clock_custom_view_lib.AnalogClockCustomView

class MainActivity : AppCompatActivity() {
    private val clockView: AnalogClockCustomView
        get() = findViewById(R.id.clock_custom_view)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        clockView.startClock()
    }

    override fun onStop() {
        super.onStop()
        clockView.stopClock()
    }
}