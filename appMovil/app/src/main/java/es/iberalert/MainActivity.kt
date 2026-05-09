@file:OptIn(ExperimentalMaterial3Api::class)
package es.iberalert

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import java.util.*

// --- COLORS FROM INTERFACES ---
val IberBlueHeader = Color(0xFFBBDEFB)
val IberOrange = Color(0xFFFFE0B2) // Emergency
val IberYellow = Color(0xFFFFF9C4) // Alert
val IberInfoBlue = Color(0xFFE1F5FE) // Info/Reports

// --- i18n SYSTEM ---
val translations = mapOf(
    "es" to mapOf(
        "login_title" to "Iniciar sesión",
        "login_btn_clave" to "Acceder con Cl@ve",
        "nav_home" to "Inicio",
        "nav_subs" to "Zonas Suscritas",
        "nav_reports" to "Informes",
        "nav_incidents" to "Gestión de Incidencias",
        "nav_control" to "Panel de Control",
        "nav_settings" to "Ajustes Generales",
        "nav_help" to "Ayuda y FAQ",
        "nav_contact" to "Contacto",
        "nav_privacy" to "Política de Privacidad",
        "search_placeholder" to "Buscar ubicaciones...",
        "btn_back" to "Volver",
        "btn_save" to "Guardar",
        "btn_cancel" to "Cancelar",
        "btn_create_incident" to "Crear Incidencia",
        "btn_create_report" to "Crear Informe"
    ),
    "ca" to mapOf(
        "login_title" to "Iniciar sessió",
        "login_btn_clave" to "Accedir amb Cl@ve",
        "nav_home" to "Inici",
        "nav_subs" to "Zones Subscrites",
        "nav_reports" to "Informes",
        "nav_incidents" to "Gestió d'Incidències",
        "nav_control" to "Tauler de Control",
        "nav_settings" to "Ajustos Generals",
        "nav_help" to "Ajuda i FAQ",
        "nav_contact" to "Contacte",
        "nav_privacy" to "Política de Privadesa",
        "search_placeholder" to "Cercar ubicacions...",
        "btn_back" to "Tornar",
        "btn_save" to "Desar",
        "btn_cancel" to "Cancel·lar",
        "btn_create_incident" to "Crear Incidència",
        "btn_create_report" to "Crear Informe"
    ),
    "eu" to mapOf(
        "login_title" to "Hasi saioa",
        "login_btn_clave" to "Sartu Cl@ve bidez",
        "nav_home" to "Hasiera",
        "nav_subs" to "Harpidetutako Eremuak",
        "nav_reports" to "Txostenak",
        "nav_incidents" to "Gorabeheren Kudeaketa",
        "nav_control" to "Kontrol Panela",
        "nav_settings" to "Ezarpen Orokorrak",
        "nav_help" to "Laguntza eta FAQ",
        "nav_contact" to "Kontaktua",
        "nav_privacy" to "Pribatutasun Politika",
        "search_placeholder" to "Bilatu kokapenak...",
        "btn_back" to "Itzuli",
        "btn_save" to "Gorde",
        "btn_cancel" to "Utzi",
        "btn_create_incident" to "Gorabehera Sortu",
        "btn_create_report" to "Txostena Sortu"
    ),
    "gl" to mapOf(
        "login_title" to "Iniciar sesión",
        "login_btn_clave" to "Acceder con Cl@ve",
        "nav_home" to "Inicio",
        "nav_subs" to "Zonas Subscritas",
        "nav_reports" to "Informes",
        "nav_incidents" to "Xestión de Incidencias",
        "nav_control" to "Panel de Control",
        "nav_settings" to "Axustes Xerais",
        "nav_help" to "Axuda e FAQ",
        "nav_contact" to "Contacto",
        "nav_privacy" to "Política de Privacidade",
        "search_placeholder" to "Buscar localizacións...",
        "btn_back" to "Volver",
        "btn_save" to "Gardar",
        "btn_cancel" to "Cancelar",
        "btn_create_incident" to "Crear Incidencia",
        "btn_create_report" to "Crear Informe"
    )
)

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
        "Ceuta, Ceuta", "Ciudad Real, Castilla-La Mancha", "Córdoba, Andalucía", "Cuenca, Castilla-La Mancha",
        "Girona, Cataluña", "Granada, Andalucía", "Guadalajara, Castilla-La Mancha", "Huelva, Andalucía",
        "Huesca, Aragón", "Jaén, Andalucía", "Las Palmas de Gran Canaria, Canarias", "León, Castilla y León",
        "Lleida, Cataluña", "Logroño, La Rioja", "Lugo, Galicia", "Madrid, Madrid", "Málaga, Andalucía",
        "Melilla, Melilla", "Murcia, Región de Murcia", "Ourense, Galicia", "Oviedo, Asturias", "Palencia, Castilla y León",
        "Palma, Islas Baleares", "Pamplona, Navarra", "Pontevedra, Galicia", "Salamanca, Castilla y León",
        "San Sebastián, País Vasco", "Santa Cruz de Tenerife, Canarias", "Santander, Cantabria", "Segovia, Castilla y León",
        "Sevilla, Andalucía", "Soria, Castilla y León", "Tarragona, Cataluña", "Teruel, Aragón", "Toledo, Castilla-La Mancha",
        "Valencia, Comunidad Valenciana", "Valladolid, Castilla y León", "Vitoria-Gasteiz, País Vasco", "Zamora, Castilla y León",
        "Zaragoza, Aragón"
    ).sorted()
    
    var currentUser by mutableStateOf<User?>(null)
    var currentLanguage by mutableStateOf("es")
    var searchQuery by mutableStateOf("")

    val incidents = mutableStateListOf(
        Incident(1, "Madrid, Madrid", "Tornado", "Emergencia", "Tornado detectado por las afueras de la ciudad, fuertes vientos arrancando árboles.", "Activa", "Busque refugio subterráneo o en habitaciones interiores sin ventanas. Aléjese de zonas arboladas.", "09-04-2026"),
        Incident(2, "Cádiz, Andalucía", "Inundaciones", "Emergencia", "Inundaciones graves provocadas por lluvias torrenciales continuadas a lo largo del territorio gaditano.", "Activa", "Evite salir de casa en la medida de lo posible y aléjese de cauces de ríos y zonas bajas.", "09-04-2026"),
        Incident(3, "Cádiz, Andalucía", "Precaución Costera", "Alerta", "Marea alta inusual y viento moderado afectando al paseo marítimo.", "Activa", "Evite pasear por el litoral y amarrar bien las embarcaciones.", "08-04-2026"),
        Incident(4, "Sevilla, Andalucía", "Lluvias torrenciales", "Emergencia", "Fuertes precipitaciones esperadas durante la tarde.", "Activa", "Conduzca con precaución, riesgo de balsas de agua en la vía.", "08-04-2026")
    )
    
    val reports = mutableStateListOf(
        Report(1, "admin", "IF-0001", "2026-04-01", "2026-04-07", "Comp. meteorológicos (Mortalidad)", "Madrid, Madrid", mapOf("red" to 45, "yellow" to 55), "Análisis del volumen total de incidencias durante el periodo. Se ha detectado un incremento del 15%."),
        Report(2, "admin", "IF-0002", "2026-03-15", "2026-03-30", "Comp. agravantes y predictores", "Cádiz, Andalucía", mapOf("red" to 80, "yellow" to 20), "Los daños infraestructurales se estiman en 12.5M de euros tras las últimas inundaciones que afectaron a múltiples tramos."),
        Report(3, "admin", "IF-0003", "2026-04-09", "2026-04-10", "Panel de Control (Dashboard)", "Nacional", mapOf("red" to 30, "yellow" to 70), "Resumen global de indicadores.")
    )

    fun t(key: String): String {
        return translations[currentLanguage]?.get(key) ?: key
    }

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
    primary = Color(0xFF1976D2),
    onPrimary = Color.White,
    background = Color(0xFFF8F9FA),
    surface = Color.White,
    onSurface = Color.Black
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
        composable("subscriptions") { SubscriptionsScreen(navController, viewModel) }
        composable("location_detail/{city}") { backStackEntry -> 
            LocationDetailScreen(navController, viewModel, backStackEntry.arguments?.getString("city") ?: "") 
        }
        composable("incident_detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0
            IncidentDetailScreen(navController, viewModel, id)
        }
        composable("report_detail/{id}") { backStackEntry -> 
            ReportDetailScreen(navController, viewModel, backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0) 
        }
        composable("reports_admin") { AdminReportsScreen(navController, viewModel) }
        composable("incidents_admin") { AdminIncidentsScreen(navController, viewModel) }
        composable("control_panel") { ControlPanelScreen(navController, viewModel) }
        composable("incident_form/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: -1L
            IncidentFormScreen(navController, viewModel, id)
        }
        composable("report_form/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: -1L
            ReportFormScreen(navController, viewModel, id)
        }
        composable("settings") { SettingsScreen(navController, viewModel) }
        composable("help") { HelpScreen(navController, viewModel) }
        composable("contact") { ContactScreen(navController, viewModel) }
        composable("privacy") { PrivacyScreen(navController, viewModel) }
    }
}

// --- COMPONENTS ---

@Composable
fun AppScaffold(
    navController: NavController,
    viewModel: AppViewModel,
    title: String? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                modifier = Modifier.width(300.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(viewModel.currentUser?.username ?: "Usuario", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(viewModel.currentUser?.role?.replaceFirstChar { it.uppercase() } ?: "", color = Color.Gray, fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    DrawerItem(viewModel.t("nav_reports"), Icons.Default.Description) { 
                        scope.launch { drawerState.close() }
                        navController.navigate("reports_admin") 
                    }
                    DrawerItem(viewModel.t("nav_incidents"), Icons.Default.AssignmentInd) { 
                        scope.launch { drawerState.close() }
                        navController.navigate("incidents_admin") 
                    }
                    DrawerItem(viewModel.t("nav_control"), Icons.Default.SettingsSuggest) { 
                        scope.launch { drawerState.close() }
                        navController.navigate("control_panel") 
                    }
                    
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    
                    DrawerItem(viewModel.t("nav_settings"), Icons.Default.Settings) { 
                        scope.launch { drawerState.close() }
                        navController.navigate("settings")
                    }
                    DrawerItem(viewModel.t("nav_help"), Icons.Default.HelpOutline) { 
                        scope.launch { drawerState.close() }
                        navController.navigate("help")
                    }
                    DrawerItem(viewModel.t("nav_contact"), Icons.Default.ContactPage) { 
                        scope.launch { drawerState.close() }
                        navController.navigate("contact")
                    }
                    DrawerItem(viewModel.t("nav_privacy"), Icons.Default.PrivacyTip) { 
                        scope.launch { drawerState.close() }
                        navController.navigate("privacy")
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CustomTopBar(onMenuClick = { scope.launch { drawerState.open() } }, viewModel = viewModel, navController = navController)
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        icon = { 
                            Box {
                                Icon(Icons.Default.Book, contentDescription = null)
                                Icon(
                                    Icons.Default.Favorite, 
                                    contentDescription = null, 
                                    modifier = Modifier.size(10.dp).align(Alignment.BottomEnd),
                                    tint = if (currentRoute == "subscriptions") Color.Red else Color.Gray
                                )
                            }
                        },
                        selected = currentRoute == "subscriptions",
                        onClick = { navController.navigate("subscriptions") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        selected = currentRoute == "main",
                        onClick = { navController.navigate("main") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.MeetingRoom, contentDescription = null) },
                        selected = false,
                        onClick = { (context as? Activity)?.finishAndRemoveTask() }
                    )
                }
            }
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}

@Composable
fun DrawerItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = { Icon(icon, contentDescription = null) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp)
    )
}

@Composable
fun CustomTopBar(onMenuClick: () -> Unit, viewModel: AppViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth().background(IberBlueHeader).padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = R.drawable.ic_launcher), contentDescription = null, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("IberAlert", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { newVal -> viewModel.searchQuery = newVal },
                placeholder = { Text(viewModel.t("search_placeholder"), fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, modifier = Modifier.size(20.dp)) },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(12.dp))
            Icon(Icons.Default.AccountCircle, null, modifier = Modifier.size(32.dp), tint = Color.DarkGray)
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, null, modifier = Modifier.size(32.dp), tint = Color.DarkGray)
            }
        }
        if (viewModel.searchQuery.isNotEmpty()) {
            val filtered = viewModel.allCities.filter { it.contains(viewModel.searchQuery, ignoreCase = true) }
            if (filtered.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                        items(filtered) { city ->
                            Text(city, modifier = Modifier.fillMaxWidth().clickable {
                                viewModel.searchQuery = ""
                                navController.navigate("location_detail/$city")
                            }.padding(12.dp))
                        }
                    }
                }
            }
        }
    }
}

// --- SCREENS ---

@Composable
fun LoginScreen(navController: NavController, viewModel: AppViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("es" to "Español", "ca" to "Catalán", "eu" to "Euskera", "gl" to "Gallego")

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("IberAlert", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Image(painter = painterResource(id = R.drawable.ic_launcher), contentDescription = null, modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.height(48.dp))
        Text(viewModel.t("login_title"), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate("clave") },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text(viewModel.t("login_btn_clave"), color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Box {
            OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(languages.find { it.first == viewModel.currentLanguage }?.second ?: "Español")
                Icon(Icons.Default.ArrowDropDown, null)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                languages.forEach { lang ->
                    DropdownMenuItem(text = { Text(lang.second) }, onClick = {
                        viewModel.currentLanguage = lang.first
                        expanded = false
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("He leído y estoy de acuerdo con los Términos y la Política de Privacidad", fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}

@Composable
fun SubscriptionsScreen(navController: NavController, viewModel: AppViewModel) {
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Text(viewModel.t("nav_subs"), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            val mySubs = viewModel.currentUser?.subscriptions ?: listOf()
            if (mySubs.isEmpty()) {
                Text("No estás suscrito a ninguna zona todavía.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(mySubs) { city ->
                        Card(modifier = Modifier.fillMaxWidth().clickable { navController.navigate("location_detail/$city") }) {
                            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(city, modifier = Modifier.weight(1f))
                                Icon(Icons.Default.ArrowOutward, null)
                            }
                        }
                    }
                }
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
                Text("Acceso Cl@ve", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Introduce tus credenciales", color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(value = user, onValueChange = { newVal -> user = newVal }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = pass, onValueChange = { newVal -> pass = newVal }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
                
                if (error) {
                    Text("Credenciales incorrectas", color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = { navController.popBackStack() }) { Text("Cancelar") }
                    Button(onClick = {
                        if (viewModel.login(user.lowercase().trim(), pass.lowercase().trim())) {
                            navController.navigate("main") { popUpTo("login") { inclusive = true } }
                        } else {
                            error = true
                        }
                    }) { Text("Acceder") }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavController, viewModel: AppViewModel) {
    AppScaffold(navController, viewModel) { paddingValues ->
        val mySubs = viewModel.currentUser?.subscriptions ?: listOf()
        val activeIncidents = viewModel.incidents.filter { it.estado == "Activa" && mySubs.contains(it.ciudad) }
        
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Text("Últimas Incidencias", fontSize = 22.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
            
            if (activeIncidents.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay incidencias en tus zonas suscritas", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(activeIncidents) { inc ->
                        IncidentHomeCard(inc) { navController.navigate("incident_detail/${inc.id}") }
                    }
                }
            }
        }
    }
}

@Composable
fun IncidentHomeCard(inc: Incident, onClick: () -> Unit) {
    val bgColor = if (inc.nivel == "Emergencia") IberOrange else IberYellow
    val icon = if (inc.nivel == "Emergencia") Icons.Default.Warning else Icons.Default.Info
    
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, null, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("${inc.ciudad}. Nivel ${inc.nivel}:", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(inc.titulo, fontSize = 15.sp)
        }
    }
}

@Composable
fun IncidentDetailScreen(navController: NavController, viewModel: AppViewModel, id: Long) {
    val inc = viewModel.incidents.find { it.id == id }
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState())) {
            if (inc == null) {
                Text("Incidencia no encontrada")
            } else {
                val bgColor = if (inc.nivel == "Emergencia") IberOrange else IberYellow
                val icon = if (inc.nivel == "Emergencia") Icons.Default.Warning else Icons.Default.Info
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, null, Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("${inc.ciudad}. Nivel ${inc.nivel}: ${inc.titulo}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Text(inc.descripcion, fontSize = 16.sp)
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Recomendaciones:", fontWeight = FontWeight.Bold)
                Text(inc.recomendacion)
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("Fecha de emisión: ${inc.fecha}", fontWeight = FontWeight.Bold)
                
                Spacer(modifier = Modifier.height(32.dp))
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("< ${viewModel.t("btn_back")}", color = Color(0xFF1976D2))
                }
            }
        }
    }
}

@Composable
fun LocationDetailScreen(navController: NavController, viewModel: AppViewModel, city: String) {
    val incidents = viewModel.incidents.filter { it.ciudad == city && it.estado == "Activa" }
    var isSubbed by remember { mutableStateOf(viewModel.currentUser?.subscriptions?.contains(city) == true) }
    
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Text(city, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Información sobre la ubicación y sus avisos activos.", color = Color.Gray)
            
            Spacer(modifier = Modifier.height(24.dp))
            Text("Últimas incidencias", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            
            if (incidents.isEmpty()) {
                Text("No hay incidencias activas en esta zona.", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                    items(incidents) { inc ->
                        IncidentHomeCard(inc) { navController.navigate("incident_detail/${inc.id}") }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { navController.popBackStack() }) { Text("< ${viewModel.t("btn_back")}") }
                
                IconButton(
                    onClick = { viewModel.toggleSubscription(city); isSubbed = !isSubbed },
                    modifier = Modifier.size(64.dp).background(Color.White, CircleShape).padding(8.dp)
                ) {
                    Icon(
                        if (isSubbed) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isSubbed) Color.Red else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
        }


@Composable
fun AdminReportsScreen(navController: NavController, viewModel: AppViewModel) {
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Text(viewModel.t("nav_reports"), fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.weight(1f)) {
                items(viewModel.reports) { rep ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = IberInfoBlue)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, null, Modifier.size(32.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(rep.titulo, fontWeight = FontWeight.Bold)
                                    Text("Autor: ${rep.autor}", fontSize = 14.sp)
                                }
                                IconButton(onClick = { navController.navigate("report_detail/${rep.id}") }) {
                                    Icon(Icons.Default.Visibility, null, Modifier.size(32.dp))
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = { viewModel.reports.remove(rep) }) {
                                    Icon(Icons.Default.Delete, null)
                                }
                                IconButton(onClick = { navController.navigate("report_form/${rep.id}") }) {
                                    Icon(Icons.Default.Edit, null)
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("report_form/-1") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(viewModel.t("btn_create_report"))
            }
        }
    }
}

@Composable
fun ReportDetailScreen(navController: NavController, viewModel: AppViewModel, id: Long) {
    val rep = viewModel.reports.find { it.id == id }
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState())) {
            if (rep == null) {
                Text("Informe no encontrado")
            } else {
                Text(viewModel.t("nav_reports"), fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = IberInfoBlue)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, null, Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(rep.titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Estadísticas del Informe:", fontWeight = FontWeight.Bold)
                    Text("Total: 100%", fontSize = 12.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                // Simulated Chart
                Row(modifier = Modifier.fillMaxWidth().height(150.dp).padding(horizontal = 16.dp), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceEvenly) {
                    rep.stats.forEach { (label, value) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val color = if (label == "red") IberOrange else IberYellow
                            Box(modifier = Modifier.width(40.dp).height((value * 1.2).dp).background(color, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(if (label == "red") "Emerg." else "Alerta", fontSize = 10.sp)
                            Text("$value%", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    // Additional random data bars
                    repeat(2) { idx ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(modifier = Modifier.width(40.dp).height((20 + idx * 15).dp).background(IberInfoBlue, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Otros", fontSize = 10.sp)
                            Text("${20 + idx * 15}%", fontSize = 10.sp)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Text("Detalles del Análisis:", fontWeight = FontWeight.Bold)
                Card(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))) {
                    Text(rep.desc, modifier = Modifier.padding(16.dp), fontSize = 14.sp)
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { navController.popBackStack() }) {
                        Text("< ${viewModel.t("btn_back")}")
                    }
                    Row {
                        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) { Text("Generar") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) { Text("Exportar") }
                    }
                }
            }
        }
    }
}

@Composable
fun ControlPanelScreen(navController: NavController, viewModel: AppViewModel) {
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text(viewModel.t("nav_control"), fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Estado del servicio:", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                OutlinedTextField("Activo", {}, readOnly = true, modifier = Modifier.width(120.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Umbral de Lluvia (mm/H):", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                OutlinedTextField("55", {}, readOnly = true, modifier = Modifier.width(80.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Umbral de Viento (km/H):", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                OutlinedTextField("60", {}, readOnly = true, modifier = Modifier.width(80.dp))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Última conexión:", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                OutlinedTextField("08-04-2026", {}, readOnly = true, modifier = Modifier.width(150.dp))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) { Text(viewModel.t("btn_save")) }
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) { Text("Sincronizar") }
            }
        }
    }
}

@Composable
fun AdminIncidentsScreen(navController: NavController, viewModel: AppViewModel) {
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Text(viewModel.t("nav_incidents"), fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.weight(1f)) {
                items(viewModel.incidents) { inc ->
                    val bgColor = if (inc.nivel == "Emergencia") IberOrange else IberYellow
                    val icon = if (inc.nivel == "Emergencia") Icons.Default.Warning else Icons.Default.Info
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = bgColor)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(icon, null, Modifier.size(32.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("${inc.ciudad}. ${inc.titulo} (${inc.estado})", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                IconButton(onClick = { inc.estado = "Desestimada" }) {
                                    Icon(Icons.Default.Archive, null)
                                }
                                IconButton(onClick = { viewModel.incidents.remove(inc) }) {
                                    Icon(Icons.Default.Delete, null)
                                }
                                IconButton(onClick = { navController.navigate("incident_form/${inc.id}") }) {
                                    Icon(Icons.Default.Edit, null)
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("incident_form/-1") },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(viewModel.t("btn_create_incident"))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncidentFormScreen(navController: NavController, viewModel: AppViewModel, id: Long) {
    val existing = viewModel.incidents.find { it.id == id }
    var ciudad by remember { mutableStateOf(existing?.ciudad ?: viewModel.allCities.first()) }
    var nombre by remember { mutableStateOf(existing?.titulo ?: "") }
    var desc by remember { mutableStateOf(existing?.descripcion ?: "") }
    var estado by remember { mutableStateOf(existing?.estado ?: "Activa") }
    var instrucciones by remember { mutableStateOf(existing?.recomendacion ?: "") }
    var nivel by remember { mutableStateOf(existing?.nivel ?: "Alerta") }

    var expandedCiudad by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) }
    var expandedNivel by remember { mutableStateOf(false) }

    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text(if (id == -1L) viewModel.t("btn_create_incident") else "Editar Incidencia", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            
            ExposedDropdownMenuBox(expanded = expandedCiudad, onExpandedChange = { expandedCiudad = !expandedCiudad }) {
                OutlinedTextField(value = ciudad, onValueChange = {}, readOnly = true, label = { Text("Ubicación") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCiudad) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                ExposedDropdownMenu(expanded = expandedCiudad, onDismissRequest = { expandedCiudad = false }) {
                    viewModel.allCities.forEach { city ->
                        DropdownMenuItem(text = { Text(city) }, onClick = { ciudad = city; expandedCiudad = false })
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = nombre, onValueChange = { newVal -> nombre = newVal }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
            
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = desc, onValueChange = { newVal -> desc = newVal }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())
            
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(expanded = expandedEstado, onExpandedChange = { expandedEstado = !expandedEstado }) {
                OutlinedTextField(value = estado, onValueChange = {}, readOnly = true, label = { Text("Estado") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                ExposedDropdownMenu(expanded = expandedEstado, onDismissRequest = { expandedEstado = false }) {
                    listOf("Activa", "Desestimada").forEach { est ->
                        DropdownMenuItem(text = { Text(est) }, onClick = { estado = est; expandedEstado = false })
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(expanded = expandedNivel, onExpandedChange = { expandedNivel = !expandedNivel }) {
                OutlinedTextField(value = nivel, onValueChange = {}, readOnly = true, label = { Text("Nivel") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedNivel) }, modifier = Modifier.menuAnchor().fillMaxWidth())
                ExposedDropdownMenu(expanded = expandedNivel, onDismissRequest = { expandedNivel = false }) {
                    listOf("Alerta", "Emergencia").forEach { niv ->
                        DropdownMenuItem(text = { Text(niv) }, onClick = { nivel = niv; expandedNivel = false })
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = instrucciones, onValueChange = { newVal -> instrucciones = newVal }, label = { Text("Recomendaciones") }, modifier = Modifier.fillMaxWidth())
            
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { navController.popBackStack() }) { Text(viewModel.t("btn_cancel")) }
                Button(
                    onClick = {
                        val newInc = Incident(if(id == -1L) System.currentTimeMillis() else id, ciudad, nombre, nivel, desc, estado, instrucciones, "09-04-2026")
                        if (id == -1L) viewModel.incidents.add(newInc)
                        else {
                            val idx = viewModel.incidents.indexOfFirst { it.id == id }
                            if (idx != -1) viewModel.incidents[idx] = newInc
                        }
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) { Text(viewModel.t("btn_save")) }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportFormScreen(navController: NavController, viewModel: AppViewModel, id: Long) {
    val existing = viewModel.reports.find { it.id == id }
    var ciudad by remember { mutableStateOf(existing?.ciudad ?: viewModel.allCities.first()) }
    var titulo by remember { mutableStateOf(existing?.titulo ?: "IF-000${viewModel.reports.size + 1}") }
    var inicio by remember { mutableStateOf(existing?.inicio ?: "2026-04-01") }
    var fin by remember { mutableStateOf(existing?.fin ?: "2026-04-07") }
    var tipo by remember { mutableStateOf(existing?.tipo ?: "Comp. meteorológicos (Mortalidad)") }
    var desc by remember { mutableStateOf(existing?.desc ?: "") }

    var expandedCiudad by remember { mutableStateOf(false) }
    var expandedTipo by remember { mutableStateOf(false) }

    val reportTypes = listOf(
        "Comp. meteorológicos (Mortalidad)",
        "Comp. agravantes y predictores",
        "Panel de Control (Dashboard)"
    )

    AppScaffold(navController, viewModel) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = if (id == -1L) viewModel.t("btn_create_report") else "Editar Informe",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            ExposedDropdownMenuBox(
                expanded = expandedCiudad,
                onExpandedChange = { expandedCiudad = !expandedCiudad }
            ) {
                OutlinedTextField(
                    value = ciudad,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Ubicación") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCiudad) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedCiudad,
                    onDismissRequest = { expandedCiudad = false }
                ) {
                    viewModel.allCities.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = { ciudad = city; expandedCiudad = false }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = titulo,
                onValueChange = { newVal -> titulo = newVal },
                label = { Text("Título/Código") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = inicio, onValueChange = { newVal -> inicio = newVal }, label = { Text("F. Inicio") }, modifier = Modifier.weight(1f))
                OutlinedTextField(value = fin, onValueChange = { newVal -> fin = newVal }, label = { Text("F. Fin") }, modifier = Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = !expandedTipo }
            ) {
                OutlinedTextField(
                    value = tipo,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Informe") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    reportTypes.forEach { t ->
                        DropdownMenuItem(
                            text = { Text(t) },
                            onClick = { tipo = t; expandedTipo = false }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = desc,
                onValueChange = { newValue -> desc = newValue },
                label = { Text("Análisis de Detalles") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { navController.popBackStack() }) { Text(viewModel.t("btn_cancel")) }
                Button(
                    onClick = {
                        val newRep = Report(if(id == -1L) System.currentTimeMillis() else id, "admin", titulo, inicio, fin, tipo, ciudad, mapOf("red" to 50, "yellow" to 50), desc)
                        if (id == -1L) viewModel.reports.add(newRep)
                        else {
                            val idx = viewModel.reports.indexOfFirst { it.id == id }
                            if (idx != -1) viewModel.reports[idx] = newRep
                        }
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) { Text(viewModel.t("btn_save")) }
            }
        }
    }
}

@Composable
fun SettingsScreen(navController: NavController, viewModel: AppViewModel) {
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text(viewModel.t("nav_settings"), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Configuración de notificaciones, idioma y cuenta.", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Idioma actual: ${if(viewModel.currentLanguage=="es") "Español" else viewModel.currentLanguage.uppercase()}", fontWeight = FontWeight.Bold)
            // Add more placeholder settings
            Spacer(modifier = Modifier.height(32.dp))
            TextButton(onClick = { navController.popBackStack() }) { Text("< ${viewModel.t("btn_back")}") }
        }
    }
}

@Composable
fun HelpScreen(navController: NavController, viewModel: AppViewModel) {
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text(viewModel.t("nav_help"), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Preguntas Frecuentes (FAQ)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("1. ¿Cómo suscribirse a una zona?\nBusque la ciudad en la barra superior y pulse el corazón.")
            Text("\n2. ¿Qué significan los colores?\nNaranja: Alerta. Rojo: Emergencia.")
            Spacer(modifier = Modifier.height(32.dp))
            TextButton(onClick = { navController.popBackStack() }) { Text("< ${viewModel.t("btn_back")}") }
        }
    }
}

@Composable
fun ContactScreen(navController: NavController, viewModel: AppViewModel) {
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            Text(viewModel.t("nav_contact"), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Soporte Técnico IberAlert", fontWeight = FontWeight.Bold)
            Text("Email: soporte@iberalert.es")
            Text("Teléfono: +34 900 000 000")
            Spacer(modifier = Modifier.height(32.dp))
            TextButton(onClick = { navController.popBackStack() }) { Text("< ${viewModel.t("btn_back")}") }
        }
    }
}

@Composable
fun PrivacyScreen(navController: NavController, viewModel: AppViewModel) {
    AppScaffold(navController, viewModel) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).verticalScroll(rememberScrollState())) {
            Text(viewModel.t("nav_privacy"), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            Text("En IberAlert protegemos sus datos de acuerdo con el RGPD. No compartimos su ubicación exacta con terceros, solo la usamos para enviarle alertas relevantes de su zona.")
            Spacer(modifier = Modifier.height(32.dp))
            TextButton(onClick = { navController.popBackStack() }) { Text("< ${viewModel.t("btn_back")}") }
        }
    }
}

