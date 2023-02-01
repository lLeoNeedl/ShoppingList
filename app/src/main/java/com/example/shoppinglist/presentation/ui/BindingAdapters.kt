package com.example.shoppinglist.presentation.ui

import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


interface OnTextChangeListener {
    fun onTextChange()
}

@BindingAdapter("errorInputName")
fun setErrorInputName(textInputLayout: TextInputLayout, error: Boolean) {
    if (error) {
        textInputLayout.error = textInputLayout.context.getString(R.string.error_input_name)
    } else {
        textInputLayout.error = null
    }
}

@BindingAdapter("errorInputCount")
fun setErrorInputCount(textInputLayout: TextInputLayout, error: Boolean) {
    if (error) {
        textInputLayout.error = textInputLayout.context.getString(R.string.error_input_count)
    } else {
        textInputLayout.error = null
    }
}

@BindingAdapter("textChangeListener")
fun bindTextChangeListener(
    textInputEditText: TextInputEditText,
    onTextChangeListener: OnTextChangeListener
) {
    textInputEditText.addTextChangedListener(onTextChanged = { _, _, _, _ ->
        onTextChangeListener.onTextChange()
    })
}