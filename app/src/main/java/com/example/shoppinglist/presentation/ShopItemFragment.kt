package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment(
    private val screenMode: String = MODE_UNKNOWN,
    private val shopItemId: Int = ShopItem.UNDEFINED_ID
) : Fragment() {

    private lateinit var tilName: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var tilCount: TextInputLayout
    private lateinit var etCount: EditText
    private lateinit var saveButton: Button
    private lateinit var shopItemViewModel: ShopItemViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            parseParams()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

        initViews(view)
        shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
        setupTextChangeListeners()
        observeLiveData()
    }


    private fun parseParams() {
        if (screenMode != MODE_EDIT && screenMode != MODE_ADD) {
            throw RuntimeException("Param screen mode is absent")
        }
        if (screenMode == MODE_EDIT && shopItemId == ShopItem.UNDEFINED_ID) {
            throw RuntimeException("Param shop item id is absent")
        }
    }


    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        etName = view.findViewById(R.id.et_name)
        tilCount = view.findViewById(R.id.til_count)
        etCount = view.findViewById(R.id.et_count)
        saveButton = view.findViewById(R.id.save_button)
    }

    private fun observeLiveData() {
        shopItemViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            activity?.onBackPressed()
        }
        shopItemViewModel.errorInputName.observe(viewLifecycleOwner) {
            if (it) {
                tilName.error = getString(R.string.error_input_name)
            } else {
                tilName.error = null
            }
        }
        shopItemViewModel.errorInputCount.observe(viewLifecycleOwner) {
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
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
        shopItemViewModel.shopItemLiveData.observe(viewLifecycleOwner) {
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

        fun newInstanceAddItem() = ShopItemFragment(MODE_ADD)

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment(MODE_EDIT, shopItemId)
        }

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