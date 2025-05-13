package com.example.lab3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), InputFragment.OnInputListener, ResultFragment.OnCancelListener {

    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appDatabase = AppDatabase(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InputFragment())
                .commit()
        }
    }

    override fun onInputSent(productType: String, brand: String) {
        val success = appDatabase.insertEntry(productType, brand)

        Toast.makeText(
            this,
            if (success) "Дані успішно збережено" else "Помилка при збереженні",
            Toast.LENGTH_SHORT
        ).show()

        val resultFragment = ResultFragment.newInstance(productType, brand)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, resultFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCancel() {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, InputFragment())
            .commit()
    }
}
