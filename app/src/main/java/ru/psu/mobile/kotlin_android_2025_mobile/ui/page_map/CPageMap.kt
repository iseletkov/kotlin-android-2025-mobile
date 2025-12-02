package ru.psu.mobile.kotlin_android_2025_mobile.ui.page_map

import android.graphics.PointF
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.runtime.image.ImageProvider
import ru.psu.mobile.kotlin_android_2025_mobile.R
import com.yandex.mapkit.map.Map
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.location.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.yandex.mapkit.mapview.MapView
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yandex.mapkit.map.PlacemarkMapObject

@Composable
fun CMapComponent(
    modifier: Modifier = Modifier,
    currentLocation: Point? = null,
    onMapViewCreated: (MapView) -> Unit = {}
) {
    val context = LocalContext.current

    // 1. Создание и управление жизненным циклом MapView
    val mapView = remember {
        MapView(context).apply {
            onMapViewCreated(this)
        }
    }

    // Эффект для обновления положения камеры при изменении currentLocation
//    LaunchedEffect(currentLocation) {
//        if (currentLocation != null) {
//            mapView.mapWindow.map.move(
//                CameraPosition(
//                    currentLocation, // Новые координаты
//                    15.0f, // Уровень приближения
//                    0.0f,  // Азимут
//                    0.0f   // Наклон
//                )
//            )
//        }
//    }

    // 2. Запуск карты при подключении компонента к UI
    LaunchedEffect(Unit) {
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    // 3. Остановка карты при отключении компонента от UI
    DisposableEffect(Unit) {
        onDispose {
            mapView.onStop()
            MapKitFactory.getInstance().onStop()
        }
    }

    // 4. Встраивание нативного MapView в Compose-разметку
    AndroidView(
        factory = { mapView },
        modifier = modifier
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CPageMap() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Состояние для хранения текущего местоположения
    var currentLocation by remember { mutableStateOf<Point?>(null) }
    var isTracking by remember { mutableStateOf(false) }
    var locationAccuracy by remember { mutableStateOf<Float?>(null) }

    // Состояния для управления картой
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var userPlacemark by remember { mutableStateOf<PlacemarkMapObject?>(null) }

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
                locationResult.lastLocation?.let { location ->
                    // Преобразуем Location в Point для Яндекс Карт
                    val point = Point(location.latitude, location.longitude)
                    currentLocation = point
                    locationAccuracy = location.accuracy

                    // Обновляем метку пользователя на карте
                    updateUserLocationOnMap(
                        mapView = mapView,
                        point = point,
                        accuracy = location.accuracy,
                        userPlacemarkRef = { userPlacemark },
                        onPlacemarkUpdated = { newPlacemark ->
                            userPlacemark = newPlacemark
                        }
                    )
                }
            }
        }
    }

    // Функция для запуска отслеживания местоположения
    fun startLocationTracking() {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionState.launchPermissionRequest()
            return
        }

        // Создание LocationRequest с использованием Builder
        val locationRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .setMaxUpdateDelayMillis(15000)
                .setMinUpdateDistanceMeters(10f)
                .build()
        } else {
            LocationRequest.Builder(10000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
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

        // Также получаем последнее известное местоположение
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val point = Point(it.latitude, it.longitude)
                    currentLocation = point
                    locationAccuracy = it.accuracy

                    updateUserLocationOnMap(
                        mapView = mapView,
                        point = point,
                        accuracy = it.accuracy,
                        userPlacemarkRef = { userPlacemark },
                        onPlacemarkUpdated = { newPlacemark ->
                            userPlacemark = newPlacemark
                        }
                    )
                }
            }
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

    // Данные для меток
    val places = rememberSaveable {
        listOf(
            Triple("Дом бобра", 57.991462, 56.164647),
            Triple("Парк Балатово", 57.992151, 56.177641),
            Triple("Театр Театр", 58.008195, 56.215949)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CMapComponent(
            modifier = Modifier.fillMaxSize(),
            currentLocation = currentLocation,
            onMapViewCreated = { mapViewInstance ->
                mapView = mapViewInstance
                val map = mapViewInstance.mapWindow.map

                // Устанавливаем начальную позицию камеры
                val initialPoint = currentLocation ?: Point(57.991239, 56.164615)
                map.move(
                    CameraPosition(
                        initialPoint,
                        15.0f,
                        0.0f,
                        0.0f
                    )
                )

                // Настраиваем стиль для иконок
                val iconStyle = IconStyle().apply {
                    anchor = PointF(0.5f, 1.0f)
                    scale = 2.0f
                    zIndex = 10f
                }

                // Добавляем статические метки
                places.forEach { place ->
                    val pointData = place
                    val imageProvider = ImageProvider.fromResource(context, R.drawable.ic_map_marker)
                    val placemark = map.mapObjects.addPlacemark().apply {
                        setText(pointData.first)
                        geometry = Point(pointData.second, pointData.third)
                        setIcon(imageProvider, iconStyle)
                        userData = place
                    }

                    placemark.addTapListener { mapObject, _ ->
                        val tappedItem = mapObject.userData as? Triple<String, Double, Double>
                        tappedItem?.let {
                            Toast.makeText(
                                context,
                                "Нажат: ${it.first}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        true
                    }
                }

                // Если уже есть текущее местоположение, добавляем метку
                currentLocation?.let { point ->
                    updateUserLocationOnMap(
                        mapView = mapViewInstance,
                        point = point,
                        accuracy = locationAccuracy,
                        userPlacemarkRef = { userPlacemark },
                        onPlacemarkUpdated = { newPlacemark ->
                            userPlacemark = newPlacemark
                        }
                    )
                }
            }
        )
        startLocationTracking()

//        // Кнопка для включения/выключения отслеживания
//        FloatingActionButton(
//            onClick = {
//                if (isTracking) {
//                    stopLocationTracking()
//                    Toast.makeText(context, "Отслеживание остановлено", Toast.LENGTH_SHORT).show()
//                } else {
//                    if (locationPermissionState.status.isGranted) {
//                        startLocationTracking()
//                        Toast.makeText(context, "Отслеживание запущено", Toast.LENGTH_SHORT).show()
//                    } else {
//                        locationPermissionState.launchPermissionRequest()
//                    }
//                }
//            },
//            modifier = Modifier
//                .padding(16.dp)
//                .align(Alignment.BottomEnd),
//            backgroundColor = if (isTracking) Color.Red
//            else Color.Blue,
//        ) {
//            Icon(
//                painter = painterResource(R.drawable.icon_outline_add_24),
//                contentDescription = if (isTracking) "Остановить отслеживание" else "Начать отслеживание",
//                tint = Color.White
//            )
//        }

        // Индикатор статуса отслеживания
        if (isTracking) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd),
//                backgroundColor = Color.Green.copy(alpha = 0.8f)
            ) {
                Text(
                    text = "Отслеживание активно",
                    modifier = Modifier.padding(8.dp),
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// Функция для обновления метки пользователя на карте
private fun updateUserLocationOnMap(
    mapView: MapView?,
    point: Point,
    accuracy: Float?,
    userPlacemarkRef: () -> PlacemarkMapObject?,
    onPlacemarkUpdated: (PlacemarkMapObject?) -> Unit
) {
    mapView?.let { view ->
        val map = view.mapWindow.map

        // Создаем или обновляем метку пользователя
        val currentPlacemark = userPlacemarkRef()

        if (currentPlacemark == null) {
            // Создаем новую метку для пользователя
            val context = view.context
            val userIcon = ImageProvider.fromResource(context, R.drawable.ic_map_marker)

            val newPlacemark = map.mapObjects.addPlacemark().apply {
                geometry = point
                setIcon(userIcon)
                // Настраиваем стиль для метки пользователя
                val iconStyle = IconStyle().apply {
                    anchor = PointF(0.5f, 0.5f) // Центр иконки
                    scale = 1.5f
                    zIndex = 100f // Высокий z-index, чтобы была поверх других меток
                }
                setIcon(userIcon, iconStyle)
            }

            onPlacemarkUpdated(newPlacemark)
        } else {
            // Обновляем существующую метку
            currentPlacemark.geometry = point

            // Можно обновить иконку в зависимости от точности
            accuracy?.let {
                val context = view.context
//                val iconResource = when {
//                    it < 10 -> R.drawable.ic_user_location_high_accuracy
//                    it < 30 -> R.drawable.ic_user_location_medium_accuracy
//                    else -> R.drawable.ic_user_location_low_accuracy
//                }
                val iconResource = R.drawable.ic_map_marker
                // Если у вас есть разные иконки для разной точности
                try {
                    val userIcon = ImageProvider.fromResource(context, iconResource)
                    currentPlacemark.setIcon(userIcon)
                } catch (e: Exception) {
                    // Если ресурс не найден, используем стандартную иконку
                    e.printStackTrace()
                }
            }
        }

        // Также можно добавить круг точности (опционально)
        accuracy?.let {
            if (it > 0) {
                // Создаем или обновляем круг точности
                // Для этого нужно будет хранить отдельную ссылку на CircleMapObject
            }
        }
    }
}