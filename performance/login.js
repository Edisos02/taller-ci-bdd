import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 10,
  duration: '30s',
  thresholds: {
    http_req_duration: ['p(95)<500'],
    http_req_failed: ['rate<0.01'],
  },
};

export default function () {
  const baseUrl = __ENV.BASE_URL || 'http://localhost:8080';
  const res = http.post(`${baseUrl}/login`, JSON.stringify({
    usuario: 'admin',
    clave: '1234'
  }), {
    headers: { 'Content-Type': 'application/json' },
  });

  check(res, {
    'HTTP 200': (r) => r.status === 200,
    'respuesta menor a 500 ms': (r) => r.timings.duration < 500,
  });

  sleep(1);
}