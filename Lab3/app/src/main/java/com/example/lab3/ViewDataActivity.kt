package com.example.lab3

import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity

class ViewDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data)

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }

        val db = AppDatabase(this)
        val entries = db.getAllEntries()

        val listView = findViewById<ListView>(R.id.listViewData)
        if (entries.isEmpty()) {
            Toast.makeText(this, "Дані відсутні", Toast.LENGTH_SHORT).show()
        } else {
            val data = entries.map { "Тип: ${it.first}, Фірма: ${it.second}" }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, data)
            listView.adapter = adapter
        }
    }
}
