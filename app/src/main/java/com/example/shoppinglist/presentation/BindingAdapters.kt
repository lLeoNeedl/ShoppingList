package com.example.shoppinglist.presentation

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.FragmentShopItemBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

//interface AddShopItemClickListener {
//    fun addShopItemClick(intent: Intent)
//}
//
//interface AddShopItemClickListenerLand {
//    fun addShopItemClick(shopItemFragment: ShopItemFragment)
//
//}
//
//@BindingAdapter("launchNextActivity")
//fun launchNextActivity(
//    floatingActionButton: FloatingActionButton,
//    addShopItemClickListener: AddShopItemClickListener
//) {
//    floatingActionButton.setOnClickListener {
//        addShopItemClickListener
//            .addShopItemClick(ShopItemActivity.newIntentAddItem(floatingActionButton.context))
//    }
//}
//
//@BindingAdapter("launchNewFragment")
//fun launchNewFragment(
//    floatingActionButton: FloatingActionButton,
//    addShopItemClickListenerLand: AddShopItemClickListenerLand
//) {
//    floatingActionButton.setOnClickListener {
//        addShopItemClickListenerLand
//            .addShopItemClick(ShopItemFragment.newInstanceAddItem())
//    }
//}

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