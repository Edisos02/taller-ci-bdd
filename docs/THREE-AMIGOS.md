# Sesión Three Amigos — Funcionalidad: Login

## Funcionalidad seleccionada
Inicio de sesión de usuarios registrados, por tratarse de un flujo con
reglas de negocio claras y ejemplos concretos, ideal para ilustrar BDD.

## Roles participantes

- **Product Owner:** define el valor de negocio y confirma los criterios
  de aceptación.
- **QA / Test Engineer:** identifica riesgos y propone escenarios positivos
  y negativos.
- **Developer:** evalúa la factibilidad técnica y traduce las reglas a un
  diseño concreto.

## Conversación simulada

- **Product Owner:** un usuario registrado debe poder acceder cuando
  entrega credenciales válidas.
- **QA:** ¿y qué ocurre si la clave es incorrecta? Deberíamos rechazar el
  acceso en ese caso.
- **Developer:** podemos modelar la regla como una operación cuyo resultado
  sea "concedido" o "rechazado".
- **QA:** conviene agregar ejemplos concretos para no dejar espacio a
  ambigüedad.
- **Product Owner:** cubramos entonces una credencial correcta y varias
  combinaciones incorrectas.

## Criterios de aceptación acordados

- Con el usuario `admin` y la clave `1234`, el acceso es concedido.
- Con una clave incorrecta, el acceso es rechazado.
- Con un usuario inexistente, el acceso es rechazado.
- Cada escenario debe ser verificable de forma independiente.