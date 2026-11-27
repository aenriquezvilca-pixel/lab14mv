package com.enriquez.widgetlab

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

data class Paradero(val nombre: String, val coords: String)

data class Ruta(
    val id: String,
    val inicial: String,
    val nombre: String,
    val origen: String,
    val destino: String,
    val distritos: List<String>,
    val minutos: Int,
    val precio: String,
    val color: Color,
    val descripcion: String,
    val paraderos: List<Paradero>
)

object GoWaykiData {
    val rutas = listOf(
        Ruta(
            id = "arequipa_sachaca", inicial = "A", nombre = "Arequipa - Sachaca",
            origen = "Plaza de Armas", destino = "Mirador de Sachaca",
            distritos = listOf("Arequipa", "Sachaca"),
            minutos = 20, precio = "S/ 1.00", color = Color(0xFFE53935),
            descripcion = "Ruta del centro historico hacia Sachaca.",
            paraderos = listOf(
                Paradero("Plaza de Armas", "-16.3989, -71.5369"),
                Paradero("Puente Grau", "-16.3951, -71.5412"),
                Paradero("Mirador de Sachaca", "-16.4203, -71.5601")
            )
        ),
        Ruta(
            id = "canarios", inicial = "C", nombre = "Canarios S.A.",
            origen = "Mercado Avenino", destino = "TECSUP Arequipa",
            distritos = listOf("Cayma", "Yanahuara"),
            minutos = 25, precio = "S/ 1.00", color = Color(0xFF1E88E5),
            descripcion = "Une el mercado con la zona de Cayma y TECSUP.",
            paraderos = listOf(
                Paradero("Mercado Avenino", "-16.4012, -71.5290"),
                Paradero("Av. Ejercito", "-16.3900, -71.5450"),
                Paradero("TECSUP Arequipa", "-16.4189, -71.5230")
            )
        ),
        Ruta(
            id = "cotum", inicial = "C", nombre = "Cotum B",
            origen = "Plaza de Armas", destino = "Cayma",
            distritos = listOf("Cayma"),
            minutos = 30, precio = "S/ 1.00", color = Color(0xFF43A047),
            descripcion = "Ruta directa del centro hacia Cayma.",
            paraderos = listOf(
                Paradero("Plaza de Armas", "-16.3989, -71.5369"),
                Paradero("Av. Ejercito", "-16.3900, -71.5450"),
                Paradero("Cayma", "-16.3700, -71.5460")
            )
        ),
        Ruta(
            id = "conquistador", inicial = "E", nombre = "El Conquistador",
            origen = "Terminal Terrestre", destino = "Yanahuara",
            distritos = listOf("Yanahuara", "Arequipa"),
            minutos = 40, precio = "S/ 1.20", color = Color(0xFFF59E0B),
            descripcion = "Ruta del Terminal Terrestre hacia Yanahuara, ideal para turistas.",
            paraderos = listOf(
                Paradero("Terminal Terrestre", "-16.4096, -71.5188"),
                Paradero("Av. Ejercito", "-16.3900, -71.5450"),
                Paradero("Yanahuara", "-16.3920, -71.5470")
            )
        ),
        Ruta(
            id = "san_martin", inicial = "S", nombre = "San Martin",
            origen = "Mercado San Camilo", destino = "Mall Aventura",
            distritos = listOf("Cerro Colorado", "Arequipa"),
            minutos = 35, precio = "S/ 1.00", color = Color(0xFF8E24AA),
            descripcion = "Conecta el centro con el Mall Aventura.",
            paraderos = listOf(
                Paradero("Mercado San Camilo", "-16.4040, -71.5330"),
                Paradero("Av. Aviacion", "-16.3850, -71.5500"),
                Paradero("Mall Aventura", "-16.3790, -71.5640")
            )
        )
    )

    fun buscarPorId(id: String?): Ruta? = rutas.firstOrNull { it.id == id }
}

/** Estado simple en memoria: sesion y rutas favoritas. */
object GoWaykiState {
    var correo by mutableStateOf("")
    var sesionIniciada by mutableStateOf(false)
    val favoritas = mutableStateListOf<String>()

    fun alternarFavorita(id: String) {
        if (favoritas.contains(id)) favoritas.remove(id) else favoritas.add(id)
    }

    fun cerrarSesion() {
        sesionIniciada = false
        correo = ""
    }
}
