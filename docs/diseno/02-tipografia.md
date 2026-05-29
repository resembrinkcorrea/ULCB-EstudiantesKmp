# Tipografía y Escala de Texto

## Fuentes utilizadas

| Fuente | Uso | Plataforma |
|--------|-----|-----------|
| **Rajdhani Bold** | Títulos y headings | Android + iOS |
| **MyriadPro Regular** | Etiquetas, captions institucionales | Android + iOS |
| **Roboto** (sistema) | Cuerpo de texto, body | Android |
| **SF Pro** (sistema) | Cuerpo de texto, body | iOS |

Las fuentes de marca (`Rajdhani Bold`, `MyriadPro`) se cargan desde `composeResources/font/` y están disponibles en ambas plataformas.

---

## Implementación expect / actual

La tipografía sigue el patrón `expect/actual` de KMP:

```
commonMain → expect fun ilcbTypography(): Typography
androidMain → actual fun ilcbTypography() = Typography(...)   // Roboto + Rajdhani
iosMain     → actual fun ilcbTypography() = Typography(...)   // SF Pro + Rajdhani
```

Esto garantiza que cada plataforma usa su fuente de sistema nativa para el cuerpo del texto, respetando las guías de Apple y Google, mientras que los headings de marca son consistentes.

---

## Escala tipográfica MD3

| Rol MD3 | Fuente asignada | Uso típico |
|---------|----------------|-----------|
| `displayLarge` | Roboto / SF Pro | Pantallas de bienvenida |
| `displayMedium` | Roboto / SF Pro | Pantallas hero |
| `headlineLarge` | **Rajdhani Bold** | Títulos principales de sección |
| `headlineMedium` | **Rajdhani Bold** | Subtítulos de tarjeta |
| `headlineSmall` | **Rajdhani Bold** | Encabezados de lista |
| `titleLarge` | Roboto / SF Pro | TopBar, nombres de pantalla |
| `titleMedium` | Roboto / SF Pro | Subtítulos en cards |
| `bodyLarge` | Roboto / SF Pro | Texto de lectura principal |
| `bodyMedium` | Roboto / SF Pro | Descripciones secundarias |
| `labelLarge` | **MyriadPro** | Botones, etiquetas de campo |
| `labelMedium` | **MyriadPro** | Badges, chips |
| `labelSmall` | **MyriadPro** | Captions, notas al pie |

---

## Reglas de uso

- Los **headings de marca** (Rajdhani) se usan en nombres de módulos, títulos de pantalla y encabezados de tarjetas importantes.
- El **cuerpo** siempre usa la fuente del sistema para máxima legibilidad y accesibilidad nativa.
- **MyriadPro** se reserva para elementos institucionales: etiquetas de formulario, captions con identidad de marca.
- No usar `fontFamily` directamente en las pantallas — acceder siempre a través de `MaterialTheme.typography`.

---

## Acceso en código

```kotlin
// Correcto — usa el sistema de diseño
Text(
    text = "Bienvenido",
    style = MaterialTheme.typography.headlineMedium
)

// También válido para compatibilidad con AppTheme
Text(
    text = "Etiqueta",
    fontFamily = leCordonBleuFont(),  // MyriadPro
    fontWeight = FontWeight.Bold
)
```
