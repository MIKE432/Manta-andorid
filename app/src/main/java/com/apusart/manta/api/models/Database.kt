package com.apusart.manta.api.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.apusart.manta.ui.club_scope_module.ui.club_articles.ArticlesListDao

@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class MantaDatabase: RoomDatabase() {
    abstract fun articlesListDao(): ArticlesListDao

    companion object {
        private lateinit var applicationContext: Context

        val database by lazy {
            databaseBuilder(applicationContext, MantaDatabase::class.java, "manta_database.db").build()
        }

        fun initialize(context: Context) {
            applicationContext = context.applicationContext
        }
    }
}
