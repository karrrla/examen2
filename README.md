# Examen Firebase - App Android

App nativa Android con Firebase Authentication, Firestore y FCM (HTTP v1).

## Requisitos previos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Cuenta de Google (para Firebase)

## CONFIGURACIÓN DE FIREBASE (OBLIGATORIO antes de correr)

### Paso 1: Crear proyecto en Firebase
1. Ve a https://console.firebase.google.com/
2. Click en "Agregar proyecto"
3. Nombre: `ExamenFirebaseApp` (o el que quieras)
4. Desactiva Google Analytics si quieres (no se necesita)
5. Crear proyecto

### Paso 2: Registrar la app Android
1. En tu proyecto Firebase, click en el icono Android
2. **Nombre del paquete:** `com.escom.examenfirebase` (debe ser EXACTAMENTE este)
3. Apodo: el que quieras
4. SHA-1: opcional, déjalo en blanco
5. Click en "Registrar app"
6. **Descarga `google-services.json`** y pégalo en la carpeta `app/` del proyecto
   (al mismo nivel que `build.gradle` de la app)
7. Saltarse el resto de pasos del wizard, ya está hecho en código

### Paso 3: Habilitar Authentication
1. En Firebase Console > Authentication > Get Started
2. Pestaña "Sign-in method"
3. Habilitar "Correo electrónico/Contraseña"
4. Guardar

### Paso 4: Crear Firestore Database
1. Firebase Console > Firestore Database > Crear base de datos
2. Modo: **Producción** (después cambiamos reglas)
3. Ubicación: la más cercana (ej. `nam5` o `us-central`)
4. Habilitar

### Paso 5: Reglas de Firestore
En Firestore > Reglas, pegar:
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
      allow create: if request.auth != null;
    }
    match /notifications/{notifId} {
      allow read, write: if request.auth != null;
    }
  }
}
```
Publicar.

### Paso 6: Service Account (para enviar push)
1. Firebase Console > ⚙ (engrane) > Configuración del proyecto
2. Pestaña "Cuentas de servicio"
3. Click "Generar nueva clave privada" > "Generar clave"
4. Se descarga un archivo .json (la SERVICE ACCOUNT)
5. **Abrir ese .json en un editor de texto**
6. Abrir `app/src/main/java/com/escom/examenfirebase/utils/Constants.kt`
7. Reemplazar el valor de `FIREBASE_PROJECT_ID` con tu Project ID (lo ves en el .json como `"project_id"`)
8. Reemplazar TODO el contenido de `SERVICE_ACCOUNT_JSON` con el contenido del .json descargado
   (pegar el JSON completo entre las triple comillas `"""..."""`)

### Paso 7: Abrir en Android Studio
1. Android Studio > Open > seleccionar la carpeta `ExamenFirebaseApp`
2. Esperar a que Gradle sincronice (la primera vez tarda)
3. Conectar un emulador o dispositivo físico con Google Play Services
4. Run ▶️

## CONTRASEÑA MAESTRA DE ADMIN
La contraseña maestra para registrar administradores es: **`ESCOM2026`**
(definida en `app/src/main/res/values/strings.xml` como `admin_master_password`)

## CÓMO PROBAR

1. **Registrar un usuario normal**: en la pantalla de registro, llenar datos, NO activar el switch de admin.
2. **Registrar un administrador**: en la pantalla de registro, activar el switch "Registrar como administrador" y poner la contraseña maestra `ESCOM2026`.
3. **Login**: con cualquiera de las dos cuentas.
4. Como admin, ve a "Enviar notificación", selecciona usuarios (o activa "Enviar a todos") y envía.
5. La notificación llega como push al dispositivo del destinatario y también queda guardada en su historial.

## ESTRUCTURA DEL PROYECTO

```
app/src/main/java/com/escom/examenfirebase/
├── App.kt                          # Application class (canal de notif)
├── SplashActivity.kt               # Pantalla inicial / persistencia de sesión
├── auth/
│   ├── LoginActivity.kt
│   └── RegisterActivity.kt         # con switch de admin + contraseña maestra
├── user/
│   ├── UserHomeActivity.kt         # Home usuario normal
│   ├── ProfileActivity.kt          # Ver y editar perfil
│   ├── NotificationsHistoryActivity.kt
│   └── NotificationsAdapter.kt
├── admin/
│   ├── AdminHomeActivity.kt        # Home admin
│   ├── UsersListActivity.kt        # Lista de todos los usuarios
│   ├── UsersAdapter.kt
│   ├── SendNotificationActivity.kt # Panel de envío
│   └── SelectableUsersAdapter.kt
├── notifications/
│   ├── MyFirebaseMessagingService.kt  # Recepción FCM
│   └── FcmSender.kt                # Envío HTTP v1
├── model/
│   ├── User.kt
│   └── NotificationItem.kt
└── utils/
    └── Constants.kt                # CONFIGURAR AQUÍ el service account JSON
```

## NOTAS DE SEGURIDAD (importante saberlo)

El envío directo de notificaciones desde la app incluye el JSON de la service account
dentro del APK. Esto funciona para una práctica/demo, pero NO es seguro en producción
porque cualquiera que descompile la app puede extraer las credenciales.

En un sistema real, esto se hace con Cloud Functions actuando como intermediario,
como sugiere el documento del examen. Aquí se hizo así para evitar requerir el plan
Blaze de Firebase y simplificar la entrega.
