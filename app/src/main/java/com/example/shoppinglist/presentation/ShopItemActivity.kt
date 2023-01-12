package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        val shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        val shopItemViewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        shopItemViewModel.getShopItem(shopItemId)
        shopItemViewModel.shopItemLiveData.observe(this) {
            it.name
            it.count
        }
    }

    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "ShopItemId"
        fun newIntent(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}