# Sistema de Temas — Dark / Light Mode

## Arquitectura del tema

El tema se aplica desde `App.kt` → `AppTheme {}` → `IlcbTheme {}`.

```
App.kt
 └── AppTheme { }              (AppTheme.kt — delegador)
      └── IlcbTheme { }         (core/theme/IlcbTheme.kt — implementación real)
           ├── MaterialTheme (M3)   ← pantallas nuevas
           └── MaterialTheme (M2)   ← componentes legacy (OutlinedTextField, Scaffold)
                └── content()
```

---

## Coexistencia Material 2 + Material 3

El proyecto usa **M3 como sistema principal** pero mantiene M2 porque algunos componentes de Compose (especialmente `OutlinedTextField`, `TopAppBar` heredado, `Scaffold` antiguo) leen el tema de M2.

La solución es anidar ambos `MaterialTheme`:

```kotlin
// IlcbTheme.kt
MaterialTheme(
    colorScheme = colorScheme,       // M3: pantallas nuevas
    typography   = ilcbTypography(),
    shapes       = IlcbShapes
) {
    androidx.compose.material.MaterialTheme(
        colors = lightColors(primary = m2Primary)  // M2: componentes legacy
    ) {
        content()
    }
}
```

El `primary` de M2 cambia según el modo:
- **Light mode**: `IlcbBlueDark` (`#182957`) — azul marino de marca.
- **Dark mode**: `Color(0xFF90CAF9)` — azul claro para que los bordes y labels sean visibles sobre fondo oscuro.

---

## ColorScheme — Light

| Slot M3 | Token | Hex | Uso |
|---------|-------|-----|-----|
| `primary` | `IlcbBlueDark` | `#182957` | Botones principales, TopBar |
| `onPrimary` | `IlcbOnBrand` | `#FFFFFF` | Texto sobre primary |
| `primaryContainer` | `IlcbBlueDeep` | `#00205C` | Contenedores de énfasis |
| `secondary` | `IlcbBlueMid` | `#2A4A8E` | Elementos secundarios |
| `tertiary` | `IlcbGold` | `#9B865C` | Acentos dorados |
| `error` | `IlcbError` | `#D82625` | Validaciones, alertas |
| `background` | `IlcbBackground` | `#FFFFFF` | Fondo de pantalla |
| `surface` | `IlcbSurface` | `#F1F1F1` | Cards, modales |
| `onSurface` | `IlcbOnSurface` | `#000000` | Texto general |

---

## ColorScheme — Dark

| Slot M3 | Token | Hex | Uso |
|---------|-------|-----|-----|
| `primary` | `IlcbBlueDarkDm` | `#1A237E` | Botones en oscuro |
| `secondary` | `IlcbBlueMidDm` | `#7D9AD1` | Elementos secundarios |
| `background` | — | `#1E1C1C` | Fondo oscuro |
| `surface` | — | `#090808` | Cards en oscuro |
| `onBackground` | — | `#FFFFFF` | Texto sobre fondo oscuro |
| `onSurface` | — | `#FFFFFF` | Texto sobre cards oscuros |

---

## Detección del modo activo

```kotlin
val isDarkMode = isSystemInDarkTheme()
```

El tema sigue la configuración del sistema operativo del usuario automáticamente. No hay switch manual en la app (todavía).

---

## Acceso a colores en pantallas

### Pantallas nuevas (M3)
```kotlin
val colors = MaterialTheme.colorScheme
Text(color = colors.onPrimary)
```

### Pantallas existentes (compatibilidad)
```kotlin
val colors = getColorsTheme()  // DarkModeColors — AppTheme.kt
Text(color = colors.textColor)
Card(backgroundColor = colors.colorExpenseItem)
```

---

## Shapes (bordes redondeados)

| Slot | Radio |
|------|-------|
| `extraSmall` | 4 dp |
| `small` | 8 dp |
| `medium` | 16 dp |
| `large` | 24 dp |
| `extraLarge` | 32 dp |

```kotlin
Card(shape = MaterialTheme.shapes.medium)  // 16 dp
```
