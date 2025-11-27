package ru.psu.mobile.kotlin_android_2025_mobile

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.psu.mobile.kotlin_android_2025_mobile.api.IServiceAPI
import ru.psu.mobile.kotlin_android_2025_mobile.db.CDatabase
import ru.psu.mobile.kotlin_android_2025_mobile.repositories.CRepositoryWorkTypeWithResources
import ru.psu.mobile.kotlin_android_2025_mobile.repositories.CRepositoryWorkTypes

class CApplication : Application() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://fgiscs.minstroyrf123.ru/")
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val serviceAPI = retrofit.create(IServiceAPI::class.java)

    private val database by lazy { CDatabase.getInstance(this) }
    val repositoryWorkTypes by lazy { CRepositoryWorkTypes(database.daoWorkTypes(), serviceAPI) }
    val repositoryWorkTypeWithResources by lazy {
        CRepositoryWorkTypeWithResources(
            database.daoWorkTypes(),
            database.daoResources(),
            database.daoWorkTypeWithResources()
        )
    }

//    override fun onCreate() {
//        super.onCreate()
//
//    }
}