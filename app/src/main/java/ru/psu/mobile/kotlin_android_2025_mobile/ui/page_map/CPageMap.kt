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
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import ru.psu.mobile.kotlin_android_2025_mobile.R
import com.yandex.mapkit.map.Map
@Composable
fun CMapComponent(
    modifier: Modifier = Modifier,
    onMapViewCreated: (MapView) -> Unit = {}
) {
    val context = LocalContext.current

    // 1. Создание и управление жизненным циклом MapView
    val mapView = remember {
        MapView(context).apply {
            // Здесь можно произвести первоначальную настройку карты
            onMapViewCreated(this)
        }
    }

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

@Composable
fun CPageMap() {
    val context = LocalContext.current
    var map : Map?
    val data = mutableListOf<Triple<String, Double, Double>>(
        Triple(
            "Дом бобра",
            57.991462, 56.164647
        ),
        Triple(
            "Парк Балатово",
            57.992151, 56.177641
        ),
        Triple(
            "Театр Театр",
            58.008195, 56.215949
        )

    )


    Box(modifier = Modifier.fillMaxSize()) {
        CMapComponent(
            modifier = Modifier.fillMaxSize(),
            onMapViewCreated = { mapView ->
                // Эта лямбда вызывается один раз при создании MapView.
                // Здесь можно получить доступ к объекту карты и настроить его.
                map = mapView.mapWindow.map

                // Пример: переместить камеру к точке
                map.move(
                    CameraPosition(
                        Point(57.991239, 56.164615), // Координаты (широта, долгота)
                        15.0f, // Уровень приближения (zoom)
                        0.0f,  // Азимут (поворот)
                        0.0f   // Наклон камеры
                    )
                )

                // 2. Создайте стиль для иконки (опционально, но желательно)
                val iconStyle = IconStyle().apply {
                    // anchor задает точку привязки иконки к координатам (0.5f, 1.0f = центр нижнего края)
                    anchor = PointF(0.5f, 1.0f)
                    // scale = 1.0f (размер по умолчанию), можно уменьшить (0.5f) или увеличить (2.0f)
                    scale = 2.0f
                    // isFlat = false означает, что иконка будет поворачиваться вместе с картой
                    //isFlat = false
                    // zIndex задает порядок отображения, если меток много
                    zIndex = 10f
                }

                data.forEach { item ->
                    // 1. Создаём локальную константу для текущего элемента
                    val pointData = item

                    // Пример: добавить метку
                    val imageProvider = ImageProvider.fromResource(context, R.drawable.ic_map_marker)
                    val placemark = map.mapObjects.addPlacemark().apply {
                        setText(pointData.first)
                        geometry = Point(pointData.second, pointData.third)
                        setIcon(imageProvider, iconStyle)
                        userData = item // Сохраняем данные
                    }
                    placemark.addTapListener { mapObject, _->
                        val tappedItem = mapObject.userData as? Triple<String, Double, Double>
                        tappedItem?.let {
//                            Toast.makeText(context, "Нажат: ${it.first}", Toast.LENGTH_SHORT).show()

//                            navController.navigate("iteminfo/${tappedItem.id}")
                        }

                        true
                    }
                }
            }
        )
    }
}