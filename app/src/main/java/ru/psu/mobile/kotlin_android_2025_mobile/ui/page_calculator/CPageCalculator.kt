package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_calculator

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.flow.StateFlow
import ru.psu.mobile.kotlin_android_2025_mobile.ui.theme.Kotlinandroid2025mobileTheme
import kotlin.String
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.psu.mobile.kotlin_android_2025_mobile.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CPageCalculator()
{
    val vm: CPageCalculatorViewModel = viewModel()

//    val operand1 by vm.operand1.collectAsStateWithLifecycle()
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
                        text = "Калькулятор", //+operand1,
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
        Calculator(
            modifier = Modifier.padding(innerPadding),
            vm
        )
    }
}

@Composable
fun CCalculatorInputTextField(
    label : String,
    state : StateFlow<String>,
    roundedShape : RoundedCornerShape,
    onChange : (String) -> Unit
)
{
    OutlinedTextField(
        value = state.collectAsState().value, //Вывод на экран изменений состояния одного из операндов
        onValueChange = { newValue -> onChange(newValue) },
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
fun Calculator(
    modifier: Modifier = Modifier,
    vm: CPageCalculatorViewModel
) {

    val roundedShape = RoundedCornerShape(8.dp)

    val result by vm.result.collectAsStateWithLifecycle()
    val operand1 by vm.operand1.collectAsStateWithLifecycle()

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
                vm.operand1,
                roundedShape
            )
            { newValue ->
                vm.onOperand1Change(newValue)
            }

//            Spacer(modifier = Modifier.height(8.dp))
            CCalculatorInputTextField(
                "Число 2",
                vm.operand2,
                roundedShape,
                onChange = { newValue ->
                    vm.onOperand2Change(newValue)
                }
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
                ) { vm.onButtonPlusPress() }

                // Кнопка сложения
                CCalculatorButton(
                    "Вычесть",
                    roundedShape,
                    Modifier.weight(1f),
                    R.drawable.icon_outline_minus_24
                ) { vm.onButtonMinusPress() }
                // Кнопка сложения
                CCalculatorButton(
                    "Умножить",
                    roundedShape,
                    Modifier.weight(1f),
                    R.drawable.icon_outline_multiply_24
                ) { vm.onButtonMultiplyPress() }
                // Кнопка сложения
                CCalculatorButton(
                    "Поделить",
                    roundedShape,
                    Modifier.weight(1f),
                    R.drawable.icon_division
                ) { vm.onButtonDividePress() }
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
                    text = result,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
//                Text(
//                    text = operand1,
//                    fontSize = 18.sp,
//                    color = MaterialTheme.colorScheme.onSurface,
//                    modifier = Modifier.fillMaxWidth()
//                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview1() {
    val vm: CPageCalculatorViewModel = viewModel()
    Kotlinandroid2025mobileTheme {
        Calculator(vm = vm)
    }
}