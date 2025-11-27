package ru.psu.mobile.kotlin_android_2025_mobile.api.dto

import kotlinx.serialization.Serializable


// Вспомогательные классы для парсинга JSON
@Serializable
data class CDTOResource(
    val Number: String,
    val Name: String,
    val MeterName: String
)