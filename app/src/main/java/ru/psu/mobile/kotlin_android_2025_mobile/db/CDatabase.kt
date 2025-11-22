package ru.psu.mobile.kotlin_android_2025_mobile.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import ru.psu.mobile.kotlin_android_2025_mobile.model.CWorkType

@Database(
    entities = [CWorkType::class],
    version = 1,
    exportSchema = false
)
abstract class CDatabase : RoomDatabase() {

    abstract fun daoWorkTypes(): IDAOWorkTypes

    companion object {
        @Volatile
        private var INSTANCE: CDatabase? = null

        fun getInstance(context: Context): CDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CDatabase::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}