package com.example.shoppinglist.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.databinding.FragmentShopItemBinding
import com.example.shoppinglist.domain.entities.ShopItem
import com.example.shoppinglist.presentation.app.ShopListApplication
import com.example.shoppinglist.presentation.viewmodels.ShopItemViewModel
import com.example.shoppinglist.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class ShopItemFragment : Fragment() {

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private val component by lazy {
        (requireActivity().application as ShopListApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val shopItemViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
    }

    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    private lateinit var onEditFinishedListener: OnEditFinishedListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnEditFinishedListener) {
            onEditFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement OnEditFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        try {
            parseParams()
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = shopItemViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        launchRightMode()

        shopItemViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditFinishedListener.onEditFinished()
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            val inputName = binding.etName.text?.toString()
            val inputCount = binding.etCount.text?.toString()
            shopItemViewModel.addShopItem(inputName, inputCount)
        }
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
            binding.etName.setText(it.name)
            binding.etCount.setText(it.count.toString())
        }
        binding.saveButton.setOnClickListener {
            val inputName = binding.etName.text?.toString()
            val inputCount = binding.etCount.text?.toString()
            shopItemViewModel.editShopItem(inputName, inputCount)
        }
    }

    interface OnEditFinishedListener {
        fun onEditFinished()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SHOP_ITEM_ID = "shop_item_id"
        private const val SCREEN_MODE = "extra_mode"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}