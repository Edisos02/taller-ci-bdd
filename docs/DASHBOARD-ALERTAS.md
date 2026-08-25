# Propuesta de Dashboard y Alertas

## Dashboard

| Sección | Indicador | Fuente del dato | Umbral / Quality gate |
|---|---|---|---|
| Pruebas unitarias | % de pruebas aprobadas | `surefire-reports/*.xml` | 100% en `develop`/`main` |
| BDD | Escenarios ejecutados y aprobados | `cucumber.xml` | 100% de escenarios críticos |
| Duración de suite | Tiempo total de ejecución | Logs del job de CI | Tendencia estable |
| Performance | RPS, latencia p95, tasa de errores | Salida de k6 | p95 < 500 ms · errores < 1% |
| Estado global | PASS / FAIL | Combinación de los anteriores | PASS solo si todos los gates se cumplen |

En una implementación real, estos datos podrían exportarse hacia una
herramienta de series temporales (Grafana + InfluxDB o Prometheus, que es
la integración nativa de k6), permitiendo observar tendencias entre
ejecuciones. Como alternativa simple y suficiente para este taller, el
propio resumen de GitHub Actions junto con los artefactos de Surefire y
Cucumber ya cumplen la función de dashboard básico navegable.

## Alertas automáticas

El fallo de cualquier prueba funcional o BDD marca el pipeline como
fallido y debería generar una notificación al equipo. Para performance,
los `thresholds` de k6 actúan como *quality gates*: si el p95 supera los
500 ms o la tasa de errores llega al 1% o más, la ejecución se considera
fallida.

**Tratamiento diferenciado:**
- Fallo funcional → alerta inmediata (indica una regresión concreta).
- Degradación de performance → se confirma contra una línea base o varias
  ejecuciones antes de escalar, para evitar falsos positivos por ruido
  puntual del entorno de CI.

**Contenido mínimo del mensaje de alerta:** commit, rama, job, prueba
afectada, y enlace directo al reporte correspondiente.

**Canal propuesto:** Slack o correo corporativo, integrado directamente
en el pipeline de GitHub Actions (por ejemplo, con una acción como
`slackapi/slack-github-action` en un paso `if: failure()`).