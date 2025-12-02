package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_map

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.location.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CPageCoordinates() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Состояния для хранения данных о местоположении
    var location by remember { mutableStateOf<Location?>(null) }
    var isTracking by remember { mutableStateOf(false) }
    var lastUpdateTime by remember { mutableStateOf<String?>(null) }

    // Состояния для ошибок
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Запрос разрешения на точное местоположение
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // Инициализация FusedLocationProviderClient
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    // Callback для обновлений местоположения
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {

                    location = it
                    lastUpdateTime = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
                        .format(java.util.Date(it.time))
                    errorMessage = null
                }
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                super.onLocationAvailability(availability)
                if (!availability.isLocationAvailable) {
                    errorMessage = "Местоположение недоступно"
                }
            }
        }
    }

    // Функция для получения последнего известного местоположения
    fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionState.launchPermissionRequest()
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc: Location? ->
                location = loc
                if (loc != null) {
                    lastUpdateTime = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())
                        .format(java.util.Date(loc.time))
                    errorMessage = null
                } else {
                    errorMessage = "Последнее местоположение не найдено"
                }
            }
            .addOnFailureListener { exception ->
                errorMessage = "Ошибка: ${exception.message}"
            }
    }

    // Функция для запуска отслеживания местоположения с использованием Builder
    fun startLocationTracking() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionState.launchPermissionRequest()
            return
        }

        // Создание LocationRequest с использованием Builder (не deprecated)
        val locationRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Для Android 12+ используем новый Builder
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .setMaxUpdateDelayMillis(15000)
                .setMinUpdateDistanceMeters(10f)
                .build()
        } else {
            // Для старых версий используем стандартный Builder
            // Начиная с play-services-location 21.0.0, Builder стал доступен через конструктор
            LocationRequest.Builder(10000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateIntervalMillis(5000)
                .setMaxUpdateDelayMillis(15000)
                .setMinUpdateDistanceMeters(10f)
                .build()
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        isTracking = true
        errorMessage = null
    }

    // Функция для остановки отслеживания
    fun stopLocationTracking() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        isTracking = false
    }

    // Автоматическая остановка отслеживания при выходе из композиции
    DisposableEffect(lifecycleOwner) {
        onDispose {
            stopLocationTracking()
        }
    }

    // UI интерфейс
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Текущие координаты") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Отображение статуса разрешений
            if (!locationPermissionState.status.isGranted) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Требуется разрешение на доступ к местоположению",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { locationPermissionState.launchPermissionRequest() }
                        ) {
                            Text("Запросить разрешение")
                        }
                    }
                }
            }

            // Отображение координат
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Текущие координаты:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (location != null) {
                        val loc = location!!
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("Широта: ${"%.6f".format(loc.latitude)}°")
                            Text("Долгота: ${"%.6f".format(loc.longitude)}°")
                            if (loc.hasAltitude()) {
                                Text("Высота: ${"%.1f".format(loc.altitude)} м")
                            }
                            if (loc.hasAccuracy()) {
                                Text("Точность: ${"%.1f".format(loc.accuracy)} м")
                            }
                            if (loc.hasSpeed() && loc.speed > 0) {
                                Text("Скорость: ${"%.1f".format(loc.speed * 3.6)} км/ч")
                            }
                        }

                        if (lastUpdateTime != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Обновлено: $lastUpdateTime",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    } else {
                        Text(
                            "Координаты не получены",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            // Отображение ошибок
            errorMessage?.let { message ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Кнопки управления
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { getLastKnownLocation() },
                    modifier = Modifier.weight(1f),
                    enabled = locationPermissionState.status.isGranted
                ) {
                    Text("Получить координаты")
                }

                Button(
                    onClick = {
                        if (isTracking) {
                            stopLocationTracking()
                        } else {
                            startLocationTracking()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = locationPermissionState.status.isGranted,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isTracking) MaterialTheme.colorScheme.secondaryContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(if (isTracking) "Остановить" else "Следить")
                }
            }

            // Статус отслеживания
            if (isTracking) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Индикатор активности
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = Color.Green,
                                    shape = CircleShape
                                )
                        )
                        Text(
                            "Отслеживание активно (используется LocationRequest.Builder)",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                // Информация о настройках для ГЛОНАСС
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Используется приоритет высокой точности",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Text(
                            "Вероятно задействуются GPS и ГЛОНАСС спутники",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }
        }
    }
}