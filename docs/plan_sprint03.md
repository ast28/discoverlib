# Sprint 03 – Planning Document

## 1. Sprint Goal
El objetivo de este sprint es tener una base de datos real y la parte de autenticación hecha. Tambien hacer bien la injección de dependencias con HILT. 

 

---

## 2. Sprint Backlog

| ID | Tarea | Responsable | Estimación (h) | Prioridad |
|----|-------|-------------|----------------|-----------|
| T1.1-3 | Configurar Room Database, definir Entidades (Trip/ItineraryItem) y crear DAOs | Alba | 2 | Alta |
| T1.4-6 | Adaptar ViewModels y asegurar actualización de UI | Alba | 2.5 | Alta |
| T2.1-3 | Diseñar pantalla de login e implementarla | Alba | 2 | Alta |
| T2.4-5 | Implementar logout | Alba | 1 | Media |
| T3.1-2 | Diseñar pantalla de registro y verificación de email | Alba | 3 | Alta |
| T3.3 | Implementar recuperación de contraseña | Alba | 2 | Media |
| T4.1-2 | Persistir datos del usuario en base de datos local y adaptar tabla de viajes para el usuario | Alba | 2 | Alta |
| T4.4 | Crear tabla para registrar historial de accesos como login | Alba | 1 | Media |
| T5.1-2 | Escribir pruebas unitarias | Alba | 2 | Alta |
| T4.3, T5.4| Actualizar documentación en design.md con esquema de base de datos | Alba | 1 | Media |

---

## 3. Definition of Done (DoD)

- [ ] Migrar CRUD de viajes e itinerarios a Room.
- [ ] Implementar patrón Repository y HILT.
- [ ] Autenticación completa con Firebase (Login, Registro, Logout, Recuperar clave).
- [ ] Persistir datos del usuario logueado en la base de datos local.
- [ ] Guardar historial de accesos (login/logout).
- [ ] Adaptar tabla de viajes para mostrar solo los del usuario logueado.
- [ ] Validar datos (nombres duplicados y fechas).
- [ ] Pasar las pruebas unitarias de DAOs.
- [ ] Vídeo demostrativo.
- [ ] Release `v3.x.x` subida.

---

## 4. Riesgos identificados

- Desconocimiento de Room y Firebase Auth

---

⚠ Este documento no puede modificarse después del 30% del sprint.
Fecha límite modificación: 12/04/2026
