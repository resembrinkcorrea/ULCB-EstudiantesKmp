# Flow: Cierre de módulo

Antes de hacer commit de cualquier módulo nuevo, ejecutar este flow.

## Pasos

### 1. Registrar endpoints en `domains/api.md`

Por cada endpoint nuevo agregar:

```
### NombreModulo
| Endpoint | Método | Request | Response |
|---|---|---|---|
| `nombreEndpoint` | POST | `NombreRequest` | `NombreResponse` |

**Request:**
campo1: Tipo
campo2: Tipo

**Rutas Navigator:**
- `/ruta` → `NombreScreen`
```

### 2. Actualizar estado en `context/PROJECT_CONTEXT.md`

Preguntar al usuario el % de avance del módulo trabajado y registrarlo:
```
| NombreModulo | X% | Notas de lo que falta o fue validado |
```

### 3. Recién entonces → commit y push

El commit incluye todos los archivos de `git status` sin discriminar.
El mensaje va en español con firma `Authored-By: Reloader - Resembrink Correa`.
