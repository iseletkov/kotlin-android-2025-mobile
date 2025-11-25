package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_work_type_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.psu.mobile.kotlin_android_2025_mobile.CApplication
import ru.psu.mobile.kotlin_android_2025_mobile.R
import ru.psu.mobile.kotlin_android_2025_mobile.model.CResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CPageWorkTypeDetail(
    workTypeGuid : String,
    navController : NavController
) {

    val context = LocalContext.current
    val application = context.applicationContext as CApplication
    val repository = application.repositoryWorkTypeWithResources

    val vm: CPageWorkTypeDetailViewModel = viewModel(
        factory = CViewModelFactoryWorkTypeResources(repository)
    )

    //val testGuid = "0da44a66-1478-42bd-b157-149bcfa264d7"
    vm.setWorkTypeGuid(workTypeGuid)

    val workTypeWithResources by vm.workTypeWithResources.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    //var showWorkTypeSelector by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(workTypeWithResources?.workType?.name ?: "Информация по виду работ") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_delete_24),
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Кнопка удалить все ресурсы
                if (workTypeWithResources?.resources?.isNotEmpty() == true) {
                    FloatingActionButton(
                        onClick = { /*vm.deleteAllResources()*/ },
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_delete_24),
                            contentDescription = "Удалить все ресурсы"
                        )
                    }
                }

                // Кнопка добавить ресурс
                FloatingActionButton(
                    onClick = { /*vm.addSampleResource()*/ }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_outline_add_24),
                        contentDescription = "Добавить ресурс"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Блок выбора вида работ
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    // Поле с текущим выбранным видом работ
                    OutlinedTextField(
                        value = workTypeWithResources?.workType?.code ?: "",
                        onValueChange = { /* Запрещаем прямое редактирование */ },
                        label = { Text("Код вида работ") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    // Поле с текущим выбранным видом работ
                    OutlinedTextField(
                        value = workTypeWithResources?.workType?.name ?: "",
                        onValueChange = { /* Запрещаем прямое редактирование */ },
                        label = { Text("Наименование вида работ") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Таблица ресурсов
            Card(
                modifier = Modifier.fillMaxSize(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    else {
                        workTypeWithResources?.resources?.let{ resources ->
                            // Таблица ресурсов
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f)
                            )
                            {
                                // Заголовок таблицы
                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Код ресурса",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(2f)
                                        )
                                        Text(
                                            text = "Наименование элемента",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(4f)
                                        )
                                        Text(
                                            text = "Ед. изм.",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = "Количество",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.End
                                        )
                                        Box(modifier = Modifier.weight(0.5f)) // Для кнопки удаления
                                    }
                                }

                                // Строки с ресурсами
                                items(resources) { resource ->
                                    CResourceTableRow(
                                        resource = resource,
                                        onDelete = { /*vm.deleteResource(resource)*/ }
                                    )
                                }
                            }
                        } ?:run {

                            // Сообщение при отсутствии ресурсов
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Ресурсы не добавлены",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CResourceTableRow(
    resource: CResource,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Код ресурса
            Text(
                text = resource.cipher ?: "-",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(2f),
                color = if (resource.cipher.isNullOrEmpty()) {
                    MaterialTheme.colorScheme.onSurfaceVariant
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )

            Text(
                text = resource.name,
                style = MaterialTheme.typography.bodyMedium
            )

            // Единицы измерения
            Text(
                text = resource.unitName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            // Количество
            Text(
                text = "%.2f".format(resource.value),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )

            // Кнопка удаления
            IconButton(
                onClick = onDelete,
                modifier = Modifier.weight(0.5f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_delete_24),
                    contentDescription = "Удалить ресурс",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}