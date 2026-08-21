Feature: Autenticación de usuarios
  Como usuario registrado
  Quiero iniciar sesión
  Para acceder a las funcionalidades protegidas

  Scenario: Login exitoso con credenciales válidas
    Given que existe un usuario "admin" con clave "1234"
    When el usuario intenta iniciar sesión
    Then el acceso debe ser concedido

  Scenario Outline: Login rechazado con credenciales inválidas
    Given que existe un usuario "admin" con clave "1234"
    When el usuario intenta iniciar sesión con usuario "<usuario>" y clave "<clave>"
    Then el acceso debe ser rechazado

    Examples:
      | usuario  | clave |
      | admin    | 0000  |
      | invitado | 1234  |