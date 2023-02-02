package com.example.shoppinglist.domain.repository

import androidx.lifecycle.LiveData
import com.example.shoppinglist.domain.entities.ShopItem

interface ShopListRepository {

    suspend fun addShopItem(shopItem: ShopItem)

    suspend fun editShopItem(shopItem: ShopItem)

    suspend fun getShopItem(shopItemId: Int): ShopItem

    fun getShopList(): LiveData <List<ShopItem>>

    suspend fun removeShopItem(shopItem: ShopItem)

}