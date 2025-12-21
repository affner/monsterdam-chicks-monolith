# BFF (Backend For Frontend) - MonsterdamChicks

Este documento describe el alcance inicial (MVP) del BFF para MonsterdamChicks, el cual expone endpoints diseñados para el Frontend con DTOs amigables y estables.

> **Objetivo:** entregar “carnita” útil al Front (Discovery + Set Preview/Detail + Creator Profile + Menu por rol), sin que el Front tenga que orquestar múltiples llamadas ni depender de DTOs internos/crud.

---

## 1) Principios del BFF (MVP)

### 1.1 No exponer `deleteDate`

En el BFF **no debe existir** `deleteDate` en:

- respuestas JSON
- DTOs de cara al Front
- contratos OpenAPI
- payloads o modelos expuestos

Esto aplica a todo lo que salga por endpoints BFF públicos.

### 1.2 Borrado lógico (obligatorio)

El BFF debe filtrar entidades “borradas lógicamente” usando **el mecanismo real del proyecto** (por ejemplo: `deleteDate=null`, `active=true`, `status=ENABLED`, etc.).

> Regla: **NO usar `deleteDate` para filtrar**, ni depender de endpoints CRUD generados para la vista pública.

### 1.3 MVP Fase 1: desbloqueo por ZIP

En esta fase:

- Si el set está **pagado/desbloqueado**: se regresa `downloadUrl` del ZIP.
- Si **no está pagado**: solo previews/públicos, y flags para que el front sepa que puede desbloquear.

Streaming/visualización online del contenido pagado queda fuera del alcance de esta fase.

---

## 2) Estructura de paquetes (basada en el diseño actual)

### 2.1 Controllers (BFF)

Paquete base:

- `com.monsterdam.app.web.rest.bff`

Sub-paquetes:

- `viewer/`
  - Público: `/api/viewer/**` y `/api/sets/**`
  - Interno viewer: `/api/bff/viewer/**` (perfil, librería)
- `creator/`
  - Público: `/api/creator/**`
  - Interno creator: `/api/bff/creator/**` (dashboard, earnings, content tools)
- `common/`
  - Utilidades generales y Utilidades públicas sin login (landing, search, account, settings, catalog interno)

> Nota: Los endpoints “públicos” son los que José va a consumir primero para Discovery, Preview, Profile y Detail.

### 2.2 Servicios BFF

- `com.monsterdam.app.service.bff.ViewerBffService`
- `com.monsterdam.app.service.bff.CreatorBffService`

Regla:

- Controller delgado → BffService arma DTOs → repos/queries recuperan data → mapper transforma → response.

### 2.3 DTOs BFF

- `com.monsterdam.app.service.dto.bff.*`
  - `MenuDTO`
  - `SingleSetDTO`
  - `ContentSetDTO`
  - `SetDetailDTO`
  - `CreatorProfileDTO`
  - (y los que se requieran, siempre sin deleteDate)

---

## 3) Endpoints públicos base (MVP)

### 3.1 Viewer público

- `GET /api/viewer/menu`

  - Devuelve `MenuDTO`
  - JWT opcional: si viene, enriquece roles; si no, ANONYMOUS.

- `GET /api/viewer/set/list`

  - Discovery: lista paginada de `SingleSetDTO`
  - Query params:
    - `sort`: `RATING | RANDOM | RECENT`
    - `type`: `FREE | PAID | ALL`
    - `page`, `size` (paginación JHipster con headers)

- `GET /api/viewer/set/content/{id}`
  - Devuelve `ContentSetDTO`
  - Si no pagado: items públicos/preview + `canUnlock`, sin `downloadUrl`
  - Si pagado: items completos + `downloadUrl` (ZIP)

### 3.2 Set detail público

- `GET /api/sets/{setId}`
  - Devuelve `SetDetailDTO`
  - Misma lógica locked/unlocked que content

### 3.3 Creator público

- `GET /api/creator/{creatorId}`
  - Devuelve `CreatorProfileDTO` (bio + sets del creador)
  - Params:
    - `type`: `FREE | PAID | ALL`
    - `page`, `size`

---

## 4) OpenAPI (SDD)

La fuente de verdad del contrato está en:

- `specs/bff/openapi.yaml`

Reglas del contrato:

- `specs/bff/rules.md`

Guía para Codex:

- `specs/bff/codex.md`

---

## 5) Notas operativas (para demo)

- Mantener los endpoints públicos estables (DTOs sin cambios bruscos).
- Aceptar que “paid/unlocked” puede iniciar como placeholder, pero debe estar claramente encapsulado en `ViewerBffService`.
- Prioridad de entrega:
  1. menu
  2. discovery list
  3. content set (preview/unlocked)
  4. creator profile
  5. set detail
