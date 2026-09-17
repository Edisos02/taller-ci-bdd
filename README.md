# Taller CI/CD + Pruebas + Despliegue

## Objetivo
Implementar un flujo completo de integración y despliegue continuo para un proyecto Java: control de versiones con estrategia de ramas, pruebas automatizadas (unitarias, de integración y BDD), pipeline de CI en GitHub Actions, y un pipeline de despliegue con validación (acceptance tests) y estrategia Blue-Green.

## Requisitos
- JDK 17 (o superior, compilando con `--release 17`)
- Maven 3.9+
- Git
- Docker Desktop (para el despliegue local)

## Estrategia de ramas: Trunk-Based Development
El proyecto usa un flujo Trunk-Based simplificado:
- `main`: rama estable, lista para producción.
- `develop`: rama de integración, donde convergen las ramas de trabajo.
- `feature/*`: ramas de corta duración para cada tarea (ej. `feature/tests-unitarios`, `feature/deploy-pipeline`), que se integran a `develop` mediante merge `--no-ff` y luego se eliminan.

No se usan ramas `release/` ni `hotfix/` (eso correspondería a GitFlow completo); la integración es frecuente y directa a `develop`.

Convención de commits: `chore` (configuración), `test` (pruebas), `ci` (pipeline), `docs` (documentación), `feat` (funcionalidad), `fix` (correcciones).

## Estrategia de pruebas

| Tipo | Herramienta | Ubicación | Qué valida |
|---|---|---|---|
| Unitarias | JUnit 5 | `CalculadoraTest.java` | Lógica aislada de una clase |
| Integración | JUnit 5 + `HttpClient` | `LoginIntegrationTest.java` | `LoginHttpServer` + `LoginService` funcionando juntos, extremo a extremo sobre HTTP real |
| BDD | Cucumber | `features/login.feature` + `bdd/steps` | Escenarios de negocio en lenguaje Gherkin sobre el flujo de login |

Las dependencias de pruebas (JUnit, Cucumber, Selenium + WebDriverManager) están declaradas en `pom.xml`.

## Ejecución local de las pruebas

```bash
git clone https://github.com/Edisos02/taller-ci-bdd.git
cd taller-ci-bdd
mvn clean test
```

Resultados en `target/surefire-reports/`. Reporte HTML navegable:
```bash
mvn clean test surefire-report:report
```
Queda en `target/site/surefire-report.html`. El reporte de Cucumber queda en `target/cucumber-report.html`.

## Pipeline de Integración Continua (CI)
Definido en `.github/workflows/ci.yml`, se dispara en cada push/PR a `main` o `develop`. Tiene dos stages explícitos:
1. **Stage 1 - Build**: compila el proyecto (`mvn clean compile`).
2. **Stage 2 - Test**: ejecuta todas las pruebas (unitarias + integración + BDD).

Luego publica reportes HTML y resultados JUnit como artefactos descargables desde la pestaña *Actions* de GitHub.

## Pipeline de Despliegue (Blue-Green)
Documentado en detalle en [`docs/DEPLOYMENT-PIPELINE.md`](docs/DEPLOYMENT-PIPELINE.md). Resumen:

1. Se construye la imagen Docker con `Dockerfile` (build multi-stage).
2. La nueva versión se despliega como contenedor **Green** en un puerto de prueba (8081), en paralelo a la versión estable **Blue** (8080).
3. Se ejecutan *acceptance tests* (peticiones HTTP reales) contra Green.
4. Si pasan, Green se promueve y reemplaza a Blue sin downtime.
5. Si fallan, Green se descarta y Blue nunca se ve afectado — este es el mecanismo de seguridad/rollback del pipeline.

Ejecución local rápida:
```bash
docker build -t taller-ci-bdd:v1 .
docker run -d --name taller-blue -p 8080:8080 taller-ci-bdd:v1
curl -X POST http://localhost:8080/login -d "{\"usuario\":\"admin\",\"clave\":\"1234\"}"
```

## Estructura del proyecto
```
taller-ci-bdd/
├── .github/workflows/ci.yml         # Pipeline de CI
├── docs/
│   ├── DASHBOARD-ALERTAS.md
│   ├── THREE-AMIGOS.md
│   └── DEPLOYMENT-PIPELINE.md        # Documentación del pipeline de despliegue
├── performance/login.js              # Script de prueba de carga (k6)
├── src/
│   ├── main/java/cl/empresa/demo/
│   │   ├── Calculadora.java
│   │   ├── LoginHttpServer.java
│   │   └── LoginService.java
│   └── test/java/cl/empresa/
│       ├── demo/CalculadoraTest.java         # Pruebas unitarias
│       ├── demo/LoginIntegrationTest.java    # Pruebas de integración
│       └── bdd/                              # Pruebas BDD (Cucumber)
├── Dockerfile                        # Build multi-stage para despliegue
├── pom.xml
└── README.md
```