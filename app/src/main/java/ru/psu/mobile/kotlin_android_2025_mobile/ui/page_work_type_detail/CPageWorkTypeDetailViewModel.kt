package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_work_type_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.psu.mobile.kotlin_android_2025_mobile.model.CResource
import ru.psu.mobile.kotlin_android_2025_mobile.model.CWorkTypeWithResources
import ru.psu.mobile.kotlin_android_2025_mobile.repositories.CRepositoryWorkTypeWithResources


class CPageWorkTypeDetailViewModel(
    private val repository: CRepositoryWorkTypeWithResources
) : ViewModel() {

    private val _workTypeWithResources = MutableStateFlow<CWorkTypeWithResources?>(null)
    val workTypeWithResources: StateFlow<CWorkTypeWithResources?> = _workTypeWithResources.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

//    init {
//        viewModelScope.launch {
//            repository.addResourceToWorkType(
//                "0da44a66-1478-42bd-b157-149bcfa264d7",
//                CResource(
//                    1,
//                    "",
//                    "Средний разряд работы 3.8",
//                    "чел.-ч",
//                    1.54,
//                    "1-100-38"
//                )
//            )
//            repository.addResourceToWorkType(
//                "0da44a66-1478-42bd-b157-149bcfa264d7",
//                CResource(
//                    2,
//                    "",
//                    "Бульдозеры, мощность 79 кВт (108 л.с.)",
//                    "маш.-ч",
//                    1.6,
//                    "91.01.01-035"
//                )
//            )
//        }
//    }
    fun setWorkTypeGuid(guid : String)
    {
        loadWorkType(guid)
    }

    fun loadWorkType(guid : String) {
        viewModelScope.launch {
            repository.getWorkTypeWithResources(guid)
                .collect { workTypeWithResources ->
                    _workTypeWithResources.value = workTypeWithResources

                    _isLoading.value = false
                }

        }
    }


//    fun addSampleResource() {
//        val workTypeGuid = _selectedWorkType.value?.guid ?: return
//
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val sampleResource = CResource(
//                    workTypeGuid = workTypeGuid,
//                    name = "Экскаватор одноковшовый дизельный",
//                    unitName = "маш.-ч",
//                    value = 2.5,
//                    resourceType = "machinery",
//                    resourceCategory = "excavator",
//                    cipher = "91.01.05-085",
//                    specifications = "Объем ковша 0.65 м3",
//                    sortOrder = _resources.value.size
//                )
//                repository.addResourceToWorkType(workTypeGuid, sampleResource)
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

//    fun deleteAllResources() {
//        val workTypeGuid = _selectedWorkType.value?.guid ?: return
//
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                repository.deleteWorkTypeResources(workTypeGuid)
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

//    fun deleteResource(resource: CResource) {
//        viewModelScope.launch {
//            repository.deleteResource(resource)
//        }
//    }
}