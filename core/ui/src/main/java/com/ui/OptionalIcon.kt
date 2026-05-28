package com.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.AirlineSeatReclineNormal
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

fun optionalIcon(name: String): ImageVector {
    val key = name.lowercase()
    return when {
        "teto" in key -> Icons.Filled.WbSunny
        "banco" in key -> Icons.Filled.AirlineSeatReclineNormal
        "câmera" in key || "camera" in key -> Icons.Filled.Camera
        "sensor" in key || "estacion" in key -> Icons.Filled.Sensors
        "gps" in key || "navega" in key -> Icons.Filled.Navigation
        "ar " in key || key.startsWith("ar") || "climatiz" in key -> Icons.Filled.AcUnit
        "som" in key || "audio" in key || "áudio" in key -> Icons.Filled.MusicNote
        "bluetooth" in key -> Icons.Filled.Bluetooth
        "combust" in key || "gasolina" in key || "etanol" in key || "flex" in key -> Icons.Filled.LocalGasStation
        "direç" in key || "dire" in key -> Icons.Filled.Settings
        else -> Icons.Filled.CheckCircle
    }
}
