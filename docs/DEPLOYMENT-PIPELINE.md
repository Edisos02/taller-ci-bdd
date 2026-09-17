# Pipeline de Despliegue — Estrategia Blue-Green

## Objetivo
Desplegar nuevas versiones de la aplicación sin interrumpir el servicio, validando cada versión candidata con *acceptance tests* antes de promoverla a producción. Si una versión falla la validación, se descarta automáticamente sin afectar la versión estable en curso.

## Componentes
- **Dockerfile**: build multi-stage (Maven+JDK para compilar, JRE-alpine para ejecutar), genera una imagen liviana de `LoginHttpServer`.
- **Blue**: contenedor estable, sirviendo tráfico real en el puerto `8080`.
- **Green**: contenedor candidato, desplegado en paralelo en el puerto `8081` para pruebas de aceptación, sin afectar a Blue.

## Flujo del pipeline

1. **Build**: se construye la imagen Docker de la nueva versión.