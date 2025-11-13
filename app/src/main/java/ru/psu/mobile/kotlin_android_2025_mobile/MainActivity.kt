package ru.psu.mobile.kotlin_android_2025_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.psu.mobile.kotlin_android_2025_mobile.ui.theme.Kotlinandroid2025mobileTheme
import kotlin.String

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kotlinandroid2025mobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CPageCalculator()
                }


            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CPageCalculator()
{
    Scaffold(
        topBar = {
//            TopAppBar(
//                colors = topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                ),
//                title = {
//                    Text("Калькулятор")
//                }
//            )

            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Калькулятор",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                },
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },

    ) { innerPadding ->
        Calculator(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun CCalculatorInputTextField(
    label : String,
    state : MutableState<String>,
    roundedShape : RoundedCornerShape
)
{
    OutlinedTextField(
        value = state.value,
        onValueChange = { newValue -> state.value = newValue },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth(),
        shape = roundedShape,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.Green,
            focusedBorderColor = Color.Red,
            focusedLabelColor = Color.Blue
        )
    )
}

@Composable
fun CCalculatorButton(
    description : String,
    roundedShape : RoundedCornerShape,
    modifier: Modifier = Modifier, // Add modifier parameter
    iconId : Int,
    onClick : () -> Unit
)
{
    Button(
        onClick = { onClick() },
        modifier = modifier
            .height(56.dp),
        shape = roundedShape,

        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Cyan
            //containerColor = Color.hsv(0.5f, 0.1f, 0.7f )
        )
    ) {

        Icon(
            painter = painterResource(iconId),
            contentDescription = description,
            tint = Color.Red
        )
    }
}


@Composable
fun Calculator(modifier: Modifier = Modifier) {
    var operand1 = remember { mutableStateOf("") }
    var operand2 = remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val roundedShape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
//                .fillMaxWidth(0.8f) //Будет занимать 80% ширины родителя.
                .widthIn(max = 500.dp) //Максимальное ограничение по ширине основной части калькулятора
                .padding(24.dp),
            //horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CCalculatorInputTextField(
                "Число 1",
                operand1,
                roundedShape
            )
//            Spacer(modifier = Modifier.height(8.dp))
            CCalculatorInputTextField(
                "Число 2",
                operand2,
                roundedShape
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Кнопка сложения
                CCalculatorButton(
                    "Сложить",
                    roundedShape,
                    Modifier.weight(1f),
                    R.drawable.icon_outline_add_24
                ) {
                    result = performOperation(
                        operand1.value,
                        operand2.value,
                        Operation.ADD
                    )
                }
                // Кнопка сложения
                CCalculatorButton(
                    "Вычесть",
                    roundedShape,
                    Modifier.weight(1f),
                    R.drawable.icon_outline_minus_24
                ) {
                    result = performOperation(
                        operand1.value,
                        operand2.value,
                        Operation.SUBTRACT
                    )
                }
                // Кнопка сложения
                CCalculatorButton(
                    "Умножить",
                    roundedShape,
                    Modifier.weight(1f),
                    R.drawable.icon_outline_multiply_24
                ) {
                    result = performOperation(
                        operand1.value,
                        operand2.value,
                        Operation.MULTIPLY
                    )
                }
                // Кнопка сложения
                CCalculatorButton(
                    "Поделить",
                    roundedShape,
                    Modifier.weight(1f),
                    R.drawable.icon_division
                ) {
                    result = performOperation(
                        operand1.value,
                        operand2.value,
                        Operation.DIVIDE
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = roundedShape
                    )
                    .border(1.dp, MaterialTheme.colorScheme.outline, roundedShape)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = if (result.isNotEmpty()) "Результат: $result" else "Результат",
                    fontSize = 18.sp,
                    color = if (result.isNotEmpty()) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}

enum class Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE
}

fun performOperation(
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
                Operation.ADD -> (num1 + num2).toString()
                Operation.SUBTRACT -> (num1 - num2).toString()
                Operation.MULTIPLY -> (num1 * num2).toString()
                Operation.DIVIDE -> {
                    if (num2 == 0.0) {
                        "Ошибка: деление на 0"
                    } else {
                        (num1 / num2).toString()
                    }
                }
            }
        }
    } catch (e: Exception) {
        "Ошибка вычисления"
    }
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview1() {
    Kotlinandroid2025mobileTheme {
        Calculator()
    }
}