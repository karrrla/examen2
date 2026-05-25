# Examen 2 - Aplicación de Notificaciones Push con Firebase

**Instituto Politécnico Nacional - Escuela Superior de Cómputo**  
**Desarrollo de Aplicaciones Móviles Nativas**  
**Ingeniería en Sistemas Computacionales - Plan 2020**

## Integrantes
- Karla Sofia Saavedra Mata
- Brian Ricardo Bernal Ramírez || 2023630387

## Descripción
Aplicación móvil nativa en Android desarrollada en Kotlin que implementa un sistema de autenticación mediante Firebase y envío de notificaciones push. La aplicación gestiona dos tipos de usuarios (normal y administrador) con diferentes niveles de acceso.

## Tecnologías utilizadas
- **Lenguaje:** Kotlin
- **IDE:** Android Studio
- **Base de datos:** Cloud Firestore
- **Autenticación:** Firebase Authentication (correo/contraseña)
- **Notificaciones:** Firebase Cloud Messaging (FCM) con HTTP v1 API
- **Arquitectura:** Activities con ViewBinding

## Funcionalidades implementadas

### Sistema de autenticación
- Registro e inicio de sesión con Firebase Authentication
- Dos roles de usuario: normal y administrador
- Registro de administrador protegido por contraseña maestra
- Persistencia de sesión
- Cierre de sesión

### Gestión de usuarios
- Visualización y edición de perfil personal (nombre, teléfono)
- Lista de usuarios registrados (vista exclusiva de administrador)

### Sistema de notificaciones push
- Envío de notificaciones desde el panel de administrador mediante FCM HTTP v1 API
- Selección de destinatarios: usuario específico, varios seleccionados o todos los usuarios
- Composición de mensaje personalizado (título y cuerpo)
- Recepción de notificaciones push en la barra de estado
- Historial de notificaciones recibidas almacenado en Firestore

## Estructura del proyecto
```
app/src/main/java/com/escom/examenfirebase/
├── App.kt                              # Application (canal de notificaciones)
├── SplashActivity.kt                   # Splash y persistencia de sesión
├── auth/
│   ├── LoginActivity.kt                # Inicio de sesión
│   └── RegisterActivity.kt             # Registro con opción de admin
├── user/
│   ├── UserHomeActivity.kt             # Pantalla principal usuario
│   ├── ProfileActivity.kt              # Edición de perfil
│   ├── NotificationsHistoryActivity.kt # Historial de notificaciones
│   └── NotificationsAdapter.kt
├── admin/
│   ├── AdminHomeActivity.kt            # Panel de administrador
│   ├── UsersListActivity.kt            # Lista de usuarios registrados
│   ├── SendNotificationActivity.kt     # Envío de notificaciones
│   └── SelectableUsersAdapter.kt       # Selección múltiple de usuarios
├── notifications/
│   ├── MyFirebaseMessagingService.kt    # Servicio de recepción FCM
│   └── FcmSender.kt                    # Envío vía HTTP v1 API
├── model/
│   ├── User.kt                         # Modelo de usuario
│   └── NotificationItem.kt             # Modelo de notificación
└── utils/
    └── Constants.kt                     # Configuración y constantes
```

## Requisitos para ejecutar
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Emulador o dispositivo con Android 7.0+ y Google Play Services
- Proyecto configurado en Firebase Console con Authentication, Firestore y FCM habilitados
