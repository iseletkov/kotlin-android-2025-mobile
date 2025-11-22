package ru.psu.mobile.kotlin_android_2025_mobile.repositories

import kotlinx.coroutines.flow.Flow
import ru.psu.mobile.kotlin_android_2025_mobile.db.IDAOWorkTypes
import ru.psu.mobile.kotlin_android_2025_mobile.model.CWorkType

class CRepositoryWorkTypes(
    private val daoWorkTypes                : IDAOWorkTypes
) {

    fun getAll(): Flow<List<CWorkType>>     = daoWorkTypes.getAll()

    suspend fun getWorkTypeById(guid: String): CWorkType? = daoWorkTypes.getById(guid)

    suspend fun insertWorkType(workType: CWorkType) = daoWorkTypes.insert(workType)

//    suspend fun insertDefaultWorkTypes() {
//        val defaultWorkTypes = listOf(
//            CWorkType(
//                name = "Устройство фундаментов железобетонных",
//                code = "01-01-001-01"
//            ),
//            CWorkType(
//                name = "Кладка стен кирпичных",
//                code = "02-01-015-04"
//            ),
//            CWorkType(
//                name = "Устройство кровли из металлочерепицы",
//                code = "03-01-042-02"
//            ),
//            CWorkType(
//                name = "Штукатурка стен цементным раствором",
//                code = "04-01-008-01"
//            ),
//            CWorkType(
//                name = "Устройство стяжки пола",
//                code = "05-01-023-03"
//            )
//        )
//        daoWorkTypes.insertAll(defaultWorkTypes)
//    }

    suspend fun updateWorkType(workType: CWorkType) = daoWorkTypes.update(workType)

    suspend fun deleteWorkType(workType: CWorkType) = daoWorkTypes.delete(workType)

    suspend fun deleteWorkTypeById(guid: String) = daoWorkTypes.deleteById(guid)

    suspend fun clearAllWorkTypes() = daoWorkTypes.deleteAll()
}