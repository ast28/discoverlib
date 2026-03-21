# Resultados de las Pruebas y Correcciones

En este documento explico las pruebas unitarias que se han programado para comprobar que las operaciones CRUD de la app funciona bien. Las pruebas se han dividido en dos partes principales: la gestión de los datos (Repository) y las reglas de validación (ViewModel).

---

## 1. Pruebas del Repository 

Aquí hemos comprobado que las operaciones básicas de guardar, leer, editar y borrar funcionan sin perder datos ni romper la app a nivel de repositorio. 

* **Test de guardar (`addTrip` / `addActivity`):** Comprobamos que, al enviarle un objeto nuevo, este se añade correctamente a la lista y el tamaño total de la base de datos aumenta.
* **Test de buscar (`getOneTrip` / `getOneActivity`):** Verificamos que si le pedimos un ID concreto, nos devuelva exactamente ese viaje o actividad. También probamos a pasarle un ID falso para comprobar que simplemente devuelve `null` sin hacer que la app se cuelgue.
* **Test de editar (`editTrip` / `editActivity`):** Comprobamos que al actualizar un elemento, solo se modifican los datos de ese elemento en concreto y el resto de la lista se queda intacta.
* **Test de borrar (`deleteTrip` / `deleteActivity`):** Verificamos que al eliminar un viaje por su ID, este desaparece completamente de la lista.

---

## 2. Pruebas del ViewModel (Lógica y Reglas)

En esta parte, los tests consisten en introducir a propósito datos incorrectos o incompletos. El objetivo es comprobar que el ViewModel revisa bien la información, frena el guardado y devuelve el error correspondiente.

* **Tests de campos vacíos y límites:** Intentamos guardar viajes y actividades dejando el título o la ubicación en blanco. También intentamos crear un viaje con un título de más de 30 caracteres para comprobar que el sistema lo rechaza.
* **Tests de coherencia de fechas:** Probamos a crear un viaje donde la fecha de fin es anterior a la de inicio, o empezar un viaje en el pasado. El test verifica que el ViewModel lance un mensaje de error pidiendo revisar las fechas.
* **Tests de fechas en actividades:** Comprobamos que no se pueda agendar una actividad en un día que cae fuera de las fechas del viaje principal.
* **Tests de solapamiento y duplicados:**
    * Intentamos meter dos actividades a la misma hora y el mismo día para verificar que salta el aviso de conflicto de horarios.
    * Intentamos crear una actividad con un nombre que ya existe en ese viaje para comprobar que no deja duplicarlas.

---

## 3. Resultados de los Tests

**Resultados 100% correctos (PASSED).**
Tras ejecutar todos los tests, el código pasa las pruebas sin problema. El `Repository` guarda la información de forma segura y el `ViewModel` aplica todas las restricciones de fechas y campos obligatorios devolviendo los `ValidationResult` correctos en cada caso.

---

