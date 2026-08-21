# Taller CI + BDD + Performance

## Objetivo
Implementar un flujo básico de integración continua para un proyecto Java,
incorporando Git, Maven, JUnit, reporting navegable, y (próximamente) BDD
con Cucumber y una prueba de performance con k6.

## Requisitos
- JDK 17 (o superior, compilando con `--release 17`)
- Maven 3.9+
- Git

## Ejecución local

```bash
git clone https://github.com/Edisos02/taller-ci-bdd.git
cd taller-ci-bdd
mvn clean test
```

Los resultados quedan en `target/surefire-reports/`.

Para generar el reporte HTML navegable:
```bash
mvn clean test surefire-report:report
```
El reporte queda en `target/site/surefire-report.html`.

## Flujo Git utilizado

```bash
git init
git add .
git commit -m "chore: inicializa proyecto Maven"
git branch develop
git checkout -b feature/tests-unitarios
git add .
git commit -m "test: agrega documentacion a pruebas de calculadora"
git checkout develop
git merge --no-ff feature/tests-unitarios
```

Se usa una convención semántica simple en los mensajes de commit: `chore` para
configuración, `test` para cambios de pruebas, `ci` para el pipeline y `fix`
para correcciones.

## Estructura del proyecto