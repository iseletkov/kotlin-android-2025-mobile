package ru.psu.mobile.kotlin_android_2025_mobile.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.psu.mobile.kotlin_android_2025_mobile.model.CWorkTypeWithResources


@Dao
interface IDAOWorkTypeWithResources {

    @Transaction
    @Query("SELECT * FROM work_types WHERE guid = :guid")
    fun getWorkTypeWithResources(guid: String): Flow<CWorkTypeWithResources?>

    @Transaction
    @Query("SELECT * FROM work_types ORDER BY code")
    fun getAllWorkTypesWithResources(): Flow<List<CWorkTypeWithResources>>
}