package com.example.lab2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class ResultFragment : Fragment() {

    private var listener: OnCancelListener? = null

    interface OnCancelListener {
        fun onCancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCancelListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnCancelListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        val textViewResult = view.findViewById<TextView>(R.id.textViewResult)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)

        val productType = arguments?.getString(ARG_PRODUCT_TYPE) ?: "Не выбрано"
        val brand = arguments?.getString(ARG_BRAND) ?: "Не выбрано"

        textViewResult.text = "Вы выбрали:\nТип: $productType\nФирма: $brand"

        buttonCancel.setOnClickListener {
            listener?.onCancel()
        }

        return view
    }

    companion object {
        private const val ARG_PRODUCT_TYPE = "arg_product_type"
        private const val ARG_BRAND = "arg_brand"

        fun newInstance(productType: String, brand: String): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putString(ARG_PRODUCT_TYPE, productType)
            args.putString(ARG_BRAND, brand)
            fragment.arguments = args
            return fragment
        }
    }
}



