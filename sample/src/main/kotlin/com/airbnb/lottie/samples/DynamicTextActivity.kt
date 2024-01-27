package com.airbnb.lottie.samples

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.samples.databinding.DynamicTextActivityBinding
import com.airbnb.lottie.samples.utils.viewBinding

class DynamicTextActivity : AppCompatActivity() {
    private val binding: DynamicTextActivityBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textDelegate = Text404Delegate(binding.dynamicTextView)
        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val str = s.toString()
                if (str.isNotEmpty()) {
                    textDelegate.setText("L", str.substring(0, 1))
                }
                if (str.length > 1) {
                    textDelegate.setText("C", str.substring(1, 2))
                }
                if (str.length > 2) {
                    textDelegate.setText("R", str.substring(2, 3))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        binding.dynamicTextView.setTextDelegate(textDelegate)
    }
}
