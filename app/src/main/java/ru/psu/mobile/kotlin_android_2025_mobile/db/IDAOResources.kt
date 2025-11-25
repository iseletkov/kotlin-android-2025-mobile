package ru.psu.mobile.kotlin_android_2025_mobile.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.psu.mobile.kotlin_android_2025_mobile.model.CResource

@Dao
interface IDAOResources {

    // Основные операции
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CResource): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CResource>)

    @Update
    suspend fun update(item: CResource)

    @Delete
    suspend fun delete(item: CResource)

    // Запросы по связи один-ко-многим
    @Query("SELECT * FROM resources WHERE workTypeGuid = :workTypeGuid ORDER BY cipher, name")
    fun getResourcesByWorkType(workTypeGuid: String): Flow<List<CResource>>

    // Удаление всех ресурсов, связанных с видом работ workTypeGuid
    @Query("DELETE FROM resources WHERE workTypeGuid = :workTypeGuid")
    suspend fun deleteByWorkType(workTypeGuid: String)

}