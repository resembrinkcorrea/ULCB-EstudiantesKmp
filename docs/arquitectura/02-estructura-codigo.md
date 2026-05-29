# Estructura de Carpetas y Capas

## Módulos de código fuente

El proyecto es **Kotlin Multiplatform**. El código se divide en tres source sets:

| Source Set | Plataforma | Qué contiene |
|------------|-----------|--------------|
| `commonMain` | Android + iOS | Toda la lógica de negocio, UI, ViewModels, repositorios |
| `androidMain` | Android | Implementaciones `actual`, Previews, MSAL Android |
| `iosMain` | iOS | Implementaciones `actual`, MSAL iOS |

---

## Árbol de carpetas — commonMain

```
composeApp/src/commonMain/kotlin/
└── pe/lecordonbleu/universidadestudiante/
    │
    ├── core/
    │   ├── config/
    │   │   └── Constantes.kt          ← URLs, IDs de sistema, dominios
    │   ├── di/
    │   │   └── AppModule.kt           ← Módulo Koin (provee HttpClient, AppRepository, VMs)
    │   ├── extensions/
    │   │   ├── DrawExtensions.kt
    │   │   └── LoginTextureOverlay.kt
    │   ├── theme/
    │   │   ├── IlcbColor.kt           ← Tokens de color (fuente única de verdad)
    │   │   ├── IlcbColorScheme.kt     ← Light + Dark ColorScheme MD3
    │   │   ├── IlcbShapes.kt          ← Shapes MD3
    │   │   ├── IlcbTheme.kt           ← Composable raíz del tema
    │   │   └── IlcbTypography.kt      ← expect fun ilcbTypography()
    │   └── utils/
    │       └── NetworkUtils.kt
    │
    ├── data/
    │   ├── remote/
    │   │   └── dto/                   ← DTOs @Serializable (mapean la API)
    │   │       ├── ResponseLoginUser.kt
    │   │       ├── ResponseDataMenu.kt
    │   │       ├── ResponseHora.kt
    │   │       └── ...
    │   └── repository/
    │       ├── RepoImpl.kt            ← Impl para login (inyección manual)
    │       └── AppRepositoryImpl.kt   ← Impl centralizada (Koin)
    │
    ├── domain/
    │   ├── model/                     ← Entidades de negocio (Kotlin puro)
    │   │   ├── UserLoginRequest.kt
    │   │   ├── UserMenuRequest.kt
    │   │   └── ...
    │   └── repository/
    │       ├── Repository.kt          ← Interfaz del login
    │       └── AppRepository.kt       ← Interfaz centralizada
    │
    ├── presentation/
    │   ├── components/                ← Composables reutilizables
    │   │   ├── AppComponents.kt
    │   │   └── Carousel.kt
    │   ├── navigation/
    │   │   └── Navigator.kt           ← NavHost + rutas
    │   ├── screens/
    │   │   ├── login/
    │   │   │   ├── LoginScreen.kt
    │   │   │   └── LoginViewModel.kt
    │   │   ├── home/
    │   │   │   ├── HomeScreen.kt
    │   │   │   ├── HomeViewModel.kt
    │   │   │   └── customcell/        ← Composables propios del home
    │   │   └── onboarding/
    │   │       └── OnBoardingScreen.kt
    │   └── vo/
    │       ├── ResourceUiState.kt
    │       └── ImagesCarousel.kt
    │
    ├── appcomunication/
    │   └── GetDeviceInformation.kt    ← expect/actual: info del dispositivo
    │
    ├── App.kt                         ← Entry point Compose
    ├── AppTheme.kt                    ← AppTheme{} + getColorsTheme() + DarkModeColors
    ├── MicrosoftLogin.kt              ← expect/actual: autenticación MSAL
    ├── SettingsStorage.kt             ← expect/actual: persistencia local
    └── Platform.kt                    ← expect/actual: nombre de plataforma
```

---

## Archivos platform-specific relevantes

```
androidMain/
├── kotlin/.../core/theme/IlcbTypography.android.kt   ← Roboto + Rajdhani + MyriadPro
├── kotlin/.../MicrosoftLogin.android.kt
├── kotlin/.../presentation/screens/login/LoginScreenPreview.kt
└── res/raw/auth_config.json                           ← Config MSAL Android

iosMain/
├── kotlin/.../core/theme/IlcbTypography.ios.kt        ← SF Pro (FontFamily.Default)
└── kotlin/.../MicrosoftLogin.ios.kt

iosApp/iosApp/
├── ContentView.swift                                  ← .ignoresSafeArea() aplicado
└── iOSApp.swift
```

---

## Convenciones de nombrado

| Tipo | Convención | Ejemplo |
|------|-----------|---------|
| Screen | `NombreScreen.kt` | `HomeScreen.kt` |
| ViewModel | `NombreViewModel.kt` | `HomeViewModel.kt` |
| DTO | `ResponseNombre.kt` | `ResponseLoginUser.kt` |
| Request model | `NombreRequest.kt` | `UserMenuRequest.kt` |
| Repository interface | `Repository.kt` / `AppRepository.kt` | — |
| Repository impl | `RepoImpl.kt` / `AppRepositoryImpl.kt` | — |
| Custom cell | dentro de `customcell/` | `AulaDemoDialog.kt` |
