package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_list_work_types

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.psu.mobile.kotlin_android_2025_mobile.R
import ru.psu.mobile.kotlin_android_2025_mobile.model.CWorkType
import androidx.compose.ui.platform.LocalConfiguration
import android.content.res.Configuration
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import ru.psu.mobile.kotlin_android_2025_mobile.CApplication
import ru.psu.mobile.kotlin_android_2025_mobile.repositories.CRepositoryWorkTypes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CPageListWorkTypes()
{
    val context = LocalContext.current
    val application = context.applicationContext as CApplication
    val repository = application.repositoryWorkTypes

    val vm: CPageListWorkTypesViewModel = viewModel(
        factory = CViewModelFactoryWorkTypes(repository)
    )
    val workTypesInfo by vm.workTypes.collectAsState()
    val lazyListState = rememberLazyListState()

    // Автоматическая прокрутка только при добавлении новых элементов
    LaunchedEffect(workTypesInfo) {
        if (workTypesInfo.second=="Item added") {
            // Новая прокрутка с анимацией
            lazyListState.animateScrollToItem(workTypesInfo.first.size - 1)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { vm.addWorkType() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_outline_add_24),
                    contentDescription = "Добавить сметную норму"
                )
            }
        }
    ) { paddingValues ->
        if (workTypesInfo.first.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Список сметных норм пуст")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                state = lazyListState
            ) {
                items(workTypesInfo.first) { workType ->
                    CWorkTypeListItem(
                        workType = workType,
                        vm
//                        onDelete = { vm.deleteWorkType(workType) }
                    )
                }
            }
        }
    }
}

@Composable
fun CWorkTypeListItem(
    workType: CWorkType,
    vm: CPageListWorkTypesViewModel,
//    onDelete: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        if (isLandscape) {
            // Горизонтальная ориентация: код, наименование, единицы измерения, кнопка удаления
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Код
                Text(
                    text = workType.code,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.widthIn(min = 80.dp)
                )

                // Наименование (занимает всё доступное пространство)
                Text(
                    text = workType.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                )

                // Единицы измерения
                Text(
                    text = workType.units.ifEmpty { "-" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.widthIn(min = 40.dp),
                    textAlign = TextAlign.End
                )

                // Кнопка удаления
                IconButton(
//                    onClick = onDelete,
                    onClick = {vm.deleteWorkType(workType)},
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_24), // Добавьте свою иконку корзины
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        } else {
            // Вертикальная ориентация: две строки с кнопкой удаления
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Верхняя строка: код и единицы измерения
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = workType.code,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = workType.units.ifEmpty { "-" },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Нижняя строка: наименование во всю ширину
                    Text(
                        text = workType.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                }

                // Кнопка удаления
                IconButton(
//                    onClick = onDelete,
                    onClick = {vm.deleteWorkType(workType)},
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_24), // Добавьте свою иконку корзины
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}