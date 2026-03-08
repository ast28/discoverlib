# Sprint 01 – Execution & Review

## 1. Resultados obtenidos

Comparación con Sprint Goal: El objetivo del sprint se ha cumplido con éxito en su totalidad. Se ha configurado correctamente el entorno en Android Studio (Kotlin 2.0.21, Min API 26) y se ha establecido la estructura de carpetas requerida (scaffolding) para la app "Discoverlib". Se han implementado todas las pantallas principales (Home, Trips, Activity, Gallery, Preferences, About, Splash) utilizando Jetpack Compose con datos simulados (mock data). Además, se ha integrado correctamente la navegación entre ellas y se ha generado toda la documentación solicitada (`README.md`, `CONTRIBUTING.md`, `LICENSE` y `design.md`) preparándolo todo para la release final.

---

## 2. Tareas completadas

| ID | Completada | Comentarios |
|----|------------|------------|
| T1.1 | Sí | Nombre "Discoverlib" decidido y aplicado en el proyecto. |
| T1.2 | Sí | Logo generado. |
| T1.3 | Sí | Versión de Android definida (API 26). |
| T1.4 | Sí | Proyecto configurado para usar Kotlin 2.0.21. |
| T1.5 | Sí | Proyecto inicializado en Android Studio. |
| T2.1 | Sí | Repositorio GitHub creado con la estructura de carpetas exigida. |
| T2.2 | Sí | Release GitHub hecho. |
| T3.1 | No | Pantallas principales implementadas, falta login por falta de tiempo. |
| T3.2 | Sí | Navegación configurada entre todas las secciones principales. |
| T3.3 | Sí | Documento `design.md` redactado e incluye el diagrama de clases en formato Mermaid. |
| T3.4 | Sí | Clases de dominio creadas con anotaciones `@TODO`. |
| T4.1 | Sí | Splash screen creada con animación de espera y redirección automática. |
| T4.2 | Sí | Pantalla *About* con información del equipo y versión implementada. |
| T4.3 | Sí | Pantalla *Preferences* desarrollada. |

---

## 3. Desviaciones

1. **Navegación:** Hubo un problema técnico al implementar la navegación hacia los detalles de la actividad (`ActivityScreen`). La aplicación se cerraba inesperadamente (crash) porque se estaban enviando más parámetros, que un principio tenian una utilidad, de los que el grafo de navegación esperaba recibir. Se ha solucionado ajustando el archivo `Routes.kt` y la llamada del `navController`.
2. **GitHub:** Al juntar la app con el repositorio he tenido dificultades por la estructura. Finalmente está bien estructurado el proyecto. 

---

## 4. Retrospectiva

### Qué funcionó bien
- Entorno creado correctamente
- La implementación de las pantallas principales
- La navegación entre ellas 

### Qué no funcionó
- No está hecho aun la autenticación

### Qué mejoraremos en el próximo sprint
- Hacer la parte de autenticación e implementar las funciones de las clases 

---

## 5. Autoevaluación del equipo (0-10)
Nota: 7

Justificación: Podría mejorarse el calculo de tiempo de las tareas, pero las que se pedian están hechas. 