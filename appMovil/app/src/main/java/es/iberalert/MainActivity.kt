@file:OptIn(ExperimentalMaterial3Api::class)
package es.iberalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import java.text.SimpleDateFormat
import java.util.*

// --- DATA MODELS ---
data class User(val username: String, val role: String, val subscriptions: MutableList<String>)
data class Incident(
    val id: Long, val ciudad: String, val titulo: String, val nivel: String, 
    val descripcion: String, var estado: String, val recomendacion: String, val fecha: String
)
data class Report(
    val id: Long, val autor: String, val titulo: String, val inicio: String, 
    val fin: String, val tipo: String, val ciudad: String, 
    val stats: Map<String, Int>, val desc: String
)

// --- VIEWMODEL (Global State) ---
class AppViewModel : ViewModel() {
    val users = listOf(
        User("admin", "autoridad", mutableListOf("Madrid, Madrid", "Sevilla, Andalucía", "Cádiz, Andalucía")),
        User("user", "usuario", mutableListOf("Barcelona, Cataluña"))
    )
    val allCities = listOf(
        "A Coruña, Galicia", "Albacete, Castilla-La Mancha", "Alicante, Comunidad Valenciana", "Almería, Andalucía",
        "Ávila, Castilla y León", "Badajoz, Extremadura", "Barcelona, Cataluña", "Bilbao, País Vasco",
        "Burgos, Castilla y León", "Cáceres, Extremadura", "Cádiz, Andalucía", "Castellón de la Plana, Comunidad Valenciana",
        "Madrid, Madrid", "Sevilla, Andalucía", "Valencia, Comunidad Valenciana"
    ).sorted()
    
    var currentUser by mutableStateOf<User?>(null)
    var language by mutableStateOf("es")

    val incidents = mutableStateListOf(
        Incident(1, "Madrid, Madrid", "Tornado", "Emergencia", "Tornado detectado por las afueras de la ciudad, fuertes vientos arrancando árboles.", "Activa", "Busque refugio subterráneo.", "09-04-2026"),
        Incident(2, "Cádiz, Andalucía", "Inundaciones", "Emergencia", "Inundaciones graves provocadas por lluvias torrenciales.", "Activa", "Evite salir de casa.", "09-04-2026"),
        Incident(3, "Cádiz, Andalucía", "Precaución Costera", "Alerta", "Marea alta inusual y viento moderado.", "Activa", "Evite pasear por el litoral.", "08-04-2026"),
        Incident(4, "Sevilla, Andalucía", "Lluvias torrenciales", "Emergencia", "Fuertes precipitaciones esperadas.", "Activa", "Conduzca con precaución.", "08-04-2026")
    )
    
    val reports = mutableStateListOf(
        Report(1, "admin", "IF-0001", "2026-04-01", "2026-04-07", "Volumen de Alertas", "Madrid, Madrid", mapOf("red" to 45, "yellow" to 55), "Análisis del volumen total de incidencias."),
        Report(2, "admin", "IF-0002", "2026-03-15", "2026-03-30", "Resumen de Daños", "Cádiz, Andalucía", mapOf("red" to 80, "yellow" to 20), "Los daños infraestructurales se estiman en 12.5M de euros.")
    )

    fun login(u: String, p: String): Boolean {
        val user = users.find { it.username == u && p == it.username }
        if (user != null) {
            currentUser = user
            return true
        }
        return false
    }
    
    fun toggleSubscription(city: String) {
        currentUser?.let { user ->
            if (user.subscriptions.contains(city)) {
                user.subscriptions.remove(city)
            } else {
                user.subscriptions.add(city)
            }
        }
    }
}

// --- THEME ---
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1E3A8A),
    onPrimary = Color.White,
    secondary = Color(0xFFF97316),
    onSecondary = Color.White,
    background = Color(0xFFF3F4F6),
    surface = Color.White,
    onSurface = Color(0xFF1F2937)
)

// --- ACTIVITY ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = LightColorScheme) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    IberAlertApp()
                }
            }
        }
    }
}

// --- APP NAVIGATION ---
@Composable
fun IberAlertApp() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, viewModel) }
        composable("clave") { ClaveLoginScreen(navController, viewModel) }
        composable("main") { MainScreen(navController, viewModel) }
        composable("location_detail/{city}") { backStackEntry -> 
            LocationDetailScreen(navController, viewModel, backStackEntry.arguments?.getString("city") ?: "") 
        }
        composable("report_detail/{id}") { backStackEntry -> 
            ReportDetailScreen(navController, viewModel, backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0) 
        }
        composable("incident_form") { IncidentFormScreen(navController, viewModel) }
    }
}

// --- LOGIN SCREENS ---
@Composable
fun LoginScreen(navController: NavController, viewModel: AppViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("IberAlert", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(48.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.login_title), fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { navController.navigate("clave") },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.login_btn_clave), fontSize = 16.sp)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Text(stringResource(R.string.login_opt_1), color = Color.Gray)
                Text(stringResource(R.string.login_opt_2), color = Color.Gray)
            }
        }
    }
}

@Composable
fun ClaveLoginScreen(navController: NavController, viewModel: AppViewModel) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(stringResource(R.string.clave_title), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(stringResource(R.string.clave_desc), color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = user,
                    onValueChange = { user = it },
                    label = { Text(stringResource(R.string.clave_user)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text(stringResource(R.string.clave_pass)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (error) {
                    Text("Credenciales inválidas", color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text(stringResource(R.string.btn_cancel))
                    }
                    Button(onClick = {
                        if (viewModel.login(user.lowercase().trim(), pass.lowercase().trim())) {
                            navController.navigate("main") { popUpTo("login") { inclusive = true } }
                        } else {
                            error = true
                        }
                    }) {
                        Text(stringResource(R.string.btn_access))
                    }
                }
            }
        }
    }
}

// --- MAIN SCREEN WITH BOTTOM NAV ---
@Composable
fun MainScreen(navController: NavController, viewModel: AppViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("IberAlert", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = "Search") }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Subs") },
                    label = { Text(stringResource(R.string.nav_subs)) },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(stringResource(R.string.nav_home)) },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                if (viewModel.currentUser?.role == "autoridad") {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Edit, contentDescription = "Incidencias") },
                        label = { Text(stringResource(R.string.nav_incidents)) },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Assessment, contentDescription = "Informes") },
                        label = { Text(stringResource(R.string.nav_reports)) },
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> SubscriptionsScreen(navController, viewModel)
                1 -> DashboardScreen(navController, viewModel)
                2 -> IncidentsManageScreen(navController, viewModel)
                3 -> ReportsScreen(navController, viewModel)
            }
        }
    }
}

@Composable
fun DashboardScreen(navController: NavController, viewModel: AppViewModel) {
    val mySubs = viewModel.currentUser?.subscriptions ?: listOf()
    val activeIncidents = viewModel.incidents.filter { it.estado == "Activa" && mySubs.contains(it.ciudad) }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Incidencias Recientes", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        
        if (activeIncidents.isEmpty()) {
            Text("No hay incidencias activas en tus zonas suscritas.", color = Color.Gray)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(activeIncidents) { inc ->
                    IncidentCard(inc)
                }
            }
        }
    }
}

@Composable
fun SubscriptionsScreen(navController: NavController, viewModel: AppViewModel) {
    val mySubs = viewModel.currentUser?.subscriptions ?: mutableListOf()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis Zonas Suscritas", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        
        if (mySubs.isEmpty()) {
            Text("No estás suscrito a ninguna zona todavía.", color = Color.Gray)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(mySubs) { city ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { navController.navigate("location_detail/$city") },
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(city, fontSize = 16.sp)
                            Icon(Icons.Default.ArrowForward, contentDescription = "Go", tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationDetailScreen(navController: NavController, viewModel: AppViewModel, city: String) {
    val incidents = viewModel.incidents.filter { it.ciudad == city && it.estado == "Activa" }
    var isSubbed by remember { mutableStateOf(viewModel.currentUser?.subscriptions?.contains(city) == true) }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(city, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Button(onClick = { 
                viewModel.toggleSubscription(city)
                isSubbed = !isSubbed
            }) {
                Text(if (isSubbed) "Desuscribirse" else "Suscribirse")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Incidencias en esta ubicación", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        
        if (incidents.isEmpty()) {
            Text("No hay incidencias registradas.", color = Color.Gray)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(incidents) { inc ->
                    IncidentCard(inc)
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        TextButton(onClick = { navController.popBackStack() }) { Text("Volver") }
    }
}

@Composable
fun IncidentCard(inc: Incident) {
    val bgColor = if (inc.nivel == "Emergencia") Color(0xFFFFF3E0) else Color(0xFFFFFDE7)
    val iconColor = if (inc.nivel == "Emergencia") Color(0xFFF97316) else Color(0xFFEAB308)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.Warning, contentDescription = "Warning", tint = iconColor)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("${inc.ciudad}. Nivel ${inc.nivel}: ${inc.titulo}", fontWeight = FontWeight.Bold)
                Text(inc.descripcion, fontSize = 14.sp)
            }
        }
    }
}

// --- ADMIN REPORTS SCREEN ---
@Composable
fun ReportsScreen(navController: NavController, viewModel: AppViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Lista de Informes", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(viewModel.reports) { rep ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("${rep.titulo} - ${rep.ciudad}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("Autor: ${rep.autor} (${rep.tipo})", fontSize = 14.sp, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = { navController.navigate("report_detail/${rep.id}") }) {
                                Icon(Icons.Default.Visibility, contentDescription = "Ver")
                            }
                            IconButton(onClick = { viewModel.reports.remove(rep) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportDetailScreen(navController: NavController, viewModel: AppViewModel, id: Long) {
    val rep = viewModel.reports.find { it.id == id }
    if (rep == null) {
        Text("Informe no encontrado")
        return
    }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("${rep.titulo}: ${rep.tipo}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("${rep.ciudad} | Periodo: ${rep.inicio} a ${rep.fin}", color = Color.White, fontSize = 14.sp)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("Detalles del Análisis", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(rep.desc)
        Spacer(modifier = Modifier.height(24.dp))
        
        // Simular gráfico de pastel con una barra de progreso
        Text("Estadísticas de Nivel", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = (rep.stats["red"]?.toFloat() ?: 0f) / 100f,
            modifier = Modifier.fillMaxWidth().height(24.dp),
            color = Color(0xFFF97316),
            trackColor = Color(0xFFEAB308)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${rep.stats["red"]}% Emergencia", color = Color(0xFFF97316), fontWeight = FontWeight.Bold)
            Text("${rep.stats["yellow"]}% Alerta", color = Color(0xFFEAB308), fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { navController.popBackStack() }) { Text("Volver") }
    }
}

// --- ADMIN INCIDENTS MANAGE SCREEN ---
@Composable
fun IncidentsManageScreen(navController: NavController, viewModel: AppViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Gestión de Incidencias", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(viewModel.incidents) { inc ->
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("${inc.ciudad} - ${inc.titulo} (${inc.estado})", fontWeight = FontWeight.Bold)
                        Text("Fecha: ${inc.fecha}", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = { navController.navigate("incident_form") }) {
                                Icon(Icons.Default.Add, contentDescription = "Crear")
                            }
                            IconButton(onClick = { inc.estado = "Desestimada" }) {
                                Icon(Icons.Default.Archive, contentDescription = "Archivar")
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        FloatingActionButton(onClick = { navController.navigate("incident_form") }) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun IncidentFormScreen(navController: NavController, viewModel: AppViewModel) {
    var titulo by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Crear Incidencia", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = titulo, onValueChange = { titulo = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
        
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            TextButton(onClick = { navController.popBackStack() }) { Text("Cancelar") }
            Button(onClick = { 
                viewModel.incidents.add(Incident(System.currentTimeMillis(), "Madrid, Madrid", titulo, "Emergencia", desc, "Activa", "N/A", "10-04-2026"))
                navController.popBackStack()
            }) { Text("Guardar") }
        }
    }
}
