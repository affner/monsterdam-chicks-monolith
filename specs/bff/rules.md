# Reglas del BFF (MVP)

Estas reglas aplican a todos los endpoints del BFF, especialmente a los públicos:

- `/api/viewer/**`
- `/api/creator/**`
- `/api/sets/**`

---

## 1) Prohibido `deleteDate` (cero rastros)

En el BFF **no debe existir** `deleteDate` en:

- DTOs (`com.monsterdam.app.service.dto.bff.*`)
- JSON de respuesta
- contratos OpenAPI
- payloads que consuma el frontend

> Si alguna entidad interna tiene `deleteDate`, se ignora completamente en el BFF y NO se mapea.

---

## 2) Borrado lógico (obligatorio) usando el mecanismo real del proyecto

Todas las consultas del BFF deben **excluir** registros borrados lógicamente usando el campo real del proyecto.

Ejemplos de mecanismos válidos (usa el que aplique en tu proyecto):

- `deleted = false`
- `active = true`
- `enabled = true`
- `status = ACTIVE`

Reglas:

- NO filtrar usando `deleteDate`.
- NO depender de endpoints CRUD “generados” para vistas públicas si esos endpoints no aplican el filtro adecuado.
- El filtro debe vivir en repositorios/queries/servicios, no en el Controller.

---

## 3) MVP Fase 1: desbloqueo y descarga ZIP

En esta fase:

### 3.1 Si NO está pagado/desbloqueado

- Devolver solo previews / items públicos
- `unlocked=false`
- `downloadUrl` no debe existir (o debe ser null)
- Si aplica: `canUnlock=true` y `price/currency`

### 3.2 Si SÍ está pagado/desbloqueado

- Devolver todo el contenido del set
- `unlocked=true`
- Incluir `downloadUrl` (ZIP del set)

> No implementar streaming/visualización online del set pagado en esta fase.

---

## 4) Contrato estable para Front

- DTOs del BFF deben ser “front friendly” (no exponer campos internos o irrelevantes).
- No exponer entidades JPA ni DTOs CRUD si tienen campos internos.
- Mantener nombres y estructura consistente.

---

## 5) Paginación y headers

- En listados paginados usar el estilo JHipster:
  - Header `X-Total-Count`
  - Header `Link` (RFC5988)
- `page` y `size` deben funcionar, además de `Pageable`.

---

## 6) JWT opcional en endpoints públicos

- Endpoints públicos deben funcionar sin JWT (ANONYMOUS).
- Si llega JWT:
  - enriquecer menú (roles)
  - determinar `unlocked` si existe la relación de compra

---

## 7) Logging

- No loguear tokens JWT ni PII sensible.
- Logs deben ser “debug-friendly” pero seguros.
