# ULCB Estudiantes KMP

> **Universidad Le Cordon Bleu Perú**
> Aplicación móvil multiplataforma para estudiantes — Android + iOS.

---

## ¿Qué es este proyecto?

Aplicación intranet para estudiantes del Universidad Le Cordon Bleu, desarrollada con **Kotlin Multiplatform + Compose Multiplatform**. Permite acceder a notas, asistencias, horarios, trámites y más desde Android e iOS con un único código base.

---

## Documentación

Toda la documentación técnica y de diseño está en la carpeta [`docs/`](docs/README.md):

| Audiencia | Documento |
|-----------|-----------|
| Director / Jefe de proyecto | [Plan de desarrollo y roadmap](docs/plan/01-roadmap.md) |
| Diseñador / Marketing | [Paleta de colores institucional](docs/diseno/01-paleta-colores.md) · [Tipografía](docs/diseno/02-tipografia.md) |
| Ingeniero / Desarrollador | [Arquitectura Clean Architecture + MVVM](docs/arquitectura/01-vision-general.md) · [Stack tecnológico](docs/stack/01-tecnologias.md) |

---

## Stack resumido

- **Kotlin Multiplatform** — lógica compartida Android + iOS
- **Compose Multiplatform** — UI declarativa multiplataforma
- **Ktor** — cliente HTTP
- **Koin** — inyección de dependencias
- **MSAL** — autenticación Microsoft (SSO institucional)
- **Clean Architecture + MVVM**

---

## Compilar y ejecutar

### Android
```shell
./gradlew :composeApp:assembleDebug
```

### iOS
Abrir `/iosApp` en Xcode y ejecutar desde ahí, o usar la configuración de ejecución del IDE.

---

## Estructura del repositorio

```
composeApp/
├── src/commonMain/    ← Lógica compartida (UI, ViewModels, Repositorios)
├── src/androidMain/   ← Implementaciones específicas Android
└── src/iosMain/       ← Implementaciones específicas iOS
iosApp/                ← Entry point Swift / Xcode
docs/                  ← Documentación del proyecto
```
