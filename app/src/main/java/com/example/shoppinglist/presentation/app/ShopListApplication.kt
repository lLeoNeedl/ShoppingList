package com.example.shoppinglist.presentation.app

import android.app.Application
import com.example.shoppinglist.data.di.components.DaggerApplicationComponent

class ShopListApplication: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}