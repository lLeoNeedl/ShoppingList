package com.example.shoppinglist.presentation.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.presentation.viewmodels.MainViewModel
import com.example.shoppinglist.presentation.adapters.ShopListAdapter
import com.example.shoppinglist.presentation.app.ShopListApplication
import com.example.shoppinglist.presentation.viewmodels.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditFinishedListener {

    private lateinit var binding: ActivityMainBinding

    private val component by lazy {
        (application as ShopListApplication).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    @Inject
    lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupRecyclerView()

        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }

        binding.buttonAddShopItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
        contentResolver.query(
            Uri.parse("content://com.example.shoppinglist/shop_items/"),
            null,
            null,
            null,
            null,
            null
        )
    }

    private fun isOnePaneMode(): Boolean {
        return binding.shopItemContainer == null
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        val rvShopList: RecyclerView = binding.rvShopList
        shopListAdapter = ShopListAdapter()
        with(rvShopList) {
            adapter = shopListAdapter
            with(ShopListAdapter) {
                recycledViewPool.setMaxRecycledViews(
                    VIEW_TYPE_DISABLED,
                    MAX_POOL_SIZE
                )
                recycledViewPool.setMaxRecycledViews(
                    VIEW_TYPE_ENABLED,
                    MAX_POOL_SIZE
                )
            }
        }
        setupClickListeners()
        setupSwipeListener(rvShopList)
    }

    private fun setupClickListeners() {
        shopListAdapter.onShopItemClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }

        shopListAdapter.onShopItemLongClickListener = {
            viewModel.editShopItem(it)
        }
    }

    override fun onEditFinished() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val shopItem = shopListAdapter.currentList[position]
                viewModel.removeShopItem(shopItem)
            }
        })
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }
}