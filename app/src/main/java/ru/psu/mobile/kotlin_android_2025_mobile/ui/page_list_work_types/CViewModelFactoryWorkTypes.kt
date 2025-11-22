package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_list_work_types

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.psu.mobile.kotlin_android_2025_mobile.repositories.CRepositoryWorkTypes

class CViewModelFactoryWorkTypes(
    private val repository: CRepositoryWorkTypes
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CPageListWorkTypesViewModel::class.java)) {
            return CPageListWorkTypesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}