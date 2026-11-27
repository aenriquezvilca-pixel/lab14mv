package com.enriquez.widgetlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.enriquez.widgetlab.ui.theme.WidgetLabTheme

/**
 * Activity unica de GoWayki. Recibe por Intent la vista inicial ("screen")
 * para que cada boton del widget abra una vista distinta.
 */
class GoWaykiActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val inicio = intent.getStringExtra(EXTRA_SCREEN) ?: "login"
        val rutaInicial = intent.getStringExtra(EXTRA_RUTA)

        setContent {
            WidgetLabTheme {
                var pantalla by remember { mutableStateOf(inicio) }
                var rutaId by remember { mutableStateOf(rutaInicial ?: "conquistador") }

                val ruta = GoWaykiData.buscarPorId(rutaId) ?: GoWaykiData.rutas.first()
                val conBarra = pantalla != "login"

                BackHandler(enabled = pantalla == "detalle") { pantalla = "rutas" }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (conBarra) GoWaykiBottomBar(pantalla) { pantalla = it }
                    }
                ) { innerPadding ->
                    val mod = Modifier.padding(innerPadding)
                    androidx.compose.foundation.layout.Box(mod) {
                        when (pantalla) {
                            "login" -> LoginScreen(onEntrar = { pantalla = "rutas" })
                            "rutas" -> RutasScreen(enfocarBusqueda = false) {
                                rutaId = it.id; pantalla = "detalle"
                            }
                            "buscar" -> RutasScreen(enfocarBusqueda = true) {
                                rutaId = it.id; pantalla = "detalle"
                            }
                            "detalle" -> DetalleRutaScreen(ruta) { pantalla = "rutas" }
                            "favoritas" -> FavoritasScreen {
                                rutaId = it.id; pantalla = "detalle"
                            }
                            "perfil" -> PerfilScreen { pantalla = "login" }
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_SCREEN = "screen"
        const val EXTRA_RUTA = "ruta"
    }
}
