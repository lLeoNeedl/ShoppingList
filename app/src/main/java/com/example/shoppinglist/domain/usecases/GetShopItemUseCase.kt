package com.example.shoppinglist.domain.usecases

import com.example.shoppinglist.domain.entities.ShopItem
import com.example.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject


class GetShopItemUseCase @Inject constructor(private val shopListRepository: ShopListRepository) {

    suspend fun getShopItem(shopItemId: Int): ShopItem = shopListRepository.getShopItem(shopItemId)
}