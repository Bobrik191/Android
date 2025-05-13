package com.example.lab3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

class InputFragment : Fragment() {

    private var listener: OnInputListener? = null
    private lateinit var radioGroupProduct: RadioGroup
    private lateinit var radioGroupBrand: RadioGroup

    interface OnInputListener {
        fun onInputSent(productType: String, brand: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnInputListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnInputListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_input, container, false)

        radioGroupProduct = view.findViewById(R.id.radioGroupProduct)
        radioGroupBrand = view.findViewById(R.id.radioGroupBrand)

        val buttonOk = view.findViewById<Button>(R.id.buttonOk)

        buttonOk.setOnClickListener {
            val selectedProduct = view.findViewById<android.widget.RadioButton>(radioGroupProduct.checkedRadioButtonId)?.text.toString()
            val selectedBrand = view.findViewById<android.widget.RadioButton>(radioGroupBrand.checkedRadioButtonId)?.text.toString()

            if (selectedProduct.isNotEmpty() && selectedBrand.isNotEmpty()) {
                listener?.onInputSent(selectedProduct, selectedBrand)
            }
        }

        val buttonOpen = view.findViewById<Button>(R.id.buttonOpen)
        buttonOpen.setOnClickListener {
            startActivity(Intent(requireContext(), ViewDataActivity::class.java))
        }

        return view
    }

    fun clearSelection() {
        radioGroupProduct.clearCheck()
        radioGroupBrand.clearCheck()
    }
}
