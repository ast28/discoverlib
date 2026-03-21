# Sprint 02 – Execution & Review

## 1. Resultados obtenidos

Comparación con Sprint Goal: El objetivo del sprint se ha cumplido con éxito en su totalidad. Se ha implementado toda la lógica principal de la aplicación utilizando la arquitectura MVVM. Y se ha comprobado que todo funciona con los tests y los logs. 

---

## 2. Tareas completadas

| ID | Completada | Comentarios |
|----|------------|------------|
| T1.1 | Sí | CRUD de Trips implementado correctamente en Repository y ViewModel. |
| T1.2 | Sí | CRUD de Activities implementado y enlazado a sus viajes correspondientes. |
| T1.3 | Sí | Validaciones creadas: fechas coherentes, límites de texto y prevención de duplicados. |
| T1.4 | Sí | Ajustes de usuario guardados usando `SharedPreferences`. |
| T1.5 | Sí | Archivos de recursos (`strings.xml`) configurados para soportar multidioma. |
| T2.1 | Sí | Flujo estructurado siguiendo el patrón MVVM. |
| T2.2 | Sí | Diálogos de creación/edición y navegación entre detalles implementados. |
| T2.3 | Sí | La interfaz reacciona y se actualiza sola al guardar o borrar gracias a `StateFlow`. |
| T3.1 | Sí | Los formularios bloquean el guardado y muestran avisos en rojo si los datos están mal. |
| T3.2 | Sí | Tests unitarios programados y superados al 100% (Status: PASSED). |
| T3.3 | Sí | Documento de pruebas y fixes (T3.4) redactado y actualizado. |
| T3.4 | Sí | Logs añadidos para rastrear navegaciones, aciertos y errores en el ViewModel. |

---

## 3. Desviaciones

1. **Falsos errores en los Logs (Carrera de estados):** Al entrar a ver los detalles de un viaje o actividad, el sistema lanzaba un error rojo en el Logcat diciendo que no lo encontraba. El problema era que la pantalla intentaba buscar los datos antes de que la lista terminara de cargar. Se ha solucionado comprobando que la lista no este vacía. 
2. **GitHub:** Al juntar la app con el repositorio he tenido dificultades por la estructura. Finalmente está bien estructurado el proyecto. 

---

## 4. Retrospectiva

### Qué funcionó bien
- Estructura aplicada correctamente
- Tests hechos
- Logs hechos

### Qué no funcionó

### Qué mejoraremos en el próximo sprint
- Mejorar la ui. 

---

## 5. Autoevaluación del equipo (0-10)
Nota: 6

Justificación: Podría mejorarse la ui, ya que el dark mode no es muy estetico y al girar el móbil se ve igual que recto. 
