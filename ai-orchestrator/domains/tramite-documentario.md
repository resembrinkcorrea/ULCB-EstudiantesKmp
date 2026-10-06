# Domain — Trámite Documentario

## Contexto

El módulo de trámite documentario permite al estudiante solicitar documentos
(certificados, constancias, diplomas, etc.) desde la app mobile (KMP) y desde
la intranet web (intranetSAA).

Ambas plataformas consumen la misma base de datos (`saa_lcbp`) y el mismo
backend REST (`saa-rest`).

---

## Cómo funciona en Web

### Flujo

```
Estudiante selecciona un trámite
        ↓
TTR combo → pro_saac_FiltrosCmb(1, 'TTR', ...)
        ↓
Devuelve: tipo_tramite_nombre, tg_valor, flag_pago, multiple, requisitos
        ↓
TRE combo → pro_saac_FiltrosCmb(1, 'TRE', ...)
        ↓
Devuelve: filas con HTML embebido (checkboxes, selects, file inputs, text inputs)
        ↓
Web inyecta ese HTML directo en la tabla — sin lógica propia
```

### Clave

La web **no tiene lógica de formularios**. El SP `pro_saac_FiltrosCmb`
genera el HTML completo en runtime leyendo los flags de `sasc.tramite`.

El campo `requisitos` del TTR contiene marcadores textuales:

| Marcador en `requisitos` | Qué significa |
|---|---|
| `*ASIGNATURAS<BR>` | El TRE generará checkboxes por curso |
| `*CICLOS<BR>` | El TRE generará checkboxes por ciclo |
| `*EMPRESA<BR>` | El TRE generará inputs de texto (RUC, empresa, área, dirigido a) |
| `*PERIODO<BR>` | El TRE generará un select de periodos |
| `*CARRERA<BR>` | El TRE generará un select de carreras |
| vacío / `*` | Formulario simple (solo motivo y tipo de entrega) |

### Bloques HTML que genera el SP (pro_saac_FiltrosCmb — TRE)

| Bloque | Condición en BD | HTML generado |
|---|---|---|
| DOCUMENTOS REQUISITOS | `tramite_req_doc` tiene registros | `<input type="file" accept=".pdf">` |
| PERIODO | `id_tramite = 115` hardcodeado en SP | `<select>` con periodos académicos |
| ASIGNATURAS | `flag_multiple = 1` en `sasc.tramite` | `<input type="checkbox">` por curso activo |
| CICLOS | `flag_multiple_ciclo = 1` en `sasc.tramite` | `<input type="checkbox">` por ciclo matriculado |
| EMPRESA | `flag_empresa = 1` en `sasc.tramite` | 4 `<input type="text">` (RUC, razón social, área, dirigido a) |

El SP también genera los combos de Tipo de Entrega y Modalidad dentro del mismo response.

---

## Cadena de tablas en BD

```
sasc.tramite
  ↓ id_tramite
sasc.tramite_req          → nombre del requisito (ej. "CAMBIO DE DOCENTE")
  ↓ id_tramite_req
sasc.tramite_req_doc      → documentos a subir (ej. "FOTOGRAFÍAS TAMAÑO PASAPORTE")
```

Ninguna tabla almacena HTML. El HTML lo genera el SP en runtime.

### Flags en sasc.tramite

| Columna | Valores | Rol |
|---|---|---|
| `flag_multiple` | NULL / 0 / 1 | 1 = tramite con selección de asignaturas |
| `flag_multiple_ciclo` | NULL / 0 / 1 | 1 = tramite con selección de ciclos |
| `flag_empresa` | NULL / 0 / 1 | 1 = tramite con datos de empresa |

**Problema:** los valores son inconsistentes (NULL vs 0) y los casos especiales
(`id_tramite = 115`, `id_tramite = 56`) están hardcodeados en el SP, no en la tabla.
`id_tipo_tramite` agrupa por categoría de negocio (Certificados, Exámenes, etc.),
no por tipo de formulario — no sirve para identificar el UI.

---

## Por qué mobile no puede usar el mismo enfoque

La web inyecta HTML arbitrario en una tabla HTML del DOM.
En Kotlin/Compose y Swift/SwiftUI no existe ese mecanismo:
no se puede renderizar un `<input type="checkbox">` como componente nativo.

La app necesita saber **qué pantalla Compose/SwiftUI renderizar** antes de
recibir los datos — el tipo de UI debe venir declarado en el response.

---

## Modelos de UI identificados en la app (A–L)

| Tipo | UI mobile | Tramites ULCB | Tramites ILCB |
|---|---|---|---|
| **A** | Checkboxes de asignaturas (cursos activos) | 98 | 125,126,133,134,135,136,138,139 |
| **B** | Inputs de texto empresa (RUC, razón social, área, dirigido a) | 67,107,1 | 67,107,1 |
| **C** | Checkboxes de ciclos matriculados | 69,73,74,109,146,148 | 2,3,9,13,19,20,28,64 |
| **D** | Select de periodo o carrera | 115,56 | 115,56 |
| **E** | Checkboxes de asignaturas desaprobadas (examen aplazado) | 87 | 46 |
| **F** | Formulario simple (motivo + tipo entrega + pago) | 66,68,70,71,72,77,78,79,80,85,106,110,111,112,113,114,116,117,118,120,121,122,160 | 7,8,10,11,41,44,47,52,53,57,60,62,63 |
| **G** | Formulario simple sin pago (cuenta corriente / devoluciones) | 124,129 | 124,129 |
| **H** | Subir archivo PDF | 76,81,101,119 | 54,128 |
| **L** | Trámite de título / bachiller (padrón de grado) | 103 | 29,40,51 |

---

## Solución propuesta — columna `tipo_det_app`

Agregar una columna en `sasc.tramite` que declare explícitamente el tipo de UI:

```sql
ALTER TABLE sasc.tramite ADD tipo_det_app VARCHAR(10) NULL;
```

### Beneficios

- La app lee un solo campo del response — sin parsear flags ni HTML
- Cuando BD agrega un trámite nuevo, solo asigna `tipo_det_app` y la app lo renderiza sin cambios de código
- Elimina el enum hardcodeado con IDs en KMP
- El campo es gestionable por el equipo de sistemas desde la BD

### Cómo llega a la app

El SP `pro_saac_FiltrosCmb` con combo `TTR` devuelve los datos del trámite
seleccionado. Se agrega `tipo_det_app` a ese SELECT:

```sql
-- Dentro del bloque TTR de pro_saac_FiltrosCmb
SELECT ...
    , trt.tipo_det_app   -- ← nuevo campo
FROM sasc.tramite trt
...
WHERE trt.id_tramite = @VARIABLE9
```

La app KMP lo consume desde el TTR response y decide qué pantalla renderizar:

```kotlin
val tipoDetApp = ttrData["tipo_det_app"]?.jsonPrimitive?.contentOrNull
val tipoFormulario = TipoFormularioTramite.fromCode(tipoDetApp)
```

```kotlin
enum class TipoFormularioTramite {
    A, B, C, D, E, F, G, H, L, DESCONOCIDO;

    companion object {
        fun fromCode(code: String?): TipoFormularioTramite =
            entries.find { it.name == code } ?: DESCONOCIDO
    }
}
```

### Pendiente de ejecución

- [ ] Confirmar valores de `tipo_det_app` para tramites sin asignar (NULL actuales)
- [ ] `ALTER TABLE sasc.tramite ADD tipo_det_app VARCHAR(10) NULL`
- [ ] `UPDATE` por cada grupo A–L con los IDs confirmados
- [ ] Modificar SP `pro_saac_FiltrosCmb` bloque TTR para devolver `tipo_det_app`
- [ ] Modificar SP `pro_saac_FiltrosCmb` bloque TRE (validar si necesita cambios)
- [ ] Actualizar `saa-rest` endpoint `listarfiltros` para exponer el nuevo campo
- [ ] Reemplazar enum hardcodeado en KMP por `fromCode()`

---

## SPs involucrados

| SP | Uso |
|---|---|
| `pro_saac_FiltrosCmb` | Lista tramites (CIM), detalle tramite (TTR), requisitos del estudiante (TRE) |
| `pro_sasc_tramites_estudiantes` | Lista tramites del estudiante, registra, actualiza estado |

## Endpoints REST involucrados

| Endpoint | Método | Uso |
|---|---|---|
| `/intranetSAA/listarfiltros` | POST | CIM (filtros), TTR (detalle tramite), TRE (requisitos) |
| `/intranetSAA/obtenerTramitesEstudiante` | POST | Lista trámites solicitados por el estudiante |
