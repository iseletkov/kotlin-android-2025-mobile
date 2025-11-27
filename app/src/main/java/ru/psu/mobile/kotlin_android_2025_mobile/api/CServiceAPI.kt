package ru.psu.mobile.kotlin_android_2025_mobile.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.psu.mobile.kotlin_android_2025_mobile.api.dto.CDTOWorkType


interface IServiceAPI {
    @GET("api/NormLegalDocPublished")
    suspend fun getWorkTypes(
        @Query("parentGuid") parentGuid: String = "12acf065-4c1b-4350-b026-41a976fffa6d",
        @Query("normLegalDocPublishedType") normLegalDocPublishedType: Int = 10,
        @Query("normLegalDocPublishedTypes") normLegalDocPublishedTypes: List<Int> = listOf(10, 41),
        @Query("state") state: Int = 20,
        @Query("hasCatalog") hasCatalog: Boolean = true,
        @Query("isNorm") isNorm: Boolean = true,
        @Query("normLegalDocBaseKind") normLegalDocBaseKind: Int = 100,
        @Query("level") level: Int = 4
    ): Map<String, List<CDTOWorkType>>


}