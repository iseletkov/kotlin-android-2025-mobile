package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_work_type_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.psu.mobile.kotlin_android_2025_mobile.repositories.CRepositoryWorkTypeWithResources

class CViewModelFactoryWorkTypeResources(
    private val repository: CRepositoryWorkTypeWithResources
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CPageWorkTypeDetailViewModel::class.java)) {
            return CPageWorkTypeDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
    }
}