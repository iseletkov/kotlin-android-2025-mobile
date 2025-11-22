package ru.psu.mobile.kotlin_android_2025_mobile.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

// Класс для сметной нормы
@Entity(tableName = "work_types")
data class CWorkType(
    @PrimaryKey
    val guid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "parent_guid")
    val parentGuid: String? = null,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "code")
    val code: String,
    @ColumnInfo(name = "units")
    val units: String = ""
)
