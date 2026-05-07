// app.js - IBERALERT Prototype Dynamic Logic (Roles & Subscriptions)

// --- STATE MANAGEMENT ---
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
        { id: 4, ciudad: "Sevilla, Andalucía", titulo: "Lluvias torrenciales", nivel: "Emergencia", descripcion: "Fuertes precipitaciones esperadas durante la tarde.", estado: "Activa", recomendacion: "Conduzca con precaución, riesgo de balsas de agua en la vía.", fecha: "08-04-2026" },
        { id: 5, ciudad: "Valladolid, Castilla y León", titulo: "Vientos huracanados", nivel: "Alerta", descripcion: "Rachas de viento superando los 100km/h.", estado: "Desestimada", recomendacion: "Retire macetas y objetos de los balcones.", fecha: "07-04-2026" },
        { id: 6, ciudad: "Barcelona, Cataluña", titulo: "Ola de Calor", nivel: "Alerta", descripcion: "Temperaturas extremadamente altas superando los 40 grados.", estado: "Activa", recomendacion: "Manténgase hidratado y no salga a las horas centrales del día.", fecha: "10-04-2026" }
    ],
    informes: [
        { id: 1, autor: "admin", titulo: "IF-0001", inicio: "2026-04-01", fin: "2026-04-07", tipo: "Volumen de alertas (ASC)", ciudad: "Madrid, Madrid" },
        { id: 2, autor: "admin", titulo: "IF-0002", inicio: "2026-03-15", fin: "2026-03-30", tipo: "Resumen de daños", ciudad: "Cádiz, Andalucía" }
    ],
    controlPanel: {
        lastConnection: "09-04-2026 14:30:00"
    }
};

let currentEditIncidentId = null;
let currentEditReportId = null;

// --- INITIALIZATION ---
const app = {
    history: [],
    currentView: null,
    currentUser: null,

    init() {
        this.setupSearch();
        this.populateSelects();
        
        const lastConn = document.getElementById('last-conn-date');
        if(lastConn) lastConn.value = state.controlPanel.lastConnection;

        this.navigate('view-login');
    },

    // --- NAVIGATION & LOGIN ---
    openClaveForm() {
        document.getElementById('view-login').classList.remove('active');
        document.getElementById('view-clave-form').classList.add('active');
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
        
        // Hide sidebar links
        const adminLinks = document.querySelector('.admin-only');
        if (adminLinks) {
            adminLinks.style.display = isAdmin ? 'block' : 'none';
        }
        
        // Hide bottom nav incident creator for normal users
        const adminBottomNav = document.querySelector('.admin-only-flex');
        if (adminBottomNav) {
            adminBottomNav.style.display = isAdmin ? 'flex' : 'none';
        }
    },

    navigate(viewId, params = {}, replaceHistory = false) {
        // Bloquear acceso a vistas de admin si es usuario
        if (this.currentUser && this.currentUser.role === 'usuario') {
            if (viewId === 'view-report-list' || viewId === 'view-report-form' || 
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

        // Simplified active link tracking for sidebar (matching text)
        const currentTitleMapping = {
            'view-dashboard': 'Inicio',
            'view-subscriptions': 'Zonas Suscritas',
            'view-report-list': 'Informes',
            'view-incident-list': 'Gestión de Incidencias',
            'view-control-panel': 'Panel de Control',
            'view-settings': 'Ajustes',
            'view-help': 'Ayuda',
            'view-contact': 'Contacto',
            'view-privacy': 'Política de Privacidad'
        };

        const activeText = currentTitleMapping[viewId];
        sidebarLinks.forEach(link => {
            if (link.textContent.trim().includes(activeText)) {
                link.style.fontWeight = '600';
                link.style.backgroundColor = '#F0F0F0';
            }
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
    },

    renderDashboard() {
        const container = document.getElementById('dashboard-cards');
        container.innerHTML = '';
        
        // Show ONLY active incidents from SUBSCRIBED locations
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
        div.innerHTML = `
            <div class="card-content">
                <i class="fa-solid ${inc.nivel === 'Emergencia' ? 'fa-triangle-exclamation' : 'fa-circle-info'} card-icon"></i>
                <div class="card-text">
                    <strong>${inc.ciudad}. Nivel ${inc.nivel}: ${inc.titulo}</strong>
                    <p>${inc.descripcion}</p>
                </div>
            </div>
            ${forDashboard ? `<div class="card-footer"><span>Ver más ></span></div>` : ''}
        `;
        div.onclick = () => this.navigate('view-incident-detail', { id: inc.id });
        return div;
    },

    getColorForLevel(nivel) {
        // Simplified Schema: Emergencia (Naranja), Alerta (Amarillo)
        if (nivel === 'Emergencia') return 'card-orange';
        if (nivel === 'Alerta') return 'card-yellow';
        return 'card-blue'; // Fallback for reports
    },

    // --- BÚSQUEDA ---
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

    // --- DETALLES DE ZONA Y SUSCRIPCIONES ---
    renderLocationDetail(city) {
        document.getElementById('loc-title').textContent = city;
        document.getElementById('loc-desc').textContent = `Información general y estado de alertas para la ubicación de ${city}.`;
        
        const container = document.getElementById('loc-incidents');
        container.innerHTML = '';

        // Boton Suscripcion
        const btnSub = document.getElementById('btn-subscribe');
        if (this.currentUser.subscriptions.includes(city)) {
            btnSub.innerHTML = '<i class="fa-solid fa-heart-crack"></i> Desuscribirse';
            btnSub.className = 'btn btn-dark';
        } else {
            btnSub.innerHTML = '<i class="fa-regular fa-heart"></i> Suscribirse';
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
        
        // Refresh view to update button
        this.renderLocationDetail(city);
    },

    renderIncidentDetail(id) {
        const inc = state.incidencias.find(i => i.id === id);
        if(!inc) return;

        const titleCard = document.getElementById('inc-detail-card');
        titleCard.className = `card title-card ${this.getColorForLevel(inc.nivel)}`;

        document.getElementById('inc-detail-title').textContent = `${inc.ciudad}, Nivel ${inc.nivel}: ${inc.titulo}`;
        document.getElementById('inc-detail-desc').textContent = inc.descripcion;
        document.getElementById('inc-detail-loc').textContent = inc.ciudad;
        document.getElementById('inc-detail-rec').textContent = inc.recomendacion;
        document.getElementById('inc-detail-date').textContent = inc.fecha;
    },

    // --- CRUD INCIDENCIAS (Autoridad) ---
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
                    <i class="fa-solid fa-box-archive" onclick="app.archiveIncident(${inc.id}, event)" title="Desestimar/Archivar"></i>
                    <i class="fa-solid fa-trash-can" onclick="app.deleteIncident(${inc.id}, event)" title="Eliminar definitivamente"></i>
                    <i class="fa-solid fa-pen-to-square" onclick="app.openIncidentForm(${inc.id}, event)" title="Editar"></i>
                </div>
            `;
            container.appendChild(div);
        });
    },

    archiveIncident(id, event) {
        event.stopPropagation();
        if(confirm('¿Desea desestimar/archivar esta incidencia? Dejará de ser visible para los usuarios normales.')) {
            const index = state.incidencias.findIndex(i => i.id == id);
            if (index !== -1) {
                state.incidencias[index].estado = 'Desestimada';
                this.renderIncidentManageList();
            }
        }
    },

    openIncidentForm(id = null, event = null) {
        if(event) event.stopPropagation();
        
        const titleEl = document.getElementById('incident-form-title');
        const form = document.querySelector('#view-incident-form form');
        form.reset();

        if (id) {
            currentEditIncidentId = id;
            titleEl.textContent = "Editar Incidencia";
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
            titleEl.textContent = "Crear Incidencia";
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
            container.innerHTML = '<p>No hay informes creadas.</p>';
            return;
        }

        state.informes.forEach(rep => {
            const div = document.createElement('div');
            // Reports are always blue
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
                    <i class="fa-solid fa-trash-can" onclick="app.deleteReport(${rep.id})"></i>
                    <i class="fa-solid fa-pen-to-square" onclick="app.openReportForm(${rep.id})"></i>
                </div>
            `;
            container.appendChild(div);
        });
    },

    openReportForm(id = null) {
        const titleEl = document.getElementById('report-form-title');
        const form = document.querySelector('#view-report-form form');
        const chart = document.getElementById('report-chart');
        form.reset();
        chart.style.display = 'none';

        if (id) {
            currentEditReportId = id;
            titleEl.textContent = "Editar Informe";
            const rep = state.informes.find(i => i.id === id);
            if (rep) {
                document.getElementById('report-id').value = rep.id;
                document.getElementById('report-start').value = rep.inicio;
                document.getElementById('report-end').value = rep.fin;
                document.getElementById('report-type').value = rep.tipo;
                document.getElementById('report-loc').value = rep.ciudad;
                chart.style.display = 'flex';
            }
        } else {
            currentEditReportId = null;
            titleEl.textContent = "Crear Informe";
            document.getElementById('report-id').value = '';
        }
        this.navigate('view-report-form');
    },

    saveReport() {
        const idVal = document.getElementById('report-id').value;
        const newRep = {
            inicio: document.getElementById('report-start').value,
            fin: document.getElementById('report-end').value,
            tipo: document.getElementById('report-type').value,
            ciudad: document.getElementById('report-loc').value,
        };

        if (idVal) {
            const index = state.informes.findIndex(i => i.id == idVal);
            if (index !== -1) {
                state.informes[index] = { ...state.informes[index], ...newRep };
            }
        } else {
            newRep.id = Date.now();
            newRep.autor = this.currentUser.username;
            newRep.titulo = `IF-${Math.floor(Math.random() * 10000).toString().padStart(4, '0')}`;
            state.informes.unshift(newRep);
        }

        this.navigate('view-report-list');
    },

    deleteReport(id) {
        if(confirm('¿Borrar este informe?')) {
            state.informes = state.informes.filter(i => i.id !== id);
            this.renderReportList();
        }
    },

    // --- UTILS ---
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
