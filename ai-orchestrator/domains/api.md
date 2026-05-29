# Domain: API — Endpoints registrados

## Base
- `BASE_ROOT_INTRANET` + `URL_BASE_INTRANET` = `{host}/saa-rest/webresources/intranetSAA/`

---

## Módulos implementados en KMP

### MarcarAsistencia
| Endpoint | Método | Request | Response |
|---|---|---|---|
| `ValidacionProyeccion` | POST | `ValidaProyeccionRequest` | `ResponseProyeccionValidacion` |
| `estadoMarcacionEstudiante` | POST | `EstadoMarcacionRequest` | `ResponseEstadoMarcacion` |
| `marcarAsistenciaEstudiante` | POST | `MarcarRequest` | `ResponseMarcarAsistencia` |
| `guardarLogNav` | POST | `NavigationLogRequest` | `ResponseNavigationLog` |

**Request ValidacionProyeccion:**
```
idEstud: Int
```

**Request estadoMarcacionEstudiante:**
```
id_uneg: Int
id_estud_pe: Int   (obtenido de ValidacionProyeccion)
id_serv: Int       (obtenido de ValidacionProyeccion)
```

**Request marcarAsistenciaEstudiante:**
```
id_unidad_negocio: Int
id_usuario: Int
id_hor_asis: Int   (obtenido de estadoMarcacionEstudiante → id_hor_asis)
id_estud_pe: Int
id_sistema: Int
```

**Storage keys guardadas en HomeScreen (desde ValidacionProyeccion):**
```
idEstudPe, idServ, idOacadArranque, idPerAcad, idPestDet
```

**Rutas Navigator:**
- `/marcarAsistencia` → `MarcarAsistenciaScreen`

**HomeScreen trigger:** `"MARC. ASISTENCIA"`

---

### HistorialAcademico
| Endpoint | Método | Request | Response |
|---|---|---|---|
| `HistorialAcademico` | POST | `HistorialAcademicoAlumnoRequest` | `ResponseHistorialAcademicoAlumno` |
| `HistorialAcademicoDetalle` | POST | `HistorialAcademicoAlumnoDetalleRequest` | `ResponseHistorialAcademicoAlumnoDetalle` |

**Request lista:**
```
id_estud_serv: String  ("0" fijo)
id_estud: String       (idEstud del usuario)
ped_estado_reg: String ("1" fijo)
```

**Request detalle:**
```
id_estud_pe: Int
id_peracad: Int
```

**Rutas Navigator:**
- `/historialAcademico` → `HistorialAcademicoScreen`
- `/historialAcademicoDetalle/{idEstudPe}/{idPeracad}` → `HistorialAcademicoDetalleScreen`

**HomeScreen trigger:** `"HISTORIAL ACADEMICO"`

---

### Encuestas

#### Encuesta Docente
| Endpoint | Método | Request | Response |
|---|---|---|---|
| `estudianteEncuesta` | POST | `AsignaturaEncuestaRequest` | `ResponseAsignaturaEncuesta` |
| `listarEncuesta` | POST | `ListarEncuestaRequest` | `ResponseListarEncuesta` |

**Request estudianteEncuesta:**
```
id_peracad: Int
id_estud_pe: Int
id_serv: Int
id_oacad_arranque: Int
```

**Response estudianteEncuesta — campo clave:**
```
ListAsignaturaEncuesta[].flag_registrado  →  0=pendiente, 1=registrado
```

**Request listarEncuesta:**
```
id_uneg: Int      (fijo: 2)
id_pest_det: Int  (de storage: idPestDet, obtenido de ValidacionProyeccion)
id_peracad: Int
```

---

#### Encuesta Satisfacción
| Endpoint | Método | Request | Response |
|---|---|---|---|
| `encuestaSatisfaccionEstudiante` | POST | `EncuestaSatisfaccionEstadoRequest` | `ResponseEncuestaSatisfaccionEstado` (HomeScreen) |
| `encuestaSatisfaccionEstudiante` | POST | `EncuestaSatisfaccionRequest` | `ResponseEncuestaSatisfaccion` (Dialog/ViewModel) |
| `guardarEncuestaSatisfaccion` | POST | `EncuestaSatisfaccionGuardarRequest` | `ResponseGuardarEncuestaSatisfaccion` |

> Mismo endpoint, dos DTOs distintos: HomeScreen solo necesita `EstadoEncuesta`, el Dialog necesita además `ListPreguntasEncuesta`. Ambos usan `ignoreUnknownKeys = true`.

**Request (estado + preguntas):**
```
id_peracad: Int
id_estud_pe: Int
id_serv: Int
id_oacad_arranque: Int
```

**Response — campos clave:**
```
EstadoEncuesta[0].valida_encuesta  →  0=pendiente (mostrar dialog), 1=ya respondida
ListPreguntasEncuesta[].id_tipo_preg  →  4=UNICA, 5=CHECK, 6=TEXTO, 8=OPTIONLINEAL
```

**Request guardarEncuestaSatisfaccion:**
```
id_sistema: Int   (fijo: 24)
Encuesta: List<EncuestaItem>
  └─ id_exam_preg, id_alternativa, texto, id_peracad, id_serv,
     id_exam_serv, id_pest_det, id_procdet, id_estud_pe, id_usuario
```

**Flujo HomeScreen (cadena con gate booleano):**
```
fetchProyeccionValidacion(idEstud)
  → guarda storage: idEstudPe, idServ, idOacadArranque, idPerAcad, idPestDet
  → fetchEncuestaDocente(...)
      └─ ListAsignaturaEncuesta.isNotEmpty() && any{flag_registrado==0}
            → showEncuestaDocenteDialog
         else
            → fetchEncuestaSatisfaccion(...)
                └─ EstadoEncuesta[0].valida_encuesta == 0
                      → showEncuestaSatisfaccionDialog
```
