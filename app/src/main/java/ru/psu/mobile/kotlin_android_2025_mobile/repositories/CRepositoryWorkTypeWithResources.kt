package ru.psu.mobile.kotlin_android_2025_mobile.repositories

import kotlinx.coroutines.flow.Flow
import ru.psu.mobile.kotlin_android_2025_mobile.db.IDAOResources
import ru.psu.mobile.kotlin_android_2025_mobile.db.IDAOWorkTypeWithResources
import ru.psu.mobile.kotlin_android_2025_mobile.db.IDAOWorkTypes
import ru.psu.mobile.kotlin_android_2025_mobile.model.CResource
import ru.psu.mobile.kotlin_android_2025_mobile.model.CWorkTypeWithResources

class CRepositoryWorkTypeWithResources(
    private val daoWorkTypes: IDAOWorkTypes,
    private val daoResources: IDAOResources,
    private val daoWorkTypeWithResources: IDAOWorkTypeWithResources
) {

    // CRUD операции для ресурсов
    suspend fun addResourceToWorkType(workTypeGuid: String, resource: CResource): Long {
        val resourceWithGuid = resource.copy(workTypeGuid = workTypeGuid)
        return daoResources.insert(resourceWithGuid)
    }

//    suspend fun addResourcesToWorkType(workTypeGuid: String, resources: List<CResourceCatalogItem>) {
//        val resourcesWithGuid = resources.map { it.copy(workTypeGuid = workTypeGuid) }
//        resourceItemDao.insertAll(resourcesWithGuid)
//    }

    fun getWorkTypeResources(workTypeGuid: String): Flow<List<CResource>> {
        return daoResources.getResourcesByWorkType(workTypeGuid)
    }

    // Получение объединенных данных
    fun getWorkTypeWithResources(workTypeGuid: String): Flow<CWorkTypeWithResources?> {
        return daoWorkTypeWithResources.getWorkTypeWithResources(workTypeGuid)
    }

    fun getAllWorkTypesWithResources(): Flow<List<CWorkTypeWithResources>> {
        return daoWorkTypeWithResources.getAllWorkTypesWithResources()
    }

    // Удаление
    suspend fun deleteWorkTypeResources(workTypeGuid: String) {
        daoResources.deleteByWorkType(workTypeGuid)
    }

    // Импорт ресурсов из JSON для конкретного типа работ
//    suspend fun importResourcesFromJson(workTypeGuid: String, jsonString: String) {
//        val resources = parseResourcesFromJson(jsonString)
//        addResourcesToWorkType(workTypeGuid, resources)
//    }

//    private fun parseResourcesFromJson(jsonString: String): List<CResourceCatalogItem> {
//        // Ваша логика парсинга JSON и преобразования в CResourceCatalogItem
//        return emptyList() // Заглушка
//    }
}