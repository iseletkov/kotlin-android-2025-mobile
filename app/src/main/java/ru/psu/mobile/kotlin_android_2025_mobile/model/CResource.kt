package ru.psu.mobile.kotlin_android_2025_mobile.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "resources",
    foreignKeys = [
        ForeignKey(
            entity = CWorkType::class,
            parentColumns = ["guid"],
            childColumns = ["workTypeGuid"],
            onDelete = ForeignKey.CASCADE, // При удалении типа работ удаляются связанные ресурсы
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index("workTypeGuid")] // Индекс для быстрого поиска по внешнему ключу
)
data class CResource(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Внешний ключ к CWorkType
    val workTypeGuid: String,

    // Основные данные ресурса
    val name: String,
    val unitName: String,
    val value: Double,
//    val resourceType: String, // "labor", "machinery", "materials"
//    val resourceCategory: String, // "workers", "machine_operators", "equipment"

    // Дополнительные поля
    val cipher: String? = null, // Шифр/код ресурса
//    val specifications: String? = null, // Характеристики

    // Технические поля
//    val sortOrder: Int = 0,
//    val createdAt: Long = System.currentTimeMillis(),
//    val isActive: Boolean = true
)