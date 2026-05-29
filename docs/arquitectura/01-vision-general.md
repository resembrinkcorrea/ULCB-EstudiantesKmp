# Arquitectura — Visión General

## Patrón: Clean Architecture + MVVM

El proyecto aplica **Clean Architecture** con separación estricta en capas. Las dependencias siempre apuntan **hacia adentro** (hacia el Domain). Ninguna capa interna conoce detalles de implementación de una capa exterior.

```
┌─────────────────────────────────────────────┐
│                  UI Layer                   │  ← Compose Multiplatform (Screens)
│            Stateful / Stateless             │
├─────────────────────────────────────────────┤
│            Presentation Layer               │  ← ViewModels (androidx.lifecycle)
│         MutableStateFlow + Events           │
├─────────────────────────────────────────────┤
│               Domain Layer                  │  ← Interfaces de Repositorio + Entities
│         (puro Kotlin, sin frameworks)       │
├─────────────────────────────────────────────┤
│                Data Layer                   │  ← Repositorios Impl + DTOs + Ktor
│          (implementaciones reales)          │
└─────────────────────────────────────────────┘
```

---

## Las cuatro capas

### 1. Data Layer
Responsabilidad: comunicarse con el mundo exterior (API REST, almacenamiento local).

- **DTOs** (`data/remote/dto/`): clases `@Serializable` que mapean la respuesta JSON de la API.
- **Repository Impl** (`data/repository/`): implementaciones concretas de las interfaces del Domain.
  - `RepoImpl` — para el flujo de login (inyección manual).
  - `AppRepositoryImpl` — repositorio centralizado para todos los ViewModels Koin.
- **Ktor Client**: HTTP con `ContentNegotiation` + `kotlinx.serialization`.

### 2. Domain Layer
Responsabilidad: contener las reglas de negocio. No importa nada de Ktor, Koin ni Compose.

- **Entities / Models** (`domain/model/`): clases de datos puras de Kotlin.
- **Repository Interfaces** (`domain/repository/`):
  - `Repository` — interfaz del login.
  - `AppRepository` — interfaz centralizada para el resto de módulos.

### 3. Presentation Layer
Responsabilidad: manejar el estado de la UI y los eventos de negocio.

- Hereda de `androidx.lifecycle.ViewModel`.
- Estado inmutable: `private val _state = MutableStateFlow<ResourceUiState<T>>`; expone `state.asStateFlow()`.
- Expone `resetUiState()` para limpiar al navegar hacia atrás.
- Acciones únicas (navegación, alertas) via `Channel` / `SharedFlow` para evitar *side-effect hell*.

### 4. UI Layer
Responsabilidad: dibujar la pantalla y reaccionar al estado.

- **Stateful Composables**: conectados al ViewModel, reciben `uiState` y delegan a stateless.
- **Stateless Composables**: puramente declarativos, reutilizables, sin dependencias de VM.
- Observan estado con `collectAsStateWithLifecycle()`.

---

## Estado de UI estandarizado

```kotlin
sealed interface ResourceUiState<out T> {
    data object Empty   : ResourceUiState<Nothing>
    data object Loading : ResourceUiState<Nothing>
    data class  Success<T>(val data: T) : ResourceUiState<T>
    data class  Error(val message: String) : ResourceUiState<Nothing>
}
```

El bloque `when(uiState)` controla tanto el render visual como los efectos secundarios (navegación, dialogs).

---

## Principios aplicados

| Principio | Cómo se aplica |
|-----------|----------------|
| **Single Responsibility** | Un ViewModel por pantalla; un Repositorio por dominio de datos |
| **Dependency Inversion** | ViewModels dependen de interfaces (`AppRepository`), no de `AppRepositoryImpl` |
| **DRY** | Lógica de validación centralizada; componentes UI reutilizables en `presentation/components/` |
| **Inmutabilidad** | `StateFlow` privado mutable, público de solo lectura |
