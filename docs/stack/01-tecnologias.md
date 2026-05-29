# Stack Tecnológico

## Plataforma base

| Tecnología | Versión | Rol |
|-----------|---------|-----|
| **Kotlin Multiplatform (KMP)** | 2.x | Compartir código entre Android e iOS |
| **Compose Multiplatform** | 1.7.x | UI declarativa multiplataforma |
| **Android** (minSdk 24, targetSdk 35) | — | Plataforma Android |
| **iOS** (deploymentTarget 16.0) | — | Plataforma iOS |
| **JVM Target** | 11 | Compilación Android |

---

## Frameworks y librerías

### Interfaz de usuario
| Librería | Uso |
|---------|-----|
| `compose.material3` | Sistema de diseño principal (MD3) |
| `compose.material` | Componentes legados coexistentes (M2) |
| `compose.materialIconsExtended` | Íconos de Material |
| `compose.components.resources` | Fuentes, imágenes, strings multiplataforma |
| `coil-compose` + `coil-network-ktor` | Carga de imágenes remotas (fotos de perfil, etc.) |

### Navegación
| Librería | Uso |
|---------|-----|
| `navigation-compose` | Navegación entre pantallas con `NavHost` + rutas |

### Networking
| Librería | Uso |
|---------|-----|
| `ktor-client-core` | Cliente HTTP multiplataforma |
| `ktor-client-okhttp` | Engine para Android |
| `ktor-client-darwin` | Engine para iOS |
| `ktor-client-content-negotiation` | Negociación de contenido JSON |
| `ktor-serialization-kotlinx-json` | Serialización JSON con `kotlinx.serialization` |

### Inyección de dependencias
| Librería | Uso |
|---------|-----|
| `koin-core` | DI multiplataforma |
| `koin-android` | Integración Android |
| `koin-androidx-compose` | `koinViewModel()` en Compose |
| `koin-compose-viewmodel` | Soporte KMP para ViewModels |

### Ciclo de vida y estado
| Librería | Uso |
|---------|-----|
| `androidx.lifecycle.viewmodelCompose` | `ViewModel` en Compose |
| `androidx.lifecycle.runtimeCompose` | `collectAsStateWithLifecycle()` |
| `androidx.lifecycle.process` | Lifecycle en Android |
| `kotlinx.coroutines.core` | `StateFlow`, `Channel`, coroutines |

### Utilidades
| Librería | Uso |
|---------|-----|
| `kotlinx.datetime` | Manejo de fechas y horas multiplataforma |
| `kotlinx.serialization` | Serialización JSON de DTOs |

### Autenticación
| Librería | Plataforma | Uso |
|---------|-----------|-----|
| `MSAL Android` v5.2.0 | Android | Microsoft Identity Platform |
| `MSAL iOS` v2.7.0 (CocoaPod) | iOS | Microsoft Identity Platform |

---

## Persistencia local

Se usa `SettingsStorage` con patrón `expect/actual`:
- **Android**: `SharedPreferences` o `DataStore` (implementación actual).
- **iOS**: `NSUserDefaults` (implementación actual).

No se usa SQLDelight actualmente — los datos de sesión se guardan en `SettingsStorage`.

---

## Arquitectura de red

```
ViewModel
  └── AppRepository (interfaz)
        └── AppRepositoryImpl
              └── HttpClient (Ktor)
                    └── BASE_ROOT_INTRANET (Constantes.kt)
                          └── /saa-rest/webresources/intranetSAA/
```

Todos los endpoints usan `ignoreUnknownKeys = true` para tolerancia ante cambios de API. Todos los campos de los DTOs tienen valores por defecto para evitar `MissingFieldException`.

---

## Entornos configurados (Constantes.kt)

| Variable | Desarrollo                 | Producción |
|---------|----------------------------|-----------|
| `BASE_ROOT_INTRANET` | `http://74.249.92.43:8080` | `http://sslcbpopen.eastus2.cloudapp.azure.com:8080` |
| `BASE_DOMAIN` | `https://devalumno`        | `https://alumno` |
| `ENV_DOMAIN` | `"demo"`                   | `"production"` |
| `ID_SISTEMA` | `24` (Universidad Android) | `24` |
| `RECIPIENT_DOMAIN` | `"ILP"` (Universidad)      | `"ILP"` |

---

## Herramientas de desarrollo

| Herramienta | Uso |
|------------|-----|
| Android Studio / IntelliJ IDEA | IDE principal |
| Xcode | Build y debug iOS |
| CocoaPods | Manejo de dependencias iOS nativas (MSAL) |
| Gradle (KTS) | Build system Android/Common |
| Git + GitHub | Control de versiones |
