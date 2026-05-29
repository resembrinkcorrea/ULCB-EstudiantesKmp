# Paleta de Colores Institucional — ILCB

> Fuente única de verdad: `core/theme/IlcbColor.kt`
> Todos los colores del proyecto se extraen de ese archivo. No usar valores hex hardcodeados en pantallas.

---

## Identidad de marca

Le Cordon Bleu Perú usa una paleta basada en **azul institucional profundo** combinado con **dorado** como acento de prestigio.

| Rol | Token | Color | Hex |
|-----|-------|-------|-----|
| Primary (light) | `IlcbBlueDark` | Azul marino oscuro | `#182957` |
| Header / TopBar | `IlcbBlueDeep` | Azul profundo | `#00205C` |
| Secondary (light) | `IlcbBlueMid` | Azul medio | `#2A4A8E` |
| Secondary variant | `IlcbBlueAccent` | Azul índigo | `#3F51B5` |
| Acento de marca | `IlcbGold` | Dorado institucional | `#9B865C` |
| Dorado claro | `IlcbGoldLight` | Dorado gradientes | `#C5A059` |

---

## Modo oscuro (Dark Mode)

| Rol | Token | Color | Hex |
|-----|-------|-------|-----|
| Primary (dark) | `IlcbBlueDarkDm` | Azul índigo oscuro | `#1A237E` |
| Secondary (dark) | `IlcbBlueMidDm` | Azul grisáceo claro | `#7D9AD1` |

---

## Superficies y fondos

| Rol | Token | Color | Hex |
|-----|-------|-------|-----|
| Fondo general | `IlcbBackground` | Blanco | `#FFFFFF` |
| Card / superficie | `IlcbSurface` | Gris muy claro | `#F1F1F1` |
| Fondo dialogs | `IlcbDialogBg` | Gris casi blanco | `#F9F9F9` |

---

## Texto

| Rol | Token | Color | Hex |
|-----|-------|-------|-----|
| Texto principal | `IlcbOnSurface` | Negro | `#000000` |
| Texto sobre marca | `IlcbOnBrand` | Blanco | `#FFFFFF` |

---

## Paleta de íconos (ColorIcons)

Usada en tarjetas de menú, badges y categorizaciones visuales.

| Token | Color | Hex |
|-------|-------|-----|
| `IlcbVioleta` | Violeta intenso | `#7719AA` |
| `IlcbAzulMedio` | Azul índigo | `#5D61C8` |
| `IlcbNaranja` | Naranja brillante | `#F08705` |
| `IlcbAzulFuerte` | Azul eléctrico | `#0063DD` |
| `IlcbAzulMarca` | Azul acento claro | `#3894F9` |

---

## Colores de estado / semánticos

| Rol | Token | Color | Hex |
|-----|-------|-------|-----|
| Error | `IlcbError` | Rojo alerta | `#D82625` |
| Error oscuro | `IlcbErrorDark` | Rojo oscuro | `#C62828` |
| Advertencia | `IlcbWarning` | Ámbar | `#FFA000` |
| Verde oscuro | `IlcbGreenDark` | Verde botella | `#1B5E20` |
| Verde medio | `IlcbGreenMid` | Verde | `#2E7D32` |
| Azul dialog | `IlcbBlueMD` | Azul acento dialogs | `#0D47A1` |

---

## Colores de tipo de clase

Usados en el calendario y listas de clases.

| Tipo | Hex |
|------|-----|
| Virtual | `#00BCD4` |
| Sincrónica | `#FFA000` |
| Presencial | `#000000` |
| Teórico | `#8BC34A` |
| Práctico | `#3F51B5` |

---

## Paleta de fondo de cards (Clase 01–10)

Fondos suaves diferenciados por posición en la lista:

| Card | Hex |
|------|-----|
| Clase 01 | `#E0F2F1` |
| Clase 02 | `#E3F2FD` |
| Clase 03 | `#D7CCC8` |
| Clase 04 | `#E8F5E9` |
| Clase 05 | `#EDE7F6` |
| Clase 06 | `#ECEFF1` |
| Clase 07 | `#FCE4EC` |
| Clase 08 | `#F3E5F5` |
| Clase 09 | `#FFF3E0` |
| Clase 10 | `#EDE7F6` |

---

> Para agregar un nuevo color: definirlo primero en `IlcbColor.kt` y luego referenciarlo desde `IlcbColorScheme.kt` o `AppTheme.kt`. Nunca usar hex directamente en las pantallas.
