package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map


enum class Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}

class CPageCalculatorViewModel : ViewModel()
{
//    var operand1 = remember { mutableStateOf("100") }
//    var operand2 = remember { mutableStateOf("200") }
//    var result by remember { mutableStateOf("л лжффж") }

    private val _operand1 = MutableStateFlow("")
    val operand1: StateFlow<String> = _operand1.asStateFlow()

    private val _operand2 = MutableStateFlow("")
    val operand2: StateFlow<String> = _operand2.asStateFlow()

    private val _result = MutableStateFlow("")

    val result: StateFlow<String> = _result
        .asStateFlow()




    fun onOperand1Change(
        str : String
    )
    {
        _operand1.value = str
    }

    fun onOperand2Change(
        str : String
    )
    {
        _operand2.value = str
    }

    fun onButtonPlusPress()
    {
        _result.value = performOperation(
            operand1.value,
            operand2.value,
            Operation.ADD
        )
    }
    fun onButtonMinusPress()
    {
        _result.value = performOperation(
            operand1.value,
            operand2.value,
            Operation.SUBTRACT
        )
    }
    fun onButtonMultiplyPress()
    {
        _result.value = performOperation(
            operand1.value,
            operand2.value,
            Operation.MULTIPLY
        )
    }
    fun onButtonDividePress()
    {
        _result.value = performOperation(
            operand1.value,
            operand2.value,
            Operation.DIVIDE
        )
    }



    private fun performOperation(
        operand1: String,
        operand2: String,
        operation: Operation
    ): String {
        return try {
            val num1 = operand1.toDoubleOrNull()
            val num2 = operand2.toDoubleOrNull()

            if (num1 == null || num2 == null) {
                "Ошибка: введите числа"
            } else {
                when (operation) {
                    Operation.ADD -> "Результат: ${num1 + num2}"
                    Operation.SUBTRACT -> "Результат: ${num1 - num2}"
                    Operation.MULTIPLY -> "Результат: ${num1 * num2}"
                    Operation.DIVIDE -> {
                        if (num2 == 0.0) {
                            "Ошибка: деление на 0"
                        } else {
                            "Результат: ${num1 / num2}"
                        }
                    }
                }
            }
        } catch (e: Exception) {
            "Ошибка вычисления"
        }
    }

}