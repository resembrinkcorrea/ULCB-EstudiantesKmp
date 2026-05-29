# Inyección de Dependencias — Koin

## Librería utilizada: Koin

Se usa **Koin** (no Hilt, no Dagger) porque es compatible con Kotlin Multiplatform sin necesidad de procesamiento de anotaciones (KSP). Funciona en Android e iOS desde el mismo módulo `commonMain`.

---

## Módulo Koin (`core/di/AppModule.kt`)

El módulo provee:

```kotlin
val appModule = module {
    // HttpClient compartido (Ktor)
    single { HttpClient { ... } }

    // Repositorio centralizado
    single<AppRepository> { AppRepositoryImpl(get()) }

    // ViewModels inyectados con koinViewModel()
    viewModelOf(::HomeViewModel)
    // ... más VMs aquí al crecer el proyecto
}
```

---

## Dos patrones válidos de inyección

El proyecto coexiste con dos patrones. **No mezclar ni migrar** uno al otro sin necesidad.

### Patrón 1 — Inyección Manual (Login)

El ViewModel construye su propio repositorio. Se usa en el flujo de login porque la instancia del `HttpClient` viene de Koin pero el ViewModel no está en el grafo de Koin.

```kotlin
// Navigator.kt
val httpClient = koinInject<HttpClient>()
val loginVm: LoginViewModel = viewModel {
    LoginViewModel(RepoImpl(httpClient))
}
```

**Cuándo usar:** módulos de autenticación o flujos de entrada donde el ViewModel no necesita estar en el grafo de Koin.

---

### Patrón 2 — Inyección Koin (`koinViewModel`)

El ViewModel está declarado en `AppModule` y Koin resuelve todas sus dependencias automáticamente.

```kotlin
// Navigator.kt
val homeVm: HomeViewModel = koinViewModel()
```

**Cuándo usar:** todos los módulos post-login (Home, Notas, Asistencias, etc.).

---

## Repositorio centralizado

Existe **un único** `AppRepository` para todos los ViewModels Koin. Al agregar una nueva funcionalidad:

1. Agregar el método en `AppRepository` (interfaz).
2. Implementar en `AppRepositoryImpl`.
3. Llamar desde el ViewModel correspondiente.

> No crear un repositorio nuevo por módulo. Todo va en `AppRepository`.

---

## Flujo de dependencias

```
AppModule
  └── HttpClient (single)
        └── AppRepositoryImpl (single)
              └── HomeViewModel (viewModel)
              └── [FuturoViewModel] (viewModel)

  RepoImpl ← construido manualmente en Navigator
        └── LoginViewModel
```

---

## Inicialización en cada plataforma

**Android** (`MainActivity.kt` / `Application`):
```kotlin
startKoin {
    modules(appModule)
}
```

**iOS** (`App.kt` / KoinApplication):
```kotlin
KoinApplication {
    modules(appModule)
}
```
