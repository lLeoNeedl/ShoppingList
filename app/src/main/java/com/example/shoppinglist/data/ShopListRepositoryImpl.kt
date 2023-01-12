package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = sortedSetOf<ShopItem>(
        { o1, o2 -> o1.id - o2.id }
    )

    private var autoIncrementId = 0

    private val shopListLiveData = MutableLiveData<List<ShopItem>>()


    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId++
        }
        shopList.add(shopItem)
        updateLiveData()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem = shopList.find { it.id == shopItemId }
        ?: throw RuntimeException("Element with id $shopItemId not found")

    override fun getShopList(): LiveData<List<ShopItem>> = shopListLiveData

    override fun removeShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateLiveData()
    }

    private fun updateLiveData() {
        shopListLiveData.value = shopList.toList()
    }
}