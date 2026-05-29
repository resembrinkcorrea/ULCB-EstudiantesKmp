# Plan de Desarrollo — Roadmap

## Estado actual del proyecto

> Última actualización: 2026-03-17
> Rama principal: `main`

---

## Módulos completados

| Módulo | Estado | Descripción |
|--------|--------|-------------|
| **Onboarding** | Completado | Pantalla de bienvenida con carrusel de imágenes institucionales |
| **Login** | Completado | Autenticación institucional (usuario + contraseña) y Microsoft SSO (MSAL) |
| **Home** | Completado | Dashboard principal: menú de módulos, estado de asistencia, aula demo |
| **Tema ILCB** | Completado | Sistema de colores MD3, tipografía, dark/light mode |
| **Arquitectura base** | Completado | Clean Architecture + MVVM, Koin, Ktor, repositorio centralizado |

---

## Módulos planificados

> El orden puede variar según prioridades del negocio.

| Módulo | Estado | Descripción |
|--------|--------|-------------|
| **Notas / Calificaciones** | Pendiente | Consulta de notas por ciclo y curso |
| **Asistencias** | Pendiente | Registro y consulta de asistencias del estudiante |
| **Horario** | Pendiente | Visualización del horario semanal |
| **Trámites** | Pendiente | Solicitudes y seguimiento de trámites académicos |
| **Pagos / Estado de cuenta** | Pendiente | Consulta de deudas, boletas y pagos |
| **Noticias / Comunicados** | Pendiente | Feed de comunicados institucionales |
| **Perfil del estudiante** | Pendiente | Datos personales, foto de perfil, datos académicos |
| **Notificaciones Push** | Pendiente | Alertas de notas, comunicados y recordatorios |

---

## Estrategia de ramas

| Rama | Propósito |
|------|-----------|
| `main` | Código estable. Recibe squash merges desde feature branches. |
| `feature/[nombre]` | Desarrollo de cada módulo nuevo. Se conserva después del merge. |

**Regla de merge:** siempre squash merge (`git merge --squash`) para mantener `main` con historial limpio, un commit por feature.

---

## Flujo de trabajo por módulo

1. Crear rama `feature/nombre-modulo`.
2. Desarrollar: Domain → Data → ViewModel → UI.
3. Agregar métodos nuevos a `AppRepository` (no crear repos separados).
4. Commit en la rama feature con historial detallado.
5. Squash merge a `main` con un commit descriptivo.
6. La rama feature se conserva intacta.

---

## Decisiones técnicas tomadas

| Decisión | Motivo |
|----------|--------|
| KMP en lugar de Flutter o React Native | Reutilización máxima de código Kotlin, acceso a APIs nativas, mismo stack que el equipo Android |
| Koin en lugar de Hilt/Dagger | Compatible con KMP sin KSP multiplataforma |
| Repositorio centralizado (`AppRepository`) | Evitar proliferación de repositorios por módulo; facilita testing |
| M2 + M3 coexistentes | Los componentes legados leen de M2; las pantallas nuevas usan M3 |
| `ignoreUnknownKeys = true` en Ktor | La API del backend puede devolver campos extras sin romper el cliente |
| Valores por defecto en todos los campos de DTO | Evita `MissingFieldException` cuando la API no devuelve todos los campos |
