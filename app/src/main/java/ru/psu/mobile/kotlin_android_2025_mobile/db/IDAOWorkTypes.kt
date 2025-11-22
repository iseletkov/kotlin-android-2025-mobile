package ru.psu.mobile.kotlin_android_2025_mobile.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.psu.mobile.kotlin_android_2025_mobile.model.CWorkType

@Dao
interface IDAOWorkTypes {
    @Query("SELECT * FROM work_types ORDER BY code")
    fun getAll(): Flow<List<CWorkType>>

    @Query("SELECT * FROM work_types WHERE guid = :guid")
    suspend fun getById(guid: String): CWorkType?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workType: CWorkType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workTypes: List<CWorkType>)

    @Update
    suspend fun update(workType: CWorkType)

    @Delete
    suspend fun delete(workType: CWorkType)

    @Query("DELETE FROM work_types WHERE guid = :guid")
    suspend fun deleteById(guid: String)

    @Query("DELETE FROM work_types")
    suspend fun deleteAll()
}