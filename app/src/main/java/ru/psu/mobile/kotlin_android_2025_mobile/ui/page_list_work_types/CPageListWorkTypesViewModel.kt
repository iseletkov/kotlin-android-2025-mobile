package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_list_work_types

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.psu.mobile.kotlin_android_2025_mobile.model.CWorkType


// ViewModel для управления списком сметных норм
class CPageListWorkTypesViewModel : ViewModel() {
    private val _workTypes = MutableStateFlow(
        Pair(
            listOf(
                CWorkType(
                    name = "Устройство фундаментов железобетонных",
                    code = "01-01-001-01"
                ),
                CWorkType(
                    name = "Кладка стен кирпичных",
                    code = "02-01-015-04"
                ),
                CWorkType(
                    name = "Устройство кровли из металлочерепицы",
                    code = "03-01-042-02"
                ),
                CWorkType(
                    name = "Штукатурка стен цементным раствором",
                    code = "04-01-008-01"
                ),
                CWorkType(
                    name = "Устройство стяжки пола",
                    code = "05-01-023-03"
                )
            ),
            "Created"
        )
    )
    val workTypes: StateFlow<Pair<List<CWorkType>, String>> = _workTypes.asStateFlow()


    fun addWorkType() {
        _workTypes.update { currentWorkTypesInfo ->
            val newNumber = currentWorkTypesInfo.first.size + 1
            Pair(
                currentWorkTypesInfo.first + CWorkType(
                    name = "Новая сметная норма $newNumber",
                    code = "XX-XX-XXX-$newNumber"
                ),
                "Item added"
            )
        }

    }
    fun deleteWorkType(workType: CWorkType) {
        _workTypes.update { currentWorkTypesInfo ->
            Pair(
                currentWorkTypesInfo.first.filter { it.guid != workType.guid },
                "Item deleted"
            )
        }
    }


    fun clearWorkTypes() {
        _workTypes.update {
            Pair(
                emptyList(),
                "Created"
            )
        }
    }
}