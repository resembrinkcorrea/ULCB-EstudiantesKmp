# Domain: KMP

## Ktor — DTOs y Requests

- Nunca usar `@SerialName` en ningún campo
- Los campos del data class se declaran exactamente igual a como los trae/espera el servidor (respetando snake_case, mayúsculas y minúsculas)
- Solo se anota `@Serializable` en la clase, nada más
- Razones:
  1. CIO/Darwin en iOS puede malinterpretar la data con `@SerialName`, generando respuestas corruptas
  2. iOS es más estricto que Android — cualquier inconsistencia entre el nombre del campo y la key real rompe el parser
  3. Los servicios REST están diseñados limpios — no hay necesidad de mapear
- Ejemplo: si el servicio devuelve `data_menu`, el campo es `val data_menu`, nunca `@SerialName("data_menu") val dataMenu`

**Correcto:**
```kotlin
@Serializable
data class CarreraRequest(val id_estud: Int)

@Serializable
data class ResponseCarrera(
    val flag_val: Int,
    val carrera: List<Carrera> = emptyList()
)
```

**Incorrecto:**
```kotlin
@Serializable
data class ResponseCarrera(
    @SerialName("flag_val") val flagVal: Int,
    @SerialName("carrera") val carrera: List<Carrera> = emptyList()
)
```

---

## Porte de validaciones — Nativo a KMP

Estas reglas aplican a **cualquier customcell o screen** que porte lógica desde el nativo Android.

### Parsing seguro de campos numéricos
El nativo usa `.toFloat()`, `.toInt()` sin guard — si el servidor manda un valor inesperado (`"-"`, `""`, `"NULL"`) crashea.
En KMP siempre usar la variante segura:
```kotlin
val valor = item.campo_numerico.toFloatOrNull()   // null si no es número
val peso  = item.campo_peso.toFloatOrNull()?.roundToInt() ?: 0
val base  = item.campo_base.toFloatOrNull() ?: 0f
```

### Strings del servidor — implementación en KMP
→ Principio: ver `knowledge/CONVENTIONS.md → Valores del servidor`

Implementación concreta: usar `equals(..., ignoreCase = true)` y declarar **todos** los casos explícitamente:
```kotlin
val colorEstado = when {
    item.estado.equals("1",    ignoreCase = true) -> colors.colorVerdeMedio
    item.estado.equals("0",    ignoreCase = true) -> colors.colorRojo
    item.estado.equals("NULL", ignoreCase = true) -> colors.textColor.copy(alpha = 0.4f)
    else                                           -> colors.textColor.copy(alpha = 0.4f)
}
```

### Campos con valor centinela del servidor (ej. "-")
→ Principio: ver `knowledge/CONVENTIONS.md → Valores del servidor`

Validar explícitamente **solo en el campo que lo recibe**, no en la lambda general:
```kotlin
val colorResultado = if (item.campo == "-") colors.textColor else colorPorValor(valorParsed)
```

### Lógica de color reutilizable — val lambda
Si la misma lógica de color se aplica a múltiples campos, NO usar función local ni repetir `if/else`. Usar un `val` lambda:
```kotlin
val colorPorValor: (Float?) -> Color = { valor ->
    valor?.let { if (base < it) colors.colorAzulMedio else colors.colorRojo } ?: colors.textColor
}
val colorA = colorPorValor(campoA)
val colorB = colorPorValor(campoB)
val colorC = colorPorValor(campoC)
```

### Campos con valor especial del servidor (ej. "-")
Si el servidor manda un texto centinela (`"-"`, `"N/A"`, etc.) para indicar ausencia de dato, validarlo explícitamente **solo en el campo que lo recibe**:
```kotlin
val colorResultado = if (item.campo == "-") colors.textColor else colorPorValor(valorParsed)
```

### Orden de validaciones en customcell
Respetar el mismo orden que el nativo para facilitar la revisión línea a línea:
1. Campos base numéricos (los que se usan como referencia para comparar)
2. Campos a comparar (parseados con `toFloatOrNull()`)
3. Lambda de color (si hay lógica reutilizable)
4. Colores resultantes de cada campo
5. Colores de estado/condición (flags de texto del servidor)
6. Pesos o valores de display secundarios

### Toast en estados vacíos de listas
→ ver `knowledge/CONVENTIONS.md → Patrón de Screen → Prohibido side effect dentro del when`

---

## Compose — Patrón de Screen
→ ver `knowledge/CONVENTIONS.md → Patrón de Screen`
