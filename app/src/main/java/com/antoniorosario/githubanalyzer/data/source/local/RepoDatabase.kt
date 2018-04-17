package com.antoniorosario.githubanalyzer.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.antoniorosario.githubanalyzer.data.Repo

@Database(entities = [(Repo::class)], version = 1)
abstract class RepoDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao

    companion object {
        private var INSTANCE: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase? {

            if (INSTANCE == null) {

                INSTANCE = Room.inMemoryDatabaseBuilder(
                        context.applicationContext,
                        RepoDatabase::class.java)
                        .allowMainThreadQueries()
                        .build()
            }

            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}