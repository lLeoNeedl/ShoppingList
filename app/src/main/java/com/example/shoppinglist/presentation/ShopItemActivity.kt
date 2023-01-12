package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

    private lateinit var tilName: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var tilCount: TextInputLayout
    private lateinit var etCount: EditText
    private lateinit var saveButton: Button
    private lateinit var shopItemViewModel: ShopItemViewModel

    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        try {
            parseIntent()
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }

        initViews()
        shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
        setupTextChangeListeners()
        observeLiveData()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        etName = findViewById(R.id.et_name)
        tilCount = findViewById(R.id.til_count)
        etCount = findViewById(R.id.et_count)
        saveButton = findViewById(R.id.save_button)
    }

    private fun observeLiveData() {
        shopItemViewModel.shouldCloseScreen.observe(this) {
            finish()
        }
        shopItemViewModel.errorInputName.observe(this) {
            if (it) {
                tilName.error = getString(R.string.error_input_name)
            } else {
                tilName.error = null
            }
        }
        shopItemViewModel.errorInputCount.observe(this) {
            if (it) {
                tilCount.error = getString(R.string.error_input_count)
            } else {
                tilCount.error = null
            }
        }
    }

    private fun launchAddMode() {
        setupSaveButton()
    }

    private fun launchEditMode() {
        try {
            shopItemViewModel.getShopItem(shopItemId)
        } catch (
            e: Exception
        ) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        shopItemViewModel.shopItemLiveData.observe(this) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        setupEditButton()
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            val inputName = etName.text?.toString()
            val inputCount = etCount.text?.toString()
            shopItemViewModel.addShopItem(inputName, inputCount)
        }
    }

    private fun setupEditButton() {
        saveButton.setOnClickListener {
            val inputName = etName.text?.toString()
            val inputCount = etCount.text?.toString()
            shopItemViewModel.editShopItem(inputName, inputCount)
        }
    }

    private fun setupTextChangeListeners() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                shopItemViewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
        )
    }

    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "shop_item_id"
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}
