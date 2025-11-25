package ru.psu.mobile.kotlin_android_2025_mobile.model

import androidx.room.Embedded
import androidx.room.Relation


data class CWorkTypeWithResources(
    @Embedded
    val workType: CWorkType,

    @Relation(
        parentColumn = "guid",
        entityColumn = "workTypeGuid"
    )
    val resources: List<CResource>
)