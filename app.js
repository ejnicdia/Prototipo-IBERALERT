// app.js - IBERALERT Prototype Dynamic Logic (Roles, Subscriptions, i18n & Reports)

// --- MULTI-LANGUAGE (i18n) DICTIONARY ---
const translations = {
    es: {
        login_title: "Iniciar sesión",
        login_btn_clave: "Acceder con Cl@ve",
        login_opt_1: "DNIe/Certificado electrónico",
        login_opt_2: "Cl@ve móvil o Cl@ve permanente",
        login_opt_3: "Ciudadanos UE",
        login_terms: "He leído y estoy de acuerdo con los <a href='#'>Términos y la Política de Privacidad</a>",
        login_footer: "Todos los derechos reservados &copy;",
        clave_title: "Identificación",
        clave_desc: "Por favor, ingrese los datos del usuario",
        clave_user: "Usuario:",
        clave_pass: "Contraseña:",
        btn_access: "Acceder",
        btn_cancel: "Cancelar",
        btn_back: "Volver",
        btn_save: "Guardar",
        btn_send: "Enviar Mensaje",
        btn_sync: "Sincronizar",
        nav_home: "Inicio",
        nav_subs: "Zonas Suscritas",
        nav_reports: "Informes",
        nav_incidents: "Gestión de Incidencias",
        nav_control: "Panel de Control",
        nav_settings: "Ajustes Generales",
        nav_help: "Ayuda y FAQ",
        nav_contact: "Contacto",
        nav_privacy: "Política de Privacidad",
        search_placeholder: "Buscar ubicaciones...",
        title_dashboard: "Incidencias Recientes (Suscripciones)",
        title_search: "Resultados de Búsqueda",
        title_subs: "Mis Zonas Suscritas",
        subtitle_loc_incidents: "Incidencias en esta ubicación",
        lbl_affected_loc: "Ubicación afectada:",
        lbl_recommendations: "Recomendaciones:",
        lbl_emission_date: "Fecha de emisión:",
        title_reports: "Lista de Informes",
        btn_create_report: "Crear Informe",
        title_create_report: "Crear Informe",
        lbl_start_date: "Fecha de Inicio:",
        lbl_end_date: "Fecha de Fin:",
        lbl_report_type: "Tipo de Reporte:",
        lbl_main_loc: "Ubicación Principal:",
        title_control: "Panel de Control",
        lbl_svc_status: "Estado del servicio:",
        lbl_rain_thresh: "Umbral de Lluvia (mm/H):",
        lbl_wind_thresh: "Umbral de Viento (km/H):",
        lbl_last_conn: "Última conexión:",
        opt_active: "Activo",
        opt_inactive: "Inactivo",
        title_incidents: "Gestión de Incidencias",
        btn_create_incident: "Crear Incidencia",
        title_create_incident: "Crear Incidencia",
        lbl_location: "Ubicación:",
        lbl_title: "Nombre/Título:",
        lbl_desc: "Descripción:",
        lbl_status: "Estado:",
        opt_dismissed: "Desestimada",
        lbl_inc_type: "Tipo de Incidencia:",
        opt_emergency: "Emergencia (Peligro Extremo / Naranja)",
        opt_alert: "Alerta (Precaución / Amarillo)",
        lbl_rec_input: "Recomendaciones (Instrucciones):",
        placeholder_write_here: "Escribe aquí",
        lbl_push_notif: "Recibir Notificaciones Push:",
        lbl_alert_sound: "Sonido de alertas:",
        opt_siren: "Sirena",
        opt_beep: "Pitido",
        opt_silence: "Silencio",
        lbl_dark_mode: "Modo Oscuro:",
        help_q1: "¿Qué significan los tipos de incidencia?",
        help_a1_strong1: "Emergencia (Naranja):",
        help_a1_desc1: "Situación de peligro extremo. Permanezca a salvo y siga las instrucciones.",
        help_a1_strong2: "Alerta (Amarillo):",
        help_a1_desc2: "Situación de riesgo moderado. Esté atento a nuevas actualizaciones.",
        help_q2: "¿Cómo me suscribo a una zona?",
        help_a2: "Busca una ubicación en la barra superior y pulsa el botón 'Suscribirse' para ver sus avisos en tu Inicio.",
        lbl_subject: "Asunto:",
        lbl_message: "Mensaje:",
        priv_p1: "Sus datos están protegidos según la ley de Protección de Datos española (LOPD). IberAlert no comparte información de ubicación precisa con terceros sin su consentimiento explícito.",
        priv_p2: "Toda la información meteorológica se provee a efectos informativos. Los datos de identificación provienen del sistema Cl@ve del estado español.",
        lbl_report_details: "Detalles del Análisis",
        alert_saved: "Ajustes guardados",
        alert_synced: "Sincronizado con estaciones meteorológicas",
        alert_msg_sent: "Mensaje enviado. Nos pondremos en contacto pronto.",
        lbl_btn_subscribe: "<i class='fa-regular fa-heart'></i> Suscribirse",
        lbl_btn_unsubscribe: "<i class='fa-solid fa-heart-crack'></i> Desuscribirse",
        lvl_emergency: "Emergencia",
        lvl_alert: "Alerta"
    },
    ca: {
        login_title: "Iniciar sessió",
        login_btn_clave: "Accedir amb Cl@ve",
        login_opt_1: "DNIe/Certificat electrònic",
        login_opt_2: "Cl@ve mòbil o Cl@ve permanent",
        login_opt_3: "Ciutadans UE",
        login_terms: "He llegit i estic d'acord amb els <a href='#'>Termes i la Política de Privadesa</a>",
        login_footer: "Tots els drets reservats &copy;",
        clave_title: "Identificació",
        clave_desc: "Si us plau, introdueixi les dades de l'usuari",
        clave_user: "Usuari:",
        clave_pass: "Contrasenya:",
        btn_access: "Accedir",
        btn_cancel: "Cancel·lar",
        btn_back: "Tornar",
        btn_save: "Desar",
        btn_send: "Enviar Missatge",
        btn_sync: "Sincronitzar",
        nav_home: "Inici",
        nav_subs: "Zones Subscrites",
        nav_reports: "Informes",
        nav_incidents: "Gestió d'Incidències",
        nav_control: "Tauler de Control",
        nav_settings: "Ajustos Generals",
        nav_help: "Ajuda i FAQ",
        nav_contact: "Contacte",
        nav_privacy: "Política de Privadesa",
        search_placeholder: "Cercar ubicacions...",
        title_dashboard: "Incidències Recents (Subscripcions)",
        title_search: "Resultats de Cerca",
        title_subs: "Les meves Zones Subscrites",
        subtitle_loc_incidents: "Incidències en aquesta ubicació",
        lbl_affected_loc: "Ubicació afectada:",
        lbl_recommendations: "Recomanacions:",
        lbl_emission_date: "Data d'emissió:",
        title_reports: "Llista d'Informes",
        btn_create_report: "Crear Informe",
        title_create_report: "Crear Informe",
        lbl_start_date: "Data d'Inici:",
        lbl_end_date: "Data de Fi:",
        lbl_report_type: "Tipus de Report:",
        lbl_main_loc: "Ubicació Principal:",
        title_control: "Tauler de Control",
        lbl_svc_status: "Estat del servei:",
        lbl_rain_thresh: "Llindar de Pluja (mm/H):",
        lbl_wind_thresh: "Llindar de Vent (km/H):",
        lbl_last_conn: "Última connexió:",
        opt_active: "Actiu",
        opt_inactive: "Inactiu",
        title_incidents: "Gestió d'Incidències",
        btn_create_incident: "Crear Incidència",
        title_create_incident: "Crear Incidència",
        lbl_location: "Ubicació:",
        lbl_title: "Nom/Títol:",
        lbl_desc: "Descripció:",
        lbl_status: "Estat:",
        opt_dismissed: "Desestimada",
        lbl_inc_type: "Tipus d'Incidència:",
        opt_emergency: "Emergència (Perill Extrem / Taronja)",
        opt_alert: "Alerta (Precaució / Groc)",
        lbl_rec_input: "Recomanacions (Instruccions):",
        placeholder_write_here: "Escriu aquí",
        lbl_push_notif: "Rebre Notificacions Push:",
        lbl_alert_sound: "So d'alertes:",
        opt_siren: "Sirena",
        opt_beep: "Xiulet",
        opt_silence: "Silenci",
        lbl_dark_mode: "Mode Fosc:",
        help_q1: "Què signifiquen els tipus d'incidència?",
        help_a1_strong1: "Emergència (Taronja):",
        help_a1_desc1: "Situació de perill extrem. Romangui a resguard i segueixi les instruccions.",
        help_a1_strong2: "Alerta (Groc):",
        help_a1_desc2: "Situació de risc moderat. Estigui atent a noves actualitzacions.",
        help_q2: "Com em subscric a una zona?",
        help_a2: "Busca una ubicació a la barra superior i prem el botó 'Subscriure's' per veure els seus avisos al teu Inici.",
        lbl_subject: "Assumpte:",
        lbl_message: "Missatge:",
        priv_p1: "Les seves dades estan protegides segons la llei de Protecció de Dades espanyola (LOPD).",
        priv_p2: "Tota la informació meteorològica es proveeix a efectes informatius.",
        lbl_report_details: "Detalls de l'Anàlisi",
        alert_saved: "Ajustos desats",
        alert_synced: "Sincronitzat amb estacions meteorològiques",
        alert_msg_sent: "Missatge enviat. Ens posarem en contacte aviat.",
        lbl_btn_subscribe: "<i class='fa-regular fa-heart'></i> Subscriure's",
        lbl_btn_unsubscribe: "<i class='fa-solid fa-heart-crack'></i> Cancel·lar subscripció",
        lvl_emergency: "Emergència",
        lvl_alert: "Alerta"
    },
    eu: {
        login_title: "Hasi saioa",
        login_btn_clave: "Sartu Cl@ve bidez",
        login_opt_1: "DNIe/Ziurtagiri elektronikoa",
        login_opt_2: "Cl@ve mugikorra edo iraunkorra",
        login_opt_3: "EBko herritarrak",
        login_terms: "<a href='#'>Baldintzak eta Pribatutasun Politika</a> irakurri eta onartzen ditut",
        login_footer: "Eskubide guztiak erreserbatuta &copy;",
        clave_title: "Identifikazioa",
        clave_desc: "Mesedez, sartu erabiltzailearen datuak",
        clave_user: "Erabiltzailea:",
        clave_pass: "Pasahitza:",
        btn_access: "Sartu",
        btn_cancel: "Utzi",
        btn_back: "Itzuli",
        btn_save: "Gorde",
        btn_send: "Mezua Bidali",
        btn_sync: "Sinkronizatu",
        nav_home: "Hasiera",
        nav_subs: "Harpidetutako Eremuak",
        nav_reports: "Txostenak",
        nav_incidents: "Gorabeheren Kudeaketa",
        nav_control: "Kontrol Panela",
        nav_settings: "Ezarpen Orokorrak",
        nav_help: "Laguntza eta FAQ",
        nav_contact: "Kontaktua",
        nav_privacy: "Pribatutasun Politika",
        search_placeholder: "Bilatu kokapenak...",
        title_dashboard: "Azken Gorabeherak (Harpidetzak)",
        title_search: "Bilaketaren Emaitzak",
        title_subs: "Nire Harpidetutako Eremuak",
        subtitle_loc_incidents: "Gorabeherak kokapen honetan",
        lbl_affected_loc: "Eragindako kokapena:",
        lbl_recommendations: "Gomendioak:",
        lbl_emission_date: "Igorpen data:",
        title_reports: "Txostenen Zerrenda",
        btn_create_report: "Txostena Sortu",
        title_create_report: "Txostena Sortu",
        lbl_start_date: "Hasiera Data:",
        lbl_end_date: "Amaiera Data:",
        lbl_report_type: "Txosten Mota:",
        lbl_main_loc: "Kokapen Nagusia:",
        title_control: "Kontrol Panela",
        lbl_svc_status: "Zerbitzuaren egoera:",
        lbl_rain_thresh: "Euri Atalasea (mm/H):",
        lbl_wind_thresh: "Haize Atalasea (km/H):",
        lbl_last_conn: "Azken konexioa:",
        opt_active: "Aktiboa",
        opt_inactive: "Inaktiboa",
        title_incidents: "Gorabeheren Kudeaketa",
        btn_create_incident: "Gorabehera Sortu",
        title_create_incident: "Gorabehera Sortu",
        lbl_location: "Kokapena:",
        lbl_title: "Izena/Izenburua:",
        lbl_desc: "Deskribapena:",
        lbl_status: "Egoera:",
        opt_dismissed: "Ztertua",
        lbl_inc_type: "Gorabehera Mota:",
        opt_emergency: "Larrialdia (Muturreko Arriskua / Laranja)",
        opt_alert: "Alerta (Prebentzioa / Horia)",
        lbl_rec_input: "Gomendioak (Argibideak):",
        placeholder_write_here: "Idatzi hemen",
        lbl_push_notif: "Jaso Push Jakinarazpenak:",
        lbl_alert_sound: "Alerten soinua:",
        opt_siren: "Sirena",
        opt_beep: "Txistua",
        opt_silence: "Isiltasuna",
        lbl_dark_mode: "Modu Iluna:",
        help_q1: "Zer esan nahi dute gorabehera motek?",
        help_a1_strong1: "Larrialdia (Laranja):",
        help_a1_desc1: "Muturreko arrisku egoera. Egon babesean eta jarraitu argibideak.",
        help_a1_strong2: "Alerta (Horia):",
        help_a1_desc2: "Arrisku moderatuko egoera. Adi egon eguneratze berriei.",
        help_q2: "Nola harpidetzen naiz eremu batera?",
        help_a2: "Bilatu kokapen bat goiko barran eta sakatu 'Harpidetu' botoia Hasieran abisuak ikusteko.",
        lbl_subject: "Gaia:",
        lbl_message: "Mezua:",
        priv_p1: "Zure datuak Espainiako Datuak Babesteko Legearen (DBLO) arabera babestuta daude.",
        priv_p2: "Informazio meteorologiko guztia informazio-ondorioetarako ematen da.",
        lbl_report_details: "Analisiaren Xehetasunak",
        alert_saved: "Ezarpenak gordeta",
        alert_synced: "Estazio meteorologikoekin sinkronizatuta",
        alert_msg_sent: "Mezua bidalita. Laster jarriko gara harremanetan zurekin.",
        lbl_btn_subscribe: "<i class='fa-regular fa-heart'></i> Harpidetu",
        lbl_btn_unsubscribe: "<i class='fa-solid fa-heart-crack'></i> Harpidetza kendu",
        lvl_emergency: "Larrialdia",
        lvl_alert: "Alerta"
    },
    gl: {
        login_title: "Iniciar sesión",
        login_btn_clave: "Acceder con Cl@ve",
        login_opt_1: "DNIe/Certificado electrónico",
        login_opt_2: "Cl@ve móbil ou Cl@ve permanente",
        login_opt_3: "Cidadáns da UE",
        login_terms: "Lin e estou dacordo cos <a href='#'>Termos e a Política de Privacidade</a>",
        login_footer: "Todos os dereitos reservados &copy;",
        clave_title: "Identificación",
        clave_desc: "Por favor, introduza os datos do usuario",
        clave_user: "Usuario:",
        clave_pass: "Contrasinal:",
        btn_access: "Acceder",
        btn_cancel: "Cancelar",
        btn_back: "Volver",
        btn_save: "Gardar",
        btn_send: "Enviar Mensaxe",
        btn_sync: "Sincronizar",
        nav_home: "Inicio",
        nav_subs: "Zonas Subscritas",
        nav_reports: "Informes",
        nav_incidents: "Xestión de Incidencias",
        nav_control: "Panel de Control",
        nav_settings: "Axustes Xerais",
        nav_help: "Axuda e FAQ",
        nav_contact: "Contacto",
        nav_privacy: "Política de Privacidade",
        search_placeholder: "Buscar localizacións...",
        title_dashboard: "Incidencias Recentes (Subscricións)",
        title_search: "Resultados da Busca",
        title_subs: "As miñas Zonas Subscritas",
        subtitle_loc_incidents: "Incidencias nesta localización",
        lbl_affected_loc: "Localización afectada:",
        lbl_recommendations: "Recomendacións:",
        lbl_emission_date: "Data de emisión:",
        title_reports: "Lista de Informes",
        btn_create_report: "Crear Informe",
        title_create_report: "Crear Informe",
        lbl_start_date: "Data de Inicio:",
        lbl_end_date: "Data de Fin:",
        lbl_report_type: "Tipo de Reporte:",
        lbl_main_loc: "Localización Principal:",
        title_control: "Panel de Control",
        lbl_svc_status: "Estado do servizo:",
        lbl_rain_thresh: "Limiar de Choiva (mm/H):",
        lbl_wind_thresh: "Limiar de Vento (km/H):",
        lbl_last_conn: "Última conexión:",
        opt_active: "Activo",
        opt_inactive: "Inactivo",
        title_incidents: "Xestión de Incidencias",
        btn_create_incident: "Crear Incidencia",
        title_create_incident: "Crear Incidencia",
        lbl_location: "Localización:",
        lbl_title: "Nome/Título:",
        lbl_desc: "Descrición:",
        lbl_status: "Estado:",
        opt_dismissed: "Desestimada",
        lbl_inc_type: "Tipo de Incidencia:",
        opt_emergency: "Emerxencia (Perigo Extremo / Laranxa)",
        opt_alert: "Alerta (Precaución / Amarelo)",
        lbl_rec_input: "Recomendacións (Instrucións):",
        placeholder_write_here: "Escribe aquí",
        lbl_push_notif: "Recibir Notificacións Push:",
        lbl_alert_sound: "Son de alertas:",
        opt_siren: "Sirena",
        opt_beep: "Pito",
        opt_silence: "Silencio",
        lbl_dark_mode: "Modo Escuro:",
        help_q1: "Que significan os tipos de incidencia?",
        help_a1_strong1: "Emerxencia (Laranxa):",
        help_a1_desc1: "Situación de perigo extremo. Permaneza a salvo e siga as instrucións.",
        help_a1_strong2: "Alerta (Amarelo):",
        help_a1_desc2: "Situación de risco moderado. Estea atento a novas actualizacións.",
        help_q2: "Como me subscribo a unha zona?",
        help_a2: "Busca unha localización na barra superior e pulsa o botón 'Subscribirse' para ver os seus avisos no teu Inicio.",
        lbl_subject: "Asunto:",
        lbl_message: "Mensaxe:",
        priv_p1: "Os seus datos están protexidos segundo a lei de Protección de Datos española (LOPD).",
        priv_p2: "Toda a información meteorolóxica provese a efectos informativos.",
        lbl_report_details: "Detalles da Análise",
        alert_saved: "Axustes gardados",
        alert_synced: "Sincronizado con estacións meteorolóxicas",
        alert_msg_sent: "Mensaxe enviada. Poremosnos en contacto pronto.",
        lbl_btn_subscribe: "<i class='fa-regular fa-heart'></i> Subscribirse",
        lbl_btn_unsubscribe: "<i class='fa-solid fa-heart-crack'></i> Desubscribirse",
        lvl_emergency: "Emerxencia",
        lvl_alert: "Alerta"
    }
};

let currentLang = 'es';

const rawCiudades = [
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
];
const ciudadesSorted = rawCiudades.sort((a, b) => a.localeCompare(b, 'es'));

const state = {
    usuarios: [
        { username: 'admin', role: 'autoridad', subscriptions: ['Madrid, Madrid', 'Sevilla, Andalucía', 'Cádiz, Andalucía'] },
        { username: 'user', role: 'usuario', subscriptions: ['Barcelona, Cataluña'] }
    ],
    ciudades: ciudadesSorted,
    incidencias: [
        { id: 1, ciudad: "Madrid, Madrid", titulo: "Tornado", nivel: "Emergencia", descripcion: "Tornado detectado por las afueras de la ciudad, fuertes vientos arrancando árboles.", estado: "Activa", recomendacion: "Busque refugio subterráneo o en habitaciones interiores sin ventanas. Aléjese de zonas arboladas.", fecha: "09-04-2026" },
        { id: 2, ciudad: "Cádiz, Andalucía", titulo: "Inundaciones", nivel: "Emergencia", descripcion: "Inundaciones graves provocadas por lluvias torrenciales continuadas a lo largo del territorio gaditano.", estado: "Activa", recomendacion: "Evite salir de casa en la medida de lo posible y aléjese de cauces de ríos y zonas bajas.", fecha: "09-04-2026" },
        { id: 3, ciudad: "Cádiz, Andalucía", titulo: "Precaución Costera", nivel: "Alerta", descripcion: "Marea alta inusual y viento moderado afectando al paseo marítimo.", estado: "Activa", recomendacion: "Evite pasear por el litoral y amarrar bien las embarcaciones.", fecha: "08-04-2026" },
        { id: 4, ciudad: "Sevilla, Andalucía", titulo: "Lluvias torrenciales", nivel: "Emergencia", descripcion: "Fuertes precipitaciones esperadas durante la tarde.", estado: "Activa", recomendacion: "Conduzca con precaución, riesgo de balsas de agua en la vía.", fecha: "08-04-2026" }
    ],
    informes: [
        { id: 1, autor: "admin", titulo: "IF-0001", inicio: "2026-04-01", fin: "2026-04-07", tipo: "Volumen de Alertas", ciudad: "Madrid, Madrid", stats: { red: 45, yellow: 55 }, desc: "Análisis del volumen total de incidencias durante el periodo. Se ha detectado un incremento del 15%." },
        { id: 2, autor: "admin", titulo: "IF-0002", inicio: "2026-03-15", fin: "2026-03-30", tipo: "Resumen de Daños", ciudad: "Cádiz, Andalucía", stats: { red: 80, yellow: 20 }, desc: "Los daños infraestructurales se estiman en 12.5M de euros tras las últimas inundaciones que afectaron a múltiples tramos." }
    ],
    controlPanel: {
        lastConnection: "09-04-2026 14:30:00"
    }
};

let currentEditIncidentId = null;

const app = {
    history: [],
    currentView: null,
    currentUser: null,

    init() {
        this.setupSearch();
        this.populateSelects();
        
        const lastConn = document.getElementById('last-conn-date');
        if(lastConn) lastConn.value = state.controlPanel.lastConnection;

        this.changeLanguage('es');
        this.navigate('view-login');
    },

    // --- i18n ---
    t(key) {
        return translations[currentLang][key] || key;
    },

    changeLanguage(langCode) {
        currentLang = langCode;
        document.querySelectorAll('[data-i18n]').forEach(el => {
            el.innerHTML = this.t(el.getAttribute('data-i18n'));
        });
        document.querySelectorAll('[data-i18n-placeholder]').forEach(el => {
            el.setAttribute('placeholder', this.t(el.getAttribute('data-i18n-placeholder')));
        });
        
        if (this.currentView) {
            this.renderView(this.currentView, this.currentParams || {});
            this.updateNav(this.currentView);
        }
    },

    // --- NAVIGATION & LOGIN ---
    openClaveForm() {
        document.getElementById('view-login').classList.remove('active');
        document.getElementById('view-clave-form').classList.add('active');
    },

    closeClaveForm() {
        document.getElementById('view-clave-form').classList.remove('active');
        document.getElementById('view-login').classList.add('active');
        document.getElementById('login-user').value = '';
        document.getElementById('login-pass').value = '';
    },

    doLogin() {
        const u = document.getElementById('login-user').value.trim().toLowerCase();
        const p = document.getElementById('login-pass').value.trim().toLowerCase();
        const errorEl = document.getElementById('login-error');

        const foundUser = state.usuarios.find(user => user.username === u && p === user.username);

        if (foundUser) {
            this.currentUser = foundUser;
            errorEl.textContent = '';
            document.getElementById('view-clave-form').classList.remove('active');
            document.getElementById('app-container').style.display = 'flex';
            this.applyRBAC();
            this.navigate('view-dashboard', {}, true);
        } else {
            errorEl.textContent = 'Credenciales inválidas. Pruebe admin/admin o user/user.';
        }
    },

    applyRBAC() {
        const isAdmin = this.currentUser.role === 'autoridad';
        
        const adminLinks = document.querySelector('.admin-only');
        if (adminLinks) {
            adminLinks.style.display = isAdmin ? 'block' : 'none';
        }
        
        const adminBottomNav = document.querySelector('.admin-only-flex');
        if (adminBottomNav) {
            adminBottomNav.style.display = isAdmin ? 'flex' : 'none';
        }
    },

    navigate(viewId, params = {}, replaceHistory = false) {
        if (this.currentUser && this.currentUser.role === 'usuario') {
            if (viewId === 'view-report-list' || viewId === 'view-report-form' || viewId === 'view-report-detail' ||
                viewId === 'view-incident-list' || viewId === 'view-incident-form' || 
                viewId === 'view-control-panel') {
                alert("Acceso denegado. Se requieren permisos de Autoridad.");
                return;
            }
        }

        document.getElementById('sidebar').classList.remove('open');

        if (this.currentView && !replaceHistory) {
            this.history.push({view: this.currentView, params: this.currentParams || {}});
        } else if (replaceHistory) {
            this.history = [];
        }

        const views = document.querySelectorAll('.app-view');
        views.forEach(v => v.classList.remove('active'));

        const targetView = document.getElementById(viewId);
        if (targetView) {
            targetView.classList.add('active');
            this.currentView = viewId;
            this.currentParams = params;
        }

        this.updateNav(viewId);
        this.renderView(viewId, params);
    },

    goBack() {
        if (this.history.length > 0) {
            const prev = this.history.pop();
            this.navigate(prev.view, prev.params, true);
        } else {
            this.navigate('view-dashboard', {}, true);
        }
    },

    toggleSidebar() {
        document.getElementById('sidebar').classList.toggle('open');
    },

    updateNav(viewId) {
        const bottomItems = document.querySelectorAll('#bottom-nav .nav-item');
        bottomItems.forEach(item => item.classList.remove('active'));
        
        if (viewId === 'view-subscriptions' && bottomItems[0]) bottomItems[0].classList.add('active');
        if (viewId === 'view-dashboard' && bottomItems[1]) bottomItems[1].classList.add('active');
        if (viewId === 'view-incident-form' && bottomItems[2]) bottomItems[2].classList.add('active');

        const sidebarLinks = document.querySelectorAll('.sidebar-link');
        sidebarLinks.forEach(link => {
            link.style.fontWeight = '400';
            link.style.backgroundColor = 'transparent';
        });
    },

    // --- VIEW RENDERING ---
    renderView(viewId, params) {
        if (viewId === 'view-dashboard') this.renderDashboard();
        else if (viewId === 'view-search') this.renderSearchResults(params.query || '');
        else if (viewId === 'view-location-detail') this.renderLocationDetail(params.city);
        else if (viewId === 'view-subscriptions') this.renderSubscriptions();
        else if (viewId === 'view-incident-detail') this.renderIncidentDetail(params.id);
        else if (viewId === 'view-incident-list') this.renderIncidentManageList();
        else if (viewId === 'view-report-list') this.renderReportList();
        else if (viewId === 'view-report-detail') this.renderReportDetail(params.id);
    },

    renderDashboard() {
        const container = document.getElementById('dashboard-cards');
        container.innerHTML = '';
        
        const mySubs = this.currentUser.subscriptions;
        const activeIncidents = state.incidencias.filter(i => i.estado === 'Activa' && mySubs.includes(i.ciudad));
        
        if (activeIncidents.length === 0) {
            container.innerHTML = '<p>No hay incidencias activas en tus zonas suscritas.</p>';
            return;
        }

        activeIncidents.forEach(inc => {
            container.appendChild(this.createIncidentCard(inc, true));
        });
    },

    renderSubscriptions() {
        const container = document.getElementById('subscriptions-list');
        container.innerHTML = '';

        const mySubs = this.currentUser.subscriptions;
        if (mySubs.length === 0) {
            container.innerHTML = '<p style="padding: 15px 0;">No estás suscrito a ninguna zona todavía.</p>';
            return;
        }

        mySubs.forEach(city => {
            const item = document.createElement('div');
            item.className = 'location-item';
            item.innerHTML = `
                <span>${city}</span>
                <div style="display:flex; gap:15px; align-items:center;">
                    <i class="fa-solid fa-arrow-up-right-from-square" onclick="app.navigate('view-location-detail', { city: '${city}' })"></i>
                </div>
            `;
            container.appendChild(item);
        });
    },

    createIncidentCard(inc, forDashboard = false) {
        const div = document.createElement('div');
        div.className = `card ${this.getColorForLevel(inc.nivel)}`;
        const localNivel = inc.nivel === 'Emergencia' ? this.t('lvl_emergency') : this.t('lvl_alert');
        div.innerHTML = `
            <div class="card-content">
                <i class="fa-solid ${inc.nivel === 'Emergencia' ? 'fa-triangle-exclamation' : 'fa-circle-info'} card-icon"></i>
                <div class="card-text">
                    <strong>${inc.ciudad}. Nivel ${localNivel}: ${inc.titulo}</strong>
                    <p>${inc.descripcion}</p>
                </div>
            </div>
            ${forDashboard ? `<div class="card-footer"><span>Ver más ></span></div>` : ''}
        `;
        div.onclick = () => this.navigate('view-incident-detail', { id: inc.id });
        return div;
    },

    getColorForLevel(nivel) {
        if (nivel === 'Emergencia') return 'card-orange';
        if (nivel === 'Alerta') return 'card-yellow';
        return 'card-blue';
    },

    setupSearch() {
        const searchInput = document.getElementById('global-search');
        if (searchInput) {
            searchInput.addEventListener('input', (e) => {
                const query = e.target.value.trim().toLowerCase();
                if (query.length > 0) {
                    this.navigate('view-search', { query: query });
                } else if (this.currentView === 'view-search') {
                    this.goBack();
                }
            });
        }
    },

    renderSearchResults(query) {
        const container = document.getElementById('search-results');
        container.innerHTML = '';
        
        const filtered = state.ciudades.filter(c => c.toLowerCase().includes(query));
        
        if (filtered.length === 0) {
            container.innerHTML = '<p style="padding: 15px 0;">No se encontraron ubicaciones.</p>';
            return;
        }

        filtered.forEach(city => {
            const item = document.createElement('div');
            item.className = 'location-item';
            item.innerHTML = `<span>${city}</span><i class="fa-solid fa-arrow-up-right-from-square"></i>`;
            item.onclick = () => this.navigate('view-location-detail', { city: city });
            container.appendChild(item);
        });
    },

    renderLocationDetail(city) {
        document.getElementById('loc-title').textContent = city;
        document.getElementById('loc-desc').textContent = `Información general y estado de alertas para la ubicación de ${city}.`;
        
        const container = document.getElementById('loc-incidents');
        container.innerHTML = '';

        const btnSub = document.getElementById('btn-subscribe');
        if (this.currentUser.subscriptions.includes(city)) {
            btnSub.innerHTML = this.t('lbl_btn_unsubscribe');
            btnSub.className = 'btn btn-dark';
        } else {
            btnSub.innerHTML = this.t('lbl_btn_subscribe');
            btnSub.className = 'btn btn-outline';
        }

        const incidents = state.incidencias.filter(i => i.ciudad === city && i.estado === 'Activa');
        if (incidents.length === 0) {
            container.innerHTML = '<p>No hay incidencias registradas en esta ubicación.</p>';
            return;
        }

        incidents.forEach(inc => {
            container.appendChild(this.createIncidentCard(inc, false));
        });
    },

    toggleSubscription() {
        const city = document.getElementById('loc-title').textContent;
        const subs = this.currentUser.subscriptions;
        const index = subs.indexOf(city);
        
        if (index > -1) {
            subs.splice(index, 1);
        } else {
            subs.push(city);
        }
        
        this.renderLocationDetail(city);
    },

    renderIncidentDetail(id) {
        const inc = state.incidencias.find(i => i.id === id);
        if(!inc) return;

        const titleCard = document.getElementById('inc-detail-card');
        titleCard.className = `card title-card ${this.getColorForLevel(inc.nivel)}`;

        const localNivel = inc.nivel === 'Emergencia' ? this.t('lvl_emergency') : this.t('lvl_alert');
        document.getElementById('inc-detail-title').textContent = `${inc.ciudad}, Nivel ${localNivel}: ${inc.titulo}`;
        document.getElementById('inc-detail-desc').textContent = inc.descripcion;
        document.getElementById('inc-detail-loc').textContent = inc.ciudad;
        document.getElementById('inc-detail-rec').textContent = inc.recomendacion;
        document.getElementById('inc-detail-date').textContent = inc.fecha;
    },

    renderIncidentManageList() {
        const container = document.getElementById('incident-manage-list');
        container.innerHTML = '';

        if (state.incidencias.length === 0) {
            container.innerHTML = '<p>No hay incidencias creadas.</p>';
            return;
        }

        state.incidencias.forEach(inc => {
            const div = document.createElement('div');
            div.className = `card ${this.getColorForLevel(inc.nivel)}`;
            div.innerHTML = `
                <div class="card-content">
                    <i class="fa-solid fa-circle-info card-icon"></i>
                    <div class="card-text">
                        <strong>${inc.ciudad}. ${inc.titulo} (${inc.estado})</strong>
                        <p>${inc.fecha}</p>
                    </div>
                </div>
                <div class="report-actions right-align" style="margin-top:10px; justify-content: flex-end;">
                    <i class="fa-solid fa-box-archive" onclick="app.archiveIncident(${inc.id}, event)"></i>
                    <i class="fa-solid fa-trash-can" onclick="app.deleteIncident(${inc.id}, event)"></i>
                    <i class="fa-solid fa-pen-to-square" onclick="app.openIncidentForm(${inc.id}, event)"></i>
                </div>
            `;
            container.appendChild(div);
        });
    },

    archiveIncident(id, event) {
        event.stopPropagation();
        if(confirm('¿Desea desestimar/archivar esta incidencia?')) {
            const index = state.incidencias.findIndex(i => i.id == id);
            if (index !== -1) {
                state.incidencias[index].estado = 'Desestimada';
                this.renderIncidentManageList();
            }
        }
    },

    openIncidentForm(id = null, event = null) {
        if(event) event.stopPropagation();
        
        const form = document.querySelector('#view-incident-form form');
        form.reset();

        if (id) {
            currentEditIncidentId = id;
            const inc = state.incidencias.find(i => i.id === id);
            if (inc) {
                document.getElementById('inc-id').value = inc.id;
                document.getElementById('inc-loc').value = inc.ciudad;
                document.getElementById('inc-title').value = inc.titulo;
                document.getElementById('inc-desc').value = inc.descripcion;
                document.getElementById('inc-status').value = inc.estado;
                document.getElementById('inc-level').value = inc.nivel;
                document.getElementById('inc-rec').value = inc.recomendacion;
            }
        } else {
            currentEditIncidentId = null;
            document.getElementById('inc-id').value = '';
        }
        this.navigate('view-incident-form');
    },

    saveIncident() {
        const idVal = document.getElementById('inc-id').value;
        const newInc = {
            ciudad: document.getElementById('inc-loc').value,
            titulo: document.getElementById('inc-title').value,
            descripcion: document.getElementById('inc-desc').value,
            estado: document.getElementById('inc-status').value,
            nivel: document.getElementById('inc-level').value,
            recomendacion: document.getElementById('inc-rec').value,
        };

        if (idVal) {
            const index = state.incidencias.findIndex(i => i.id == idVal);
            if (index !== -1) {
                state.incidencias[index] = { ...state.incidencias[index], ...newInc };
            }
        } else {
            newInc.id = Date.now();
            const d = new Date();
            newInc.fecha = `${String(d.getDate()).padStart(2,'0')}-${String(d.getMonth()+1).padStart(2,'0')}-${d.getFullYear()}`;
            state.incidencias.unshift(newInc);
        }

        this.navigate('view-incident-list');
    },

    deleteIncident(id, event) {
        event.stopPropagation();
        if(confirm('¿Está seguro de que desea borrar esta incidencia permanentemente?')) {
            state.incidencias = state.incidencias.filter(i => i.id !== id);
            this.renderIncidentManageList();
        }
    },

    // --- CRUD INFORMES (Autoridad) ---
    renderReportList() {
        const container = document.getElementById('report-list-container');
        container.innerHTML = '';

        if (state.informes.length === 0) {
            container.innerHTML = '<p>No hay informes creados.</p>';
            return;
        }

        state.informes.forEach(rep => {
            const div = document.createElement('div');
            div.className = `report-card card-blue`;
            div.style.marginBottom = "15px";
            div.innerHTML = `
                <div class="report-header">
                    <div class="report-title-area">
                        <i class="fa-regular fa-circle-check"></i>
                        <div>
                            <strong>${rep.titulo} - ${rep.ciudad}</strong><br>
                            <span class="text-muted">Autor: ${rep.autor} (${rep.tipo})</span>
                        </div>
                    </div>
                </div>
                <div class="report-actions" style="margin-top:10px; justify-content: flex-end;">
                    <i class="fa-solid fa-eye" onclick="app.navigate('view-report-detail', { id: ${rep.id} })" title="Ver Informe"></i>
                    <i class="fa-solid fa-pen-to-square" onclick="app.openReportForm(${rep.id})" title="Editar Informe"></i>
                    <i class="fa-solid fa-trash-can" onclick="app.deleteReport(${rep.id})" title="Borrar"></i>
                </div>
            `;
            container.appendChild(div);
        });
    },

    openReportForm(id = null) {
        const form = document.querySelector('#view-report-form form');
        form.reset();
        
        if (id) {
            const rep = state.informes.find(i => i.id === id);
            if (rep) {
                document.getElementById('report-id').value = rep.id;
                document.getElementById('report-start').value = rep.inicio;
                document.getElementById('report-end').value = rep.fin;
                document.getElementById('report-type').value = rep.tipo;
                document.getElementById('report-loc').value = rep.ciudad;
            }
        } else {
            document.getElementById('report-id').value = '';
        }
        
        this.navigate('view-report-form');
    },

    saveReport() {
        const newRep = {
            id: Date.now(),
            autor: this.currentUser.username,
            titulo: `IF-${Math.floor(Math.random() * 10000).toString().padStart(4, '0')}`,
            inicio: document.getElementById('report-start').value,
            fin: document.getElementById('report-end').value,
            tipo: document.getElementById('report-type').value,
            ciudad: document.getElementById('report-loc').value,
        };

        // Generar datos coherentes basados en el tipo
        if (newRep.tipo === "Volumen de Alertas") {
            const r = Math.floor(Math.random() * 40) + 10;
            const y = 100 - r;
            newRep.stats = { red: r, yellow: y };
            newRep.desc = `El informe revela un aumento en las incidencias en ${newRep.ciudad} entre el ${newRep.inicio} y el ${newRep.fin}. Se emitieron múltiples notificaciones preventivas.`;
        } else if (newRep.tipo === "Resumen de Daños") {
            newRep.stats = { red: 90, yellow: 10 };
            const cost = (Math.random() * 10).toFixed(1);
            newRep.desc = `Los daños infraestructurales en la ubicación de ${newRep.ciudad} se estiman en ${cost}M de euros tras los últimos sucesos meteorológicos severos. Las autoridades locales han sido notificadas.`;
        } else {
            newRep.stats = { red: 30, yellow: 70 };
            newRep.desc = `Evolución climática registrada para ${newRep.ciudad}. Las tendencias muestran un cambio en los patrones habituales para esta época del año.`;
        }

        state.informes.unshift(newRep);
        this.navigate('view-report-list');
    },

    deleteReport(id) {
        if(confirm('¿Borrar este informe?')) {
            state.informes = state.informes.filter(i => i.id !== id);
            this.renderReportList();
        }
    },

    renderReportDetail(id) {
        const rep = state.informes.find(i => i.id === id);
        if(!rep) return;

        document.getElementById('view-rep-title').textContent = `${rep.titulo}: ${rep.tipo}`;
        document.getElementById('view-rep-meta').textContent = `${rep.ciudad} | Periodo: ${rep.inicio} a ${rep.fin} | Autor: ${rep.autor}`;
        
        // Cargar texto
        document.getElementById('view-rep-text').innerHTML = `
            <p style="margin-bottom:10px;"><strong>Análisis del sistema:</strong></p>
            <p>${rep.desc}</p>
        `;

        // Modificar el gráfico (porcentajes generados)
        const chartObj = document.getElementById('rep-pie-chart');
        const labelsObj = document.getElementById('rep-chart-labels');
        
        chartObj.style.background = `conic-gradient(#FBC582 0deg ${rep.stats.red * 3.6}deg, #FDE8A5 ${rep.stats.red * 3.6}deg 360deg)`;
        
        labelsObj.innerHTML = `
            <span style="color: #FBC582; font-size: 14px; font-weight:bold; display:block; margin-bottom:5px;">${rep.stats.red}% - Nivel Emergencia (Naranja)</span>
            <span style="color: #FDE8A5; font-size: 14px; font-weight:bold; display:block; margin-bottom:5px; background:rgba(0,0,0,0.4); padding:2px; border-radius:4px;">${rep.stats.yellow}% - Nivel Alerta (Amarillo)</span>
        `;
    },

    populateSelects() {
        const repLoc = document.getElementById('report-loc');
        const incLoc = document.getElementById('inc-loc');
        
        let options = state.ciudades.map(c => `<option value="${c}">${c}</option>`).join('');
        if(repLoc) repLoc.innerHTML = options;
        if(incLoc) incLoc.innerHTML = options;
    }
};

document.addEventListener('DOMContentLoaded', () => {
    app.init();
});
