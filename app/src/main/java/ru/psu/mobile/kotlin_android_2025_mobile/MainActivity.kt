package ru.psu.mobile.kotlin_android_2025_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.psu.mobile.kotlin_android_2025_mobile.ui.page_calculator.CPageCalculator
import ru.psu.mobile.kotlin_android_2025_mobile.ui.theme.Kotlinandroid2025mobileTheme
import ru.psu.mobile.kotlin_android_2025_mobile.ui.page_list_work_types.CPageListWorkTypes
import ru.psu.mobile.kotlin_android_2025_mobile.ui.page_work_type_detail.CPageWorkTypeDetail

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
                    CMainNavigation()
//                    CPageCalculator()
//                    CPageListWorkTypes()
                }
            }
        }
    }
}

// Определение маршрутов
@Composable
fun CMainNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val currentRoute = navController.currentDestination?.route
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_calculate_24),
                            contentDescription = "Калькулятор"
                        )
                    },
                    label = { Text("Калькулятор") },
                    selected = currentRoute == "Calculator",
                    onClick = {
                        if (currentRoute != "Calculator") {
                            navController.navigate("Calculator")
                        }
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.outline_list_24),
                            contentDescription = "Сметные нормы"
                        )
                    },
                    label = { Text("Сметные нормы") },
                    selected = currentRoute == "WorkTypes",
                    onClick = {
                        if (currentRoute != "WorkTypes") {
                            navController.navigate("WorkTypes")
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "WorkTypes",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Calculator") {
                CPageCalculator()
            }
            composable("WorkTypes") {
                CPageListWorkTypes(navController = navController)
            }
            composable(
                "WorkTypeDetail/{workTypeGuid}",
                arguments = listOf(navArgument("workTypeGuid") { type = NavType.StringType })
            ) { backStackEntry ->
                val workTypeGuid = backStackEntry.arguments?.getString("workTypeGuid") ?: ""
                CPageWorkTypeDetail(
                    workTypeGuid = workTypeGuid,
                    navController = navController
                )
            }
        }
    }
}