package com.example.shoppinglist.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun shopListDao(): ShopListDao

    companion object {
        private const val DB_NAME = "shop_item.db"
        private var db: AppDatabase? = null
        private var lock = Any()

        fun getInstance(application: Application): AppDatabase {
            db?.let {
                return it
            }
            synchronized(lock) {
                db?.let {
                    return it
                }
                val instance = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                ).allowMainThreadQueries().build()
                db = instance
                return instance
            }
        }
    }
}