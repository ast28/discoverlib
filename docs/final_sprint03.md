# Sprint 03 – Execution & Review

## 1. Resultados obtenidos

El objetivo del sprint se ha cumplido con éxito. Se ha implementado la persistencia de datos con SQLite y Room. Además, se ha configurado la autenticación con Firebase, la inyección de dependencias con HILT y una interfaz reactiva a los cambios de la base de datos.

---

## 2. Tareas completadas

| ID | Completada | Comentarios |
|----|------------|------------|
| T1.1-3 | Sí | Configuración de Room con DAOs y entidades completada. |
| T1.4-6 | Sí | CRUD operativo y ViewModels adaptados para usar Room. |
| T2.1-3 | Sí | Pantalla de login operativa y conectada a Firebase. |
| T2.4-5 | Sí | Cierre de sesión y tracking de operaciones con Logcat listos. |
| T3.1-2 | Sí | Registro con Firebase y verificación por email terminados. |
| T3.3 | Sí | Recuperación de contraseña implementada. |
| T4.1-2 | Sí | Datos persistidos localmente y viajes adaptados a múltiples usuarios. |
| T4.4 | Sí | Historial de accesos (login/logout) registrado en tabla. |
| T5.1-2 | Sí | Tests unitarios superados y validaciones (edad, duplicados) aplicadas. |
| T4.3, 5.4| Sí | Documentación `design.md` y esquemas actualizados. |

---

## 3. Desviaciones

1. **Problemas de asincronía y reactividad (Efecto Fantasma):** Al actualizar el usuario, a veces se sobrescribían campos al usar estados viejos en memoria del ViewModel. Se solucionó cambiando la arquitectura a un modelo 100% reactivo usando `Flow` con Room como única fuente de verdad.
2. **Flujo de verificación de email:** Esperar a que el usuario verificase su email sin cerrar la pantalla de registro daba problemas. Se modificó para que, tras el registro, la app envíe al usuario directamente al Login con un aviso amigable.

---

## 4. Retrospectiva

### Qué funcionó bien
- La inyección de dependencias con HILT limpió mucho el código.
- Room y los tests unitarios en memoria funcionaron a la perfección.

### Qué no funcionó
- La sincronización manual de variables en el ViewModel dio fallos de datos desactualizados, obligando a migrar todo a un flujo reactivo puro.

### Qué mejoraremos en el próximo sprint
- Mejor gestion del tiempo y detalles para que sea mas amigable la ui. 

---

## 5. Autoevaluación del equipo (0-10)
**Nota:** 7.5

**Justificación:** La app funciona bien peró se podrían mejorar cosas como scrolls en los formularios para poder ver mejor el campo que estas rellenando (eso se ve si pruebas la app con un movil), que por gestión de tiempo no he podido hacer. 
