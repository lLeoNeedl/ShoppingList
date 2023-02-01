package com.example.shoppinglist.data.di.modules

import android.app.Application
import androidx.databinding.DataBinderMapperImpl
import com.example.shoppinglist.data.database.AppDatabase
import com.example.shoppinglist.data.database.ShopListDao
import com.example.shoppinglist.data.di.annotations.ApplicationScope
import com.example.shoppinglist.data.repository_impl.ShopListRepositoryImpl
import com.example.shoppinglist.domain.repository.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {
        @Provides
        @ApplicationScope
        fun provideShopListDao(application: Application): ShopListDao {
            return AppDatabase.getInstance(application).shopListDao()
        }
    }
}