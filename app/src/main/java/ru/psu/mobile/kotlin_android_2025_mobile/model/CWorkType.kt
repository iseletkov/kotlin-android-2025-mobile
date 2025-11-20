package ru.psu.mobile.kotlin_android_2025_mobile.model

import java.util.UUID

// Класс для сметной нормы
data class CWorkType(
    val guid: String = UUID.randomUUID().toString(),
    val parentGuid: String? = null,
    val name: String,
    val code: String,
    val units: String = ""
)