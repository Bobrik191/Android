package com.example.lab1

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val productGroup = findViewById<RadioGroup>(R.id.rgProductType)
        val companyGroup = findViewById<RadioGroup>(R.id.rgCompany)
        val btnOk = findViewById<Button>(R.id.btnOk)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val resultText = findViewById<TextView>(R.id.tvResult)

        btnOk.setOnClickListener {
            val selectedProductId = productGroup.checkedRadioButtonId
            val selectedCompanyId = companyGroup.checkedRadioButtonId

            if (selectedProductId != -1 && selectedCompanyId != -1) {
                val selectedProduct = findViewById<RadioButton>(selectedProductId).text
                val selectedCompany = findViewById<RadioButton>(selectedCompanyId).text
                resultText.text = "Вибрано: $selectedProduct від $selectedCompany"
            } else {
                Toast.makeText(this, "Будь ласка, виберіть товар і фірму!", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            productGroup.clearCheck()
            companyGroup.clearCheck()
            resultText.text = ""
        }
    }
}