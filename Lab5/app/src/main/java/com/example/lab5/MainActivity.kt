package com.example.lab5

import android.app.Activity
import android.hardware.*
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.*

class MainActivity : Activity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magneticField: Sensor? = null

    private lateinit var compassArrow: ImageView
    private lateinit var angleText: TextView

    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)
    private var currentAzimuth = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compassArrow = findViewById(R.id.compassArrow)
        angleText = findViewById(R.id.angleText)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
        magneticField?.also {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    gravity = event.values.clone()
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    geomagnetic = event.values.clone()
                }
            }

            if (gravity.isNotEmpty() && geomagnetic.isNotEmpty()) {
                val R = FloatArray(9)
                val I = FloatArray(9)
                if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
                    val orientation = FloatArray(3)
                    SensorManager.getOrientation(R, orientation)

                    val azimuthRad = orientation[0]
                    val azimuthDeg = (Math.toDegrees(azimuthRad.toDouble()) + 360) % 360
                    angleText.text = "Азимут: ${azimuthDeg.toInt()}° ${getDirection(azimuthDeg)}"

                    rotateCompass(azimuthDeg.toFloat())
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun rotateCompass(newAzimuth: Float) {
        val rotateAnimation = RotateAnimation(
            currentAzimuth,
            newAzimuth,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = 500
        rotateAnimation.fillAfter = true
        compassArrow.startAnimation(rotateAnimation)
        currentAzimuth = newAzimuth
    }


    private fun getDirection(deg: Double): String {
        return when {
            deg >= 337.5 || deg < 22.5 -> "N"
            deg >= 22.5 && deg < 67.5 -> "NE"
            deg >= 67.5 && deg < 112.5 -> "E"
            deg >= 112.5 && deg < 157.5 -> "SE"
            deg >= 157.5 && deg < 202.5 -> "S"
            deg >= 202.5 && deg < 247.5 -> "SW"
            deg >= 247.5 && deg < 292.5 -> "W"
            deg >= 292.5 && deg < 337.5 -> "NW"
            else -> ""
        }
    }
}
