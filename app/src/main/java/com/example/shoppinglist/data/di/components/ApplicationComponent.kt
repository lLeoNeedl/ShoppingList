package com.example.shoppinglist.data.di.components

import android.app.Application
import com.example.shoppinglist.data.di.annotations.ApplicationScope
import com.example.shoppinglist.data.di.modules.DataModule
import com.example.shoppinglist.data.di.modules.ViewModelModule
import com.example.shoppinglist.presentation.ui.MainActivity
import com.example.shoppinglist.presentation.ui.ShopItemFragment
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: ShopItemFragment)

    @Component.Factory
    interface ApplicationComponentFactory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}