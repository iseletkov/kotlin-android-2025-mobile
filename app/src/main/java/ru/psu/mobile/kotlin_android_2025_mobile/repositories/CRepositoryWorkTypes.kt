package ru.psu.mobile.kotlin_android_2025_mobile.repositories

import kotlinx.coroutines.flow.Flow
import ru.psu.mobile.kotlin_android_2025_mobile.api.IServiceAPI
import ru.psu.mobile.kotlin_android_2025_mobile.api.dto.CDTOResource
import ru.psu.mobile.kotlin_android_2025_mobile.db.IDAOWorkTypes
import ru.psu.mobile.kotlin_android_2025_mobile.model.CWorkType
import kotlinx.serialization.json.Json

class CRepositoryWorkTypes(
    private val daoWorkTypes                : IDAOWorkTypes,
    private val serviceAPI                  : IServiceAPI
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

    suspend fun loadWorkTypesFromApi(): Boolean {
        return try {
            val response = serviceAPI.getWorkTypes()

            // Преобразуем ответ API в наши сущности
            val workTypes = response.flatMap { (_, normLegalDocs) ->
                normLegalDocs.map { doc ->
                    CWorkType(
                        guid = doc.guid,
                        parentGuid = doc.parentGuid,
                        name = doc.name,
                        code = doc.code,
                        units = extractUnitsFromNormTable(doc.normTableJson),
                    )
                }
            }

            // Очищаем старые данные и добавляем новые
            daoWorkTypes.deleteAll()
            daoWorkTypes.insertAll(workTypes)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    private fun extractUnitsFromNormTable(normTableJson: String?): String {
        if (normTableJson.isNullOrEmpty()) return ""

        return try {
            val normTable = Json.decodeFromString<List<CDTOResource>>(normTableJson)
            normTable.firstOrNull()?.MeterName ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun updateWorkType(workType: CWorkType) = daoWorkTypes.update(workType)

    suspend fun deleteWorkType(workType: CWorkType) = daoWorkTypes.delete(workType)

    suspend fun deleteWorkTypeById(guid: String) = daoWorkTypes.deleteById(guid)

    suspend fun clearAllWorkTypes() = daoWorkTypes.deleteAll()
}