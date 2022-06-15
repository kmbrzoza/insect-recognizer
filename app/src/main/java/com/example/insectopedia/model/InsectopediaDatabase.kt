package com.example.insectopedia.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Insect::class], version = 2, exportSchema = false)
abstract class InsectopediaDatabase : RoomDatabase() {

    abstract val dao: InsectopediaDAO

    companion object {

        @Volatile
        private var INSTANCE: InsectopediaDatabase? = null

        fun getInstance(context: Context): InsectopediaDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        InsectopediaDatabase::class.java,
                        "insectopedia_database"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}