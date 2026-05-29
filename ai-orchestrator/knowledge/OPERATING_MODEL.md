# Operating Model

## Fuente de verdad

- La única fuente de verdad de la arquitectura de automatización es `ai-orchestrator/`
- El código fuente del proyecto sigue siendo la fuente de verdad funcional
- Todo debe persistirse en: `knowledge/` (reglas estables), `domains/` (por tecnología), `flows/` (procesos), `skills/` (automatización)
- Prohibido usar memoria interna de Claude como fuente de verdad
- Prohibido duplicar información
- Prohibido almacenar reglas fuera del repositorio
- Si hay algo que recordar → escribirlo en el `.md` correspondiente dentro del proyecto

## Ejecución

- Claude no debe ejecutar: gradle, pod install, docker build, npm install, builds pesados
- Solo debe preparar comandos, validaciones y pasos — el usuario ejecuta manualmente

## Reglas de Interacción

1. Proponer estructura antes de crear archivos o carpetas — no crear sin aprobación
2. Respetar el estilo del usuario (ej. validaciones con flag_val, naming, etc.)
3. Explicaciones directas al punto — sin relleno
4. Respetar el patrón existente de cada módulo — no cambiarlo sin que se pida
5. No modificar código funcional sin pedido explícito. Al portar código entre proyectos (ej. Docentes → Estudiantes), preservar línea por línea — solo adaptar packages, imports y valores app-específicos

## Análisis de screen o lógica — estructura obligatoria

Cuando el usuario pide explicar la lógica de un screen, componente o validación, la respuesta debe cubrir siempre estos tres puntos en este orden:

1. **Qué hace** — idea lógica del comportamiento, sin detallar código ni mappers
2. **Qué validaciones tiene** — condiciones de negocio (comparaciones, guards, estados derivados)
3. **Qué datos manda el servidor** — campos exactos del DTO, valores posibles incluyendo centinelas (`"-"`, `"NULL"`, `"0.00"`, etc.)

**Prohibido** responder con detalle de código, nombres de funciones, clases o mappers a menos que el usuario lo pida explícitamente.
