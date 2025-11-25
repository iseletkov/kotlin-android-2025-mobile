package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_list_work_types

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.psu.mobile.kotlin_android_2025_mobile.model.CWorkType
import ru.psu.mobile.kotlin_android_2025_mobile.repositories.CRepositoryWorkTypes


// ViewModel для управления списком сметных норм
class CPageListWorkTypesViewModel(
    private val repository: CRepositoryWorkTypes
) : ViewModel() {
    private val _workTypes = MutableStateFlow<Pair<List<CWorkType>, String>>(
        Pair(
            emptyList(),
            "Loading"
        )
    )
    val workTypes: StateFlow<Pair<List<CWorkType>, String>> = _workTypes.asStateFlow()

    init {
        loadWorkTypes()
//        initializeDefaultData()
    }
    private fun loadWorkTypes() {
        viewModelScope.launch {
            repository.getAll().collect { workTypesList ->
                _workTypes.value = Pair(workTypesList, "Loaded")
            }

        }
    }

    private fun initializeDefaultData() {
        viewModelScope.launch {
            val currentWorkTypes = repository.getAll()
            if (_workTypes.value.first.isEmpty()) {
                repository.insertWorkType(CWorkType(
                        guid = "0da44a66-1478-42bd-b157-149bcfa264d7",
                        name = "Устройство фундаментов железобетонных",
                        code = "01-01-001-01"
                    )
                )
                repository.insertWorkType(CWorkType(
                        name = "Кладка стен кирпичных",
                        code = "02-01-015-04"
                    )
                )
            }
        }
    }

    fun addWorkType() {
        viewModelScope.launch {
            val newNumber = _workTypes.value.first.size + 1
            val newWorkType = CWorkType(
                name = "Новая сметная норма $newNumber",
                code = "XX-XX-XXX-$newNumber"
            )
            repository.insertWorkType(newWorkType)
//            _workTypes.update { current ->
//                Pair(current.first, "Item added")
//            }
        }

    }
    fun deleteWorkType(workType: CWorkType) {
        viewModelScope.launch {
            repository.deleteWorkType(workType)
//            _workTypes.update { current ->
//                Pair(current.first, "Item deleted")
//            }
        }
    }


    fun clearWorkTypes() {
        viewModelScope.launch {
            repository.clearAllWorkTypes()
//            _workTypes.update { current ->
//                Pair(emptyList(), "Cleared")
//            }
        }
    }
}