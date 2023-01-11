package com.example.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.*

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val getShopListUseCase = GetShopListUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val shopItem = ShopItem(name, inputCount.toInt(), true, ShopItem.UNDEFINED_ID)
        addShopItemUseCase.addShopItem(shopItem)
    }

    fun editNameAndCount(shopItem: ShopItem, name: String, count: Int) {
        val newItem = shopItem.copy(name = name, count = count)
        editShopItemUseCase.editShopItem(newItem)
    }

    private fun parseName(inputName: String?) = inputName?.trim() ?: ""

    private fun parseCount (inputCount: String?)
}