package com.enriquez.widgetlab

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class GoWaykiWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = GoWaykiWidget()
}

class GoWaykiWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                Contenido()
            }
        }
    }

    @Composable
    private fun Contenido() {
        Column(
            modifier = GlanceModifier.fillMaxSize()
                .background(GlanceTheme.colors.background)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "GoWayki - \u00BFA donde vas hoy?",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
                modifier = GlanceModifier.padding(bottom = 8.dp)
            )
            Row(modifier = GlanceModifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                BotonVista("Rutas", "rutas")
                BotonVista("Buscar", "buscar")
                BotonVista("Detalle", "detalle")
            }
            Row(
                modifier = GlanceModifier.fillMaxWidth().padding(top = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BotonVista("Favoritas", "favoritas")
                BotonVista("Perfil", "perfil")
                BotonVista("Cuenta", "login")
            }
        }
    }

    @Composable
    private fun BotonVista(texto: String, pantalla: String) {
        Button(
            text = texto,
            modifier = GlanceModifier.padding(horizontal = 2.dp),
            onClick = actionStartActivity<GoWaykiActivity>(
                actionParametersOf(PANTALLA to pantalla)
            )
        )
    }

    companion object {
        // El nombre de la clave es el nombre del extra que recibe la Activity.
        val PANTALLA = ActionParameters.Key<String>(GoWaykiActivity.EXTRA_SCREEN)
    }
}
