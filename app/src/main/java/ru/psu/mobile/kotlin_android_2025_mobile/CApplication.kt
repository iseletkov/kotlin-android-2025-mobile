package ru.psu.mobile.kotlin_android_2025_mobile

import android.app.Application
import ru.psu.mobile.kotlin_android_2025_mobile.db.CDatabase
import ru.psu.mobile.kotlin_android_2025_mobile.repositories.CRepositoryWorkTypeWithResources
import ru.psu.mobile.kotlin_android_2025_mobile.repositories.CRepositoryWorkTypes

class CApplication : Application() {
    val database by lazy { CDatabase.getInstance(this) }
    val repositoryWorkTypes by lazy { CRepositoryWorkTypes(database.daoWorkTypes()) }
    val repositoryWorkTypeWithResources by lazy {
        CRepositoryWorkTypeWithResources(
            database.daoWorkTypes(),
            database.daoResources(),
            database.daoWorkTypeWithResources()
        )
    }
}