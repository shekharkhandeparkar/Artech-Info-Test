package co.artechinfo.shekhar.model

import androidx.room.RoomDatabase
import androidx.room.Database

@Database(entities = [Fact::class], version = 1, exportSchema = false)
abstract class FactDatabase : RoomDatabase() {
    abstract fun daoAccess(): FactDaoAccess
}