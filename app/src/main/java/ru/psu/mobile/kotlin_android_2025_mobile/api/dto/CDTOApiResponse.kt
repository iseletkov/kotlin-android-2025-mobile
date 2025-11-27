package ru.psu.mobile.kotlin_android_2025_mobile.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class CDTOApiResponse(
    val data: Map<String, List<CDTOWorkType>>
)