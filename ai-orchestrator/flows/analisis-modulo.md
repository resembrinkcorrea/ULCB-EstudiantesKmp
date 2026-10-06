# Flow — Análisis de Módulo

## Trigger

```
reloader analisis <nombre-modulo>
```

Ejemplo: `reloader analisis pagos`, `reloader analisis matricula`, `reloader analisis tramite-documentario`

**No proponer soluciones en el hilo principal.** Lanzar un agente en background
y avisar cuando termine.

---

## Paso 1 — Confirmar alcance

Antes de lanzar el agente, preguntar al usuario:

- ¿Qué módulo/funcionalidad se analiza?
- ¿Desde qué plataforma? (web / mobile / ambas)
- ¿Hay algún problema específico a resolver o es exploración general?

Con eso, continuar al paso 2.

---

## Paso 2 — Lanzar Workflow en background

Lanzar `Workflow` (no `Agent`). Usar Workflow permite ver el progreso en vivo con `/workflows` y orquestar fases en paralelo.

El workflow debe seguir este orden de lectura antes de concluir nada:

### 2.1 Identificar los SPs del módulo

- Buscar en `saa-rest` el endpoint o DAO del módulo
- Extraer el nombre del SP que usa
- Leer el SP completo línea a línea (no asumir nada por el nombre)

### 2.2 Leer las tablas involucradas

- Identificar las tablas que el SP consulta o modifica
- Verificar columnas reales (no asumir campos)
- Verificar flags, relaciones y casos especiales hardcodeados en el SP

### 2.3 Leer el frontend web (si aplica)

- Buscar en `intranetSAA` la función JS que consume el endpoint
- Entender qué hace con el response: ¿inyecta HTML? ¿construye UI propia?

### 2.4 Leer el frontend mobile (si aplica)

- Buscar en el proyecto KMP el screen o ViewModel del módulo
- Identificar qué datos consume y cómo construye la UI nativa

### 2.5 Mapear el flujo completo

```
Trigger (usuario) → endpoint REST → SP → tablas → response → UI (web/mobile)
```

Anotar dónde hay lógica en SP vs lógica en frontend vs lógica en app.

---

## Paso 3 — Producir el reporte

El agente devuelve un reporte con estas secciones:

1. **Flujo de datos** — diagrama de texto del flujo completo
2. **Tablas involucradas** — nombre, columnas clave, flags relevantes
3. **Lógica en SP** — qué decisiones toma el SP (hardcodes, condiciones)
4. **Diferencia web vs mobile** — qué puede hacer uno que el otro no
5. **Problema identificado** — (si el usuario lo indicó)
6. **Opciones de solución** — mínimo 2, con pros y contras reales basados en lo leído
7. **Recomendación** — cuál opción y por qué

---

## Paso 4 — Guardar reporte y abrir nueva sesión Claude

Cuando el Workflow termina:

1. Guardar el reporte en `ai-orchestrator/context/analisis-<modulo>-<fecha>.md`
2. Abrir una nueva ventana de Terminal con Claude Code apuntando al reporte:

```bash
osascript -e 'tell application "Terminal" to do script "cd /Users/resembrink/Developer/GitHub/ULCB-EstudiantesKmp && claude \"lee ai-orchestrator/context/analisis-<modulo>-<fecha>.md y preséntame el análisis\""'
```

La sesión nueva arranca con el reporte como primer mensaje — el usuario puede
explorar, preguntar y debatir soluciones ahí, sin cortar el hilo de trabajo principal.

3. Avisar en el hilo principal: "Análisis listo — abrí nueva ventana Claude con el reporte."

---

## Notas

- El Workflow **lee, no ejecuta** — ninguna operación en BD sin confirmación del usuario
- Si el SP no está en `/tmp/`, pedirle al usuario que lo comparta o ejecutarlo con `USE saa_lcbp`
- El reporte queda en `ai-orchestrator/context/` como referencia permanente
- Usar `/workflows` para ver el progreso en tiempo real mientras corre
