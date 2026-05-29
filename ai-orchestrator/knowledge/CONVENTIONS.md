# Conventions

## Commits
- Firma obligatoria en todo commit (rama o main): `Authored-By: Reloader - Resembrink Correa`
- Nunca usar `Co-Authored-By: Claude Sonnet 4.6` ni ninguna variante de Anthropic/Claude/AI
- No hacer commit ni push automáticamente — solo cuando se pide explícitamente
- Los mensajes de commit siempre en español
- **Antes de stagear archivos**: mostrar al usuario la lista de archivos modificados (git status). Stagear siempre todos en un solo commit — nunca preguntar cuáles incluir.
- **Antes de hacer commit/push**: preguntar al usuario el avance actual del módulo trabajado y actualizar `ai-orchestrator/context/PROJECT_CONTEXT.md` antes de proceder con el commit.

### Estructura de commit message

Todo commit debe tener tres partes:

```
tipo: descripción corta del cambio

- archivo o módulo: qué se hizo y por qué
- archivo o módulo: qué se hizo y por qué

Authored-By: Reloader - Resembrink Correa
```

**Título** → primera línea, aparece en el listado de commits de GitHub
**Cuerpo** → lista de lo que cambió, se ve al abrir el commit en detalle
**Firma** → última línea siempre, aplica en cualquier rama o main

Ejemplo:
```
feat: agrega ai-orchestrator como fuente de verdad operativa

- BOOTSTRAP.md: punto de entrada para cualquier instancia de IA
- MASTER_PROMPT.md: rol, flujo de decisión y reglas operativas
- knowledge/: arquitectura, convenciones y modelo operativo
- domains/: contexto por tecnología (kmp, api, azure, docker, n8n, postman)

Authored-By: Reloader - Resembrink Correa
```

## Git / Merge
- Al unir a main: siempre squash merge (`git merge --squash`), un solo commit limpio
- La rama feature se conserva intacta después del merge
- Nunca hacer merge, push directo ni ninguna operación sobre main sin instrucción explícita del usuario

### Nomenclatura de commits en main

| Operación | Formato |
|---|---|
| Squash merge de rama | `squash: feature/nombre-rama` |
| Merge normal (conserva historial) | `merge: feature/nombre-rama` |
| Revertir un commit | `revert: <mensaje del commit revertido>` |
| Trabajo directo en main | conventional commits (`feat:`, `fix:`, `chore:`, etc.) |

## Código
- No modificar código funcional existente sin que se pida explícitamente
- Al portar código entre proyectos: preservar estilo línea por línea, solo adaptar packages e imports
- No sugerir compilar ni ejecutar el proyecto

## Caracteres prohibidos en KMP — regla universal
Aplica a **todo**: nombres de archivos, clases, data classes, DTOs, entidades, funciones, variables, campos, constantes, packages.

**Nunca usar caracteres no ASCII:**
- Prohibido: `ñ`, `á`, `é`, `í`, `ó`, `ú`, `ü`, `%`, `-` en nombres, espacios
- Motivo: KMP compila a Kotlin Native para iOS — caracteres especiales generan errores de compilación o comportamiento inesperado
- Todo nombre debe usar únicamente letras A-Z, a-z, 0-9 y `_`

Ejemplos:
| Incorrecto | Correcto |
|---|---|
| `GrupoPestañaTarea` | `GrupoPestanaTarea` |
| `nombrePestaña` | `nombrePestana` |
| `EvaluaciónAcadémica.kt` | `EvaluacionAcademica.kt` |
| `not_exámen_final` | `not_examen_final` |

## Xcode
- Cuando el usuario pide limpiar DerivedData, ejecutar directamente con Bash:
  `rm -rf ~/Library/Developer/Xcode/DerivedData/*`
- No pedirle al usuario que lo haga manualmente

## Valores del servidor — fuente de verdad
El servidor es la única fuente de verdad sobre los valores que puede mandar un campo.
**Prohibido asumir** que un string vacío, `"NULL"`, `"-"`, `"0"` o cualquier otro valor centinela es equivalente a `null` de Kotlin o a un valor por defecto.

- `"NULL"` es un texto que manda el servidor — no es `null`
- `"-"` es un texto que manda el servidor — no es ausencia de dato en Kotlin
- Cada valor debe tratarse exactamente como llega, sin transformarlo ni inferir su semántica

Al portar desde nativo, leer el código del `Adapter` o `Activity` para identificar **todos** los valores posibles que puede mandar el servidor para cada campo, y mapearlos explícitamente.

## DTOs / Serialización (capa Data)
→ ver `domains/kmp.md`

## Estándares de código

### Estado de UI
- Usar `ResourceUiState<T>` con estados: `Empty`, `Loading`, `Success(data)`, `Error(message)`

### ViewModels
- Estado interno: `MutableStateFlow` privado + `asStateFlow()` público
- Exponer siempre `resetUiState()`
- Cada screen obtiene su propio VM en el `composable` del Navigator. Nunca centralizar ViewModels de múltiples módulos en un screen raíz.
- Los setters del ViewModel solo asignan el request y llaman al getter privado. Nunca resetean otros estados internamente.

**Responsabilidades separadas — regla crítica:**
- El **ViewModel es el orquestador de datos**: decide qué servicios llamar, en qué orden y cómo combinar resultados.
- El **Screen es el orquestador del flujo de UI**: decide cuándo limpiar, cuándo pedir y cuándo mostrar cada estado.
- Nunca mezclar estas responsabilidades. Si el VM emite `Empty` antes de `Loading` dentro de un setter, está tomando responsabilidad del flujo de UI — eso es un error de concepto. El VM solo ejecuta lo que el screen le ordena.
- Flujo correcto al cambiar contexto (ej. tab): screen llama `viewModel.resetXxxState()` → luego `viewModel.setXxxRequest(...)`. Nunca al revés.

**Patrones de ViewModel** (coexisten, no mezclar por módulo):
- Manual: `viewModel { MyViewModel(RepoImpl(httpClient)) }` — usar cuando el módulo nativo usa inyección manual (`VMFactory(RepoImpl(DataSource()))`)
- Koin: `koinViewModel()` — usar cuando el módulo nativo usa Koin

Para detectar el patrón al portar desde nativo: leer el `Activity` o `Fragment` del módulo nativo antes de implementar.

### Efectos secundarios — navegación desde respuesta API

Cuando el nativo navega dentro de un `observe { is Resource.Success → startActivity(...) }`, el KMP hace lo mismo dentro del `when`:

```kotlin
when (val s = uiStateXxx) {
    is ResourceUiState.Success -> {
        val cod = s.data.campo
        if (cod.isNotEmpty()) {
            navigator.navigate("/ruta/$cod")
            viewModel.resetXxxState()   // lleva estado a Empty → el when no re-entra en Success
        }
    }
    else -> {}
}
```

`resetXxxState()` es el guard. No se necesitan `Channel`, `SharedFlow`, ni `LaunchedEffect` adicionales.
No agregar complejidad donde el nativo no la tiene.

### UI / Compose
- Usar `collectAsStateWithLifecycle()` para observar estado
- Separar Composables Stateful vs Stateless
- No usar emojis ni íconos Unicode en textos de UI (labels, mensajes de error, estados vacíos, etc.) — usar texto plano siempre

### Contrato padre-hijo de composables

El Screen padre es el único que observa `uiState` de servicios y llama `viewModel.setXxx()` de forma reactiva.

Los hijos (`components/`, `customcell/`, sub-composables):
- **No** usan `collectAsState` ni `collectAsStateWithLifecycle`
- **No** tienen `LaunchedEffect` para llamar servicios o reaccionar a estados de servicio
- **No** tienen referencia directa al ViewModel
- Reciben datos como parámetros y emiten eventos de usuario via lambdas
- Un hijo puede recibir un lambda `onClick: () -> Unit` cuyo cuerpo en el padre llame a `viewModel.setXxx()` — el hijo no conoce el ViewModel

`LaunchedEffect` en hijos está permitido solo para efectos de UI puros (scroll, animación, foco, pager).

### Colores — estándar obligatorio

**Nunca usar `Color(0xFF...)` directamente para colores de marca.** Usar siempre las constantes nombradas de `universidadestudiante.core.theme.UlcbColor`:

| Constante | Hex | Uso |
|---|---|---|
| `UlcbBlueMD` | `#0D47A1` | Azul de acento en dialogs y encuestas (RadioButton, numeración, botones primarios) |
| `UlcbDialogBg` | `#F9F9F9` | Fondo de surface en dialogs |
| `UlcbGrisNeutro` | `#757575` | Botones secundarios / cancelar en dialogs |

Para colores de tema dinámico (dark/light mode) usar `getColorsTheme()` → `DarkModeColors`. Las constantes de `IlcbColor` son fijas y no cambian con el tema.

**Regla de detección:** Si ves `private val azulXxx = Color(0xFF0D47A1)` o cualquier `Color(0xFF...)` hardcodeado en un `@Composable`, reemplazarlo por la constante `UlcbColor` correspondiente e importar desde `core.theme`.

### Patrón de Screen (Single Responsibility — obligatorio)
Cada Screen es una sola función `@Composable` con tres secciones en orden estricto.
Cada sección tiene una única responsabilidad. No mezclar entre secciones.

---

**1. Variables y estados — solo declarar, nunca derivar de uiState**
```kotlin
val colors = getColorsTheme()
var enlaces by remember { mutableStateOf<List<ListLinksInstitucional>>(emptyList()) }
var showLoading by remember { mutableStateOf(false) }
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```
Prohibido:
```kotlin
// MAL — deriva datos del servicio en la declaración
val listaCarreras = (carreraUiState as? ResourceUiState.Success<...>)
    ?.data?.firstOrNull()?.carrera?.map { it.serv_nombre } ?: emptyList()
```

---

**2. UI — solo dibuja y dispara acciones al ViewModel**
La UI lee estados locales (`enlaces`, `showLoading`, etc.) y solo puede llamar:
- `viewModel.setXxx(...)` para lanzar una petición
- `viewModel.resetXxxState()` para limpiar un estado

```kotlin
Scaffold(...) {
    MyComboBox(
        items = listaCarreras,
        onItemSelected = { seleccion ->
            seleccionCarrera = seleccion          // actualizar estado local: ok
            viewModel.setPlanEstudioRequest(id)   // disparar al VM: ok
        }
    )
}
```
Prohibido dentro de la UI:
```kotlin
// MAL — castea uiState dentro de la UI
(carreraUiState as? ResourceUiState.Success<...>)?.data?.firstOrNull()?.carrera?.firstOrNull { ... }
```

---

**3. `when (uiState)` — fuera del árbol de UI, solo actualiza estados locales**
```kotlin
when (uiState) {
    is ResourceUiState.Loading  -> { showLoading = true }
    is ResourceUiState.Success  -> { showLoading = false; enlaces = (uiState as ResourceUiState.Success).data.ListLinksInstitucional }
    is ResourceUiState.Error    -> { showLoading = false }
    ResourceUiState.Empty       -> {}
}
```
Prohibido dentro del `when`:
```kotlin
// MAL — side effect dentro del when
is ResourceUiState.Loading -> { Toast.makeText(context, "Cargando...", Toast.LENGTH_SHORT).show() }

// MAL — navegación dentro del when
is ResourceUiState.Success -> { navigator.navigate("/home") }
```

---

Después del `when` pueden ir overlays condicionales (`LoadingIndicator`, diálogos, etc.) que solo leen estados locales.

**Patrón obligatorio para `CustomDialogBasic`:**
```kotlin
// CORRECTO — el if externo saca el composable del árbol cuando no se necesita
if (showDialogResultado) {
    CustomDialogBasic(
        visible = true,
        titulo = tituloDialog,
        mensaje = mensajeDialog,
        flag_val = flagValDialog,
        confirmado = flagValDialog == 1,
        onDismiss = {
            showDialogResultado = false
            viewModel.resetXxxState()
        }
    )
}

// MAL — el composable siempre existe en el árbol, causa recomposiciones innecesarias
CustomDialogBasic(
    visible = showDialogResultado,
    ...
)
```

---

### LaunchedEffect — solo para inicialización

**Un único `LaunchedEffect` de arranque** por Screen. Su único rol es disparar los servicios iniciales cuando cambian los parámetros de entrada.

```kotlin
LaunchedEffect(idSistema, idPerfil) {
    viewModel.fetchProyeccionValidacion(idEstud)
    viewModel.setUserMenuRequest(2, idSistema, idPerfil)
}
```

**Prohibido** crear `LaunchedEffect` adicionales para encadenar servicios o reaccionar a estados. Eso se resuelve con el patrón de gate booleano dentro del `when`.

---

### Patrón de servicios encadenados — gate booleano

Cuando un servicio depende del resultado de otro, **no usar `LaunchedEffect`**. Usar un estado local booleano como gate dentro del `when`:

```kotlin
// Sección 1 — Variables
var servicioALanzado by remember { mutableStateOf(false) }
var servicioBLanzado by remember { mutableStateOf(false) }

// Sección 3 — when encadenados
when (estadoA) {
    is ResourceUiState.Success -> {
        val data = (estadoA as ResourceUiState.Success).data
        if (!servicioALanzado) {
            servicioALanzado = true
            viewModel.fetchServicioB(data.campo)
        }
    }
    else -> {}
}

when (estadoB) {
    is ResourceUiState.Success -> {
        val resultado = (estadoB as ResourceUiState.Success).data
        if (resultado.tieneAlgo && !showDialog) {
            showDialog = true
        } else if (!resultado.tieneAlgo && !servicioBLanzado) {
            servicioBLanzado = true
            viewModel.fetchServicioC(...)
        }
    }
    else -> {}
}
```

El gate booleano evita que el `when` relance el servicio en cada recomposición. Cada `when` tiene una sola responsabilidad: observar su estado y decidir el siguiente paso. No importa cuántos servicios encadenados haya — siempre un gate, nunca un `LaunchedEffect` extra.
