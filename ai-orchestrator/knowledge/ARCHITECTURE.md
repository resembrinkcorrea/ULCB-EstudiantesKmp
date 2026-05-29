# Architecture

## Patrón general
Clean Architecture + MVVM. No crear repos por módulo.

## Capas (todo en composeApp/src/commonMain)
- **Data**: RepoImpl, DTOs, Ktor
- **Domain**: Modelos de negocio (Entities) + interfaces de Repositorios
- **Presentación**: ViewModels que heredan de `androidx.lifecycle.ViewModel`
- **UI**: Pantallas y componentes Compose Multiplatform

## Organización de la capa UI

### `components/`
Componente puro e independiente. No conoce ningún screen ni dominio de negocio.
Sus parámetros son tipos genéricos (String, Boolean, LocalDate, lambdas).
Cualquier screen puede usarlo sin contexto previo.
Ejemplos: `CalendarioMensualPanel`, `AppComponents`.

### `customcell/`
Celda completa de una lista. Representa un ítem con su estructura visual completa.
Recibe un objeto del dominio directamente (DNI, foto, estado, etc.).
Tiene forma propia pero está atado a un modelo de datos específico.
Ejemplos: `ClaseCard`, `AsistenciaCard`, `EstudianteItem`.

**Regla estricta:** Un customcell dibuja un único ítem. Nunca itera sobre una lista internamente. El bucle es responsabilidad exclusiva del Screen/presentador. Si un customcell itera una lista puede generar hasta 1100 recomposiciones innecesarias.

**Jerarquía interna del customcell — Items:**
Dentro de un customcell pueden existir sub-elementos visuales propios llamados **items**.
Un item es un `@Composable` privado que pertenece exclusivamente al customcell que lo contiene.
No se expone al screen ni a otros composables.

```
Screen
 └── CustomCell (card completo — archivo en customcell/)
      └── Item (sub-elemento visual privado — @Composable private fun dentro del mismo archivo)
           Ejemplos: una imagen de estado, un badge, una fila label-valor, un indicador
```

Reglas del item:
- Siempre `private` dentro del mismo archivo del customcell
- Recibe solo los datos que necesita para dibujarse (no el objeto completo del dominio)
- No tiene lógica de negocio ni callbacks al ViewModel
- Si un item se repite en más de un customcell → proponer ascenso a `components/`

### `ui/helpers/`
Fragmento visual de apoyo para un screen específico. No tiene lógica de negocio.
Solo ayuda a dibujar algo que ese screen necesita — no es una celda completa.
No aplica a otros screens por su naturaleza visual particular.
Ejemplos: formateador de fechas para Horario, textura de fondo del Login.

### Regla de ascenso
Un helper o customcell **asciende a `components/`** cuando se detecta que puede vivir
solo, independiente de cualquier screen o dominio. Proponer el ascenso antes de moverlo.

## Sistema de colores (fuente única de verdad)

Todo color nuevo sigue este flujo obligatorio — sin excepciones:

1. **`core/theme/UlcbColor.kt`** → definir el valor hex con nombre semántico ULCB:
   `val UlcbNaranjaOscuro = Color(0xFFEF6C00)`
2. **`AppTheme.kt → DarkModeColors`** → agregar el campo con nombre en español que describe el color (no su uso):
   `val colorNaranjaOscuro: Color`
3. **`AppTheme.kt → getColorsTheme()`** → asignar el valor (con lógica dark/light si aplica):
   `colorNaranjaOscuro = UlcbNaranjaOscuro`
4. **`composeApp/src/androidMain/res/values/preview_colors.xml`** → agregar el mismo color como referencia visual de Android Studio con sufijo `_ts`:
   `<color name="ulcb_naranja_oscuro_ts">#EF6C00</color>`

**Regla del preview referencial:**
- `preview_colors.xml` es solo apoyo visual del IDE. No es fuente de verdad del tema.
- Todo color nuevo agregado en `UlcbColor.kt` debe reflejarse también en `preview_colors.xml`.
- Los nombres en `preview_colors.xml` siempre usan sufijo `_ts` para evitar cruces con recursos reales.

### Colores semánticos de estado

Cuando un módulo necesita representar estados visuales de negocio (`respondida`, `no respondida`, `deshabilitada`, `seleccionada`, `warning`, etc.), no resolver esos colores directamente dentro del Screen o customcell.

Flujo obligatorio:
1. Definir primero el color base en `UlcbColor.kt` si todavía no existe.
2. Exponer el estado visual en `AppTheme.kt -> DarkModeColors` con nombre semántico:
   `colorCardRespondida`, `colorCardNoRespondida`, `colorOptionNoSeleccionado`
3. Resolver el valor dark/light en `getColorsTheme()`.
4. Consumir ese color semántico desde UI. No repetir `if (isDarkMode)` por módulo si el estado se reutiliza.

**Objetivo:** la arquitectura base maneja el tema; los estados de caso de uso viven como semántica centralizada para evitar caos cuando el proyecto crezca.

**Nunca:**
- Hardcodear `Color(0xFF...)` directamente en un composable **ni en `getColorsTheme()`**
- Nombrar colores por su uso (`colorLogout`, `colorAprobado`, `colorPendiente`, `colorSincronica`)
- Duplicar colores que ya existen en `UlcbColor.kt`
- Agregar campos en `DarkModeColors` sin su entrada en `UlcbColor.kt`
- Nombrar campos con comportamiento dark/light en el nombre (`colorBlueDarkToLightGray`) → usar nombre del color en español (`colorAzulOscuro`)

**Por qué:** En el proyecto Universidad había duplicidad de colores y cuando se quiso implementar dark/light mode los colores se cruzaban. Esta arquitectura garantiza una sola fuente de verdad.

## Principios obligatorios
- Single Responsibility + Dependency Inversion: VMs dependen de interfaces, no implementaciones
- Clean Architecture: dependencias siempre hacia adentro — Domain no conoce Ktor ni SQLDelight
- DRY: centralizar validaciones y componentes visuales
- DI según patrón del módulo — ver `knowledge/CONVENTIONS.md → ### ViewModels`

## Stack
- DI: Koin
- Network: Ktor
- Storage: SettingsStorage (expect/actual)
- Resources: composeResources

## Patrones de ViewModel
→ ver `knowledge/CONVENTIONS.md → ### ViewModels`
