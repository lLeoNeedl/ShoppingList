package com.example.shoppinglist.domain.usecases

import com.example.shoppinglist.domain.entities.ShopItem
import com.example.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class RemoveShopItemUseCase @Inject constructor(
    private val shopListRepository: ShopListRepository
    ) {

    suspend fun removeShopItem(shopItem: ShopItem) {
        shopListRepository.removeShopItem(shopItem)
    }
}