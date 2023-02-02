package com.example.shoppinglist.domain.usecases

import com.example.shoppinglist.domain.entities.ShopItem
import com.example.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class AddShopItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {

    suspend fun addShopItem(shopItem: ShopItem) {
        shopListRepository.addShopItem(shopItem)
    }
}