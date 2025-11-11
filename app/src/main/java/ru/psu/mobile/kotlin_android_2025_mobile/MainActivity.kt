package ru.psu.mobile.kotlin_android_2025_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.psu.mobile.kotlin_android_2025_mobile.ui.theme.Kotlinandroid2025mobileTheme
import kotlin.String

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kotlinandroid2025mobileTheme {
                pageCalculator()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pageCalculator()
{
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Калькулятор")
                }
            )
        },

    ) { innerPadding ->
        Calculator(modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun MyTextField(
    label : String,
    state : MutableState<String>,
    modifier: Modifier = Modifier
)
{
    TextField(
        value = state.value,
        onValueChange = { newValue -> state.value = newValue },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun Calculator(modifier: Modifier = Modifier) {
    var value1 = remember { mutableStateOf("") }
    var value2 = remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }


    Column(
        modifier = modifier
    ) {
        MyTextField(
            "Число 1",
            value1
        )

        MyTextField(
            "Число 2",
            value2,
            Modifier.padding(top=6.dp)
        )

        Row {
            Button(
                onClick = {
                    result = "${value1.value.toDouble()+value2.value.toDouble()}"
                }
            )
            {
                Text("Плюс")
            }
            Button(
                onClick = {
                    result = "${value1.value.toDouble()-value2.value.toDouble()}"
                }
            )
            {
                Text("Минус")
            }
        }

        Text(
            text = result
        )



    }
//
//    Text(
//        text = "name: $name",
//        modifier = modifier
//    )
}

@Preview(showBackground = true)
@Composable
fun CalculatorPreview1() {
    Kotlinandroid2025mobileTheme {
        Calculator()
    }
}