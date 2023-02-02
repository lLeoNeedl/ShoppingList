package com.example.shoppinglist.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.repository_impl.ShopListRepositoryImpl
import com.example.shoppinglist.domain.entities.ShopItem
import com.example.shoppinglist.domain.usecases.EditShopItemUseCase
import com.example.shoppinglist.domain.usecases.GetShopListUseCase
import com.example.shoppinglist.domain.usecases.RemoveShopItemUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getShopListUseCase: GetShopListUseCase,
    private val removeShopItemUseCase: RemoveShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase
) : ViewModel() {

    val shopList = getShopListUseCase.getShopList()

    fun removeShopItem(shopItem: ShopItem) {
        viewModelScope.launch {
            removeShopItemUseCase.removeShopItem(shopItem)
        }
    }

    fun editShopItem(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        viewModelScope.launch {
            editShopItemUseCase.editShopItem(newItem)
        }
    }
}