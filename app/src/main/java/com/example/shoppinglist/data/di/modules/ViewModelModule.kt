package com.example.shoppinglist.data.di.modules

import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.di.annotations.ViewModelKey
import com.example.shoppinglist.presentation.viewmodels.MainViewModel
import com.example.shoppinglist.presentation.viewmodels.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @Binds
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(impl: MainViewModel): ViewModel

    @IntoMap
    @Binds
    @ViewModelKey(ShopItemViewModel::class)
    fun bindShopItemViewModel(impl: ShopItemViewModel): ViewModel
}