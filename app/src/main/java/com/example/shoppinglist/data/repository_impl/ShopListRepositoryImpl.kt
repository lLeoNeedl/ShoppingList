package com.example.shoppinglist.data.repository_impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.shoppinglist.data.database.ShopListDao
import com.example.shoppinglist.data.mapper.ShopListMapper
import com.example.shoppinglist.domain.entities.ShopItem
import com.example.shoppinglist.domain.repository.ShopListRepository
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor(
    private val shopListDao: ShopListDao,
    private val mapper: ShopListMapper
) : ShopListRepository {

    override suspend fun addShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun editShopItem(shopItem: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(shopItem))
    }

    override suspend fun getShopItem(shopItemId: Int): ShopItem {
        val shopItemDbModel = shopListDao.getShopItem(shopItemId)
        return mapper.mapDbModelToEntity(shopItemDbModel)
    }

    override fun getShopList(): LiveData<List<ShopItem>> =
        Transformations.map(shopListDao.getShopList()) {
            mapper.mapListDbModelToListEntity(it)
        }

    override suspend fun removeShopItem(shopItem: ShopItem) {
        shopListDao.deleteShopItem(shopItem.id)
    }
}