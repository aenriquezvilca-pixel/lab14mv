package com.enriquez.widgetlab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val GoRojo = Color(0xFFE53935)
val GoFondo = Color(0xFFFFF3EF)
val GoTexto = Color(0xFF2B2B2B)

/* ---------- Vista 1: Inicio de sesion / Crear cuenta ---------- */
@Composable
fun LoginScreen(onEntrar: () -> Unit) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().background(GoFondo)) {
        Column(
            modifier = Modifier.fillMaxWidth().background(GoRojo).padding(vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("\uD83D\uDE8C", fontSize = 40.sp)
            Text("GoWayki", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("Muevete por Arequipa sin perderte", color = Color.White, fontSize = 12.sp)
        }
        Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Crear cuenta", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = GoTexto)
            OutlinedTextField(
                value = correo, onValueChange = { correo = it },
                label = { Text("Correo") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = clave, onValueChange = { clave = it },
                label = { Text("Contrasena") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            if (error.isNotEmpty()) Text(error, color = GoRojo, fontSize = 12.sp)
            Button(
                onClick = {
                    if (correo.contains("@") && clave.length >= 6) {
                        GoWaykiState.correo = correo
                        GoWaykiState.sesionIniciada = true
                        onEntrar()
                    } else error = "Correo valido y contrasena de 6+ caracteres"
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoRojo),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Crear mi cuenta") }
            TextButton(
                onClick = {
                    if (correo.contains("@") && clave.length >= 6) {
                        GoWaykiState.correo = correo
                        GoWaykiState.sesionIniciada = true
                        onEntrar()
                    } else error = "Escribe tu correo y contrasena para iniciar sesion"
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Ya tengo cuenta, iniciar sesion", color = GoRojo) }
            OutlinedButton(
                onClick = {
                    GoWaykiState.correo = "invitado"
                    GoWaykiState.sesionIniciada = true
                    onEntrar()
                },
                border = BorderStroke(1.dp, GoRojo),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Entrar como invitado", color = GoRojo) }
        }
    }
}

/* ---------- Vista 2 y 3: Lista de rutas / Busqueda por distrito ---------- */
@Composable
fun RutasScreen(enfocarBusqueda: Boolean, onRuta: (Ruta) -> Unit) {
    var consulta by remember { mutableStateOf("") }
    val foco = remember { FocusRequester() }
    LaunchedEffect(enfocarBusqueda) { if (enfocarBusqueda) foco.requestFocus() }

    val filtradas = GoWaykiData.rutas.filter { r ->
        val q = consulta.trim().lowercase()
        q.isEmpty() || r.nombre.lowercase().contains(q) ||
            r.origen.lowercase().contains(q) || r.destino.lowercase().contains(q) ||
            r.distritos.any { it.lowercase().contains(q) }
    }

    Column(Modifier.fillMaxSize().background(GoFondo).padding(16.dp)) {
        Text(
            "Hola, ${GoWaykiState.correo.substringBefore("@").ifEmpty { "viajero" }} \uD83D\uDC4B",
            fontSize = 12.sp, color = Color.Gray
        )
        Text("\u00BFA donde vas hoy?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = GoTexto)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = consulta, onValueChange = { consulta = it },
            placeholder = { Text("Busca una ruta, distrito o paradero") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().focusRequester(foco)
        )
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(filtradas) { ruta -> RutaCard(ruta, onRuta) }
        }
    }
}

@Composable
fun RutaCard(ruta: Ruta, onRuta: (Ruta) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onRuta(ruta) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(36.dp).background(ruta.color, CircleShape),
                contentAlignment = Alignment.Center
            ) { Text(ruta.inicial, color = Color.White, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(ruta.nombre, fontWeight = FontWeight.Bold, color = GoTexto)
                Text("${ruta.origen} \u2192 ${ruta.destino}", fontSize = 11.sp, color = Color.Gray)
                Text(
                    "${ruta.minutos} min \u00B7 ${ruta.precio} \u00B7 ${ruta.paraderos.size} paradas",
                    fontSize = 11.sp, color = ruta.color
                )
            }
            Text(
                if (GoWaykiState.favoritas.contains(ruta.id)) "\u2605" else "\u2606",
                fontSize = 20.sp, color = GoRojo
            )
        }
    }
}

/* ---------- Vista 4: Detalle de ruta con paraderos ---------- */
@Composable
fun DetalleRutaScreen(ruta: Ruta, onAtras: () -> Unit) {
    val esFavorita = GoWaykiState.favoritas.contains(ruta.id)
    Column(Modifier.fillMaxSize().background(GoFondo)) {
        Column(Modifier.fillMaxWidth().background(ruta.color).padding(16.dp)) {
            Text(
                "\u2190", color = Color.White, fontSize = 22.sp,
                modifier = Modifier.clickable { onAtras() }
            )
            Text(ruta.nombre, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("${ruta.origen} \u2192 ${ruta.destino}", color = Color.White, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "${ruta.minutos} min  \u00B7  ${ruta.precio}  \u00B7  ${ruta.paraderos.size} paradas",
                color = Color.White, fontSize = 12.sp
            )
        }
        Column(Modifier.padding(16.dp)) {
            Text(ruta.descripcion, color = GoTexto, fontSize = 13.sp)
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { GoWaykiState.alternarFavorita(ruta.id) },
                border = BorderStroke(1.dp, GoRojo),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (esFavorita) "\u2605 Quitar de mis favoritas" else "\u2606 Guardar en mis favoritas",
                    color = GoRojo
                )
            }
            Spacer(Modifier.height(16.dp))
            Text("Paraderos", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = GoTexto)
            Spacer(Modifier.height(8.dp))
            ruta.paraderos.forEachIndexed { i, p ->
                Row(Modifier.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(12.dp).background(ruta.color, CircleShape))
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(p.nombre, fontWeight = FontWeight.SemiBold, color = GoTexto)
                        Text("Parada ${i + 1} \u00B7 ${p.coords}", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

/* ---------- Vista 5: Mis rutas favoritas ---------- */
@Composable
fun FavoritasScreen(onRuta: (Ruta) -> Unit) {
    val favoritas = GoWaykiData.rutas.filter { GoWaykiState.favoritas.contains(it.id) }
    Column(Modifier.fillMaxSize().background(GoFondo).padding(16.dp)) {
        Text("Mis rutas favoritas", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = GoTexto)
        Text("Se guardan en tu cuenta de GoWayki", fontSize = 12.sp, color = Color.Gray)
        Spacer(Modifier.height(12.dp))
        if (favoritas.isEmpty()) {
            Text("Aun no tienes favoritas. Toca \u2606 en el detalle de una ruta.", color = Color.Gray)
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(favoritas) { ruta ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { onRuta(ruta) },
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        Modifier.padding(14.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("\u2605 ${ruta.nombre}", fontWeight = FontWeight.SemiBold, color = GoTexto)
                        TextButton(onClick = { GoWaykiState.alternarFavorita(ruta.id) }) {
                            Text("Quitar", color = GoRojo)
                        }
                    }
                }
            }
        }
    }
}

/* ---------- Vista 6: Mi perfil ---------- */
@Composable
fun PerfilScreen(onCerrarSesion: () -> Unit) {
    Column(Modifier.fillMaxSize().background(GoFondo).padding(16.dp)) {
        Text("Mi perfil", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = GoTexto)
        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {
                Text(
                    GoWaykiState.correo.ifEmpty { "Sin sesion" },
                    fontWeight = FontWeight.Bold, color = GoTexto
                )
                Text("Sesion iniciada con correo y contrasena", fontSize = 11.sp, color = Color.Gray)
                Spacer(Modifier.height(8.dp))
                Text("Rutas favoritas: ${GoWaykiState.favoritas.size}", color = GoTexto)
            }
        }
        Spacer(Modifier.height(16.dp))
        OutlinedButton(
            onClick = { GoWaykiState.cerrarSesion(); onCerrarSesion() },
            border = BorderStroke(1.dp, GoRojo),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Cerrar sesion", color = GoRojo) }
    }
}

/* ---------- Barra inferior ---------- */
@Composable
fun GoWaykiBottomBar(actual: String, onIr: (String) -> Unit) {
    val colores = NavigationBarItemDefaults.colors(
        selectedIconColor = GoRojo, selectedTextColor = GoRojo,
        indicatorColor = Color(0xFFFFD9D6)
    )
    NavigationBar(containerColor = GoFondo) {
        NavigationBarItem(
            selected = actual == "rutas" || actual == "buscar" || actual == "detalle",
            onClick = { onIr("rutas") },
            icon = { Text("\uD83D\uDE8C") }, label = { Text("Rutas") }, colors = colores
        )
        NavigationBarItem(
            selected = actual == "favoritas", onClick = { onIr("favoritas") },
            icon = { Text("\u2605") }, label = { Text("Favoritas") }, colors = colores
        )
        NavigationBarItem(
            selected = actual == "perfil", onClick = { onIr("perfil") },
            icon = { Text("\uD83D\uDC64") }, label = { Text("Perfil") }, colors = colores
        )
    }
}
