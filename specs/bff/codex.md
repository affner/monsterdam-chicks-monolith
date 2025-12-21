# Guía para Codex (SDD) - BFF MVP

Este archivo define el “modo de trabajo” para que Codex implemente/ajuste el BFF **respetando el contrato OpenAPI** y las reglas del proyecto.

Archivos fuente de verdad:

- Contrato: `specs/bff/openapi.yaml`
- Reglas: `specs/bff/rules.md`

---

## 1) Contexto del proyecto

- Stack: Java + Spring Boot (estilo JHipster).
- Controllers BFF ya existen bajo: `com.monsterdam.app.web.rest.bff.*`
- Services BFF base:
  - `com.monsterdam.app.service.bff.ViewerBffService`
  - `com.monsterdam.app.service.bff.CreatorBffService`
- DTOs BFF:
  - `com.monsterdam.app.service.dto.bff.*`

---

## 2) Reglas no negociables (leer antes de tocar código)

1. **No exponer `deleteDate`**

- Nunca en DTO, JSON, OpenAPI.

2. **Filtrar borrado lógico usando el mecanismo real del proyecto**

- NO usar `deleteDate`.
- Identificar campo real (deleted/active/status/etc.) y aplicarlo en queries/repos.

3. **No usar CRUD Resources para endpoints públicos**

- El BFF arma DTOs dedicados (bff DTOs).

4. **MVP fase 1: contenido pagado → ZIP**

- Si unlocked: incluir `downloadUrl`.
- Si locked: solo preview público.

---

## 3) Endpoints MVP a asegurar (deben cumplir OpenAPI)

### Viewer público

- `GET /api/viewer/menu`

  - Implementación: `ViewerPublicController#getMenu()` → `viewerBffService.buildMenu()`
  - JWT opcional: si hay token, detectar roles; si no, ANONYMOUS.

- `GET /api/viewer/set/list`

  - Implementación: `ViewerPublicController#listSets(...)` → `viewerBffService.listSets(sort,type,pageable)`
  - Debe:
    - aceptar sort: RATING | RANDOM | RECENT
    - aceptar type: FREE | PAID | ALL
    - aplicar filtro de borrado lógico
    - regresar headers de paginación JHipster

- `GET /api/viewer/set/content/{id}`
  - Implementación: `ViewerPublicController#getContentSet(id)` → `viewerBffService.getContentSet(id)`
  - Debe:
    - aplicar filtro de borrado lógico
    - determinar unlocked (puede iniciar como placeholder si aún no existe compra)
    - locked → previews
    - unlocked → todo + downloadUrl

### Creator público

- `GET /api/creator/{creatorId}`
  - Implementación: `CreatorPublicController#getCreatorProfile(...)` → `creatorBffService.getCreatorProfile(...)`
  - Debe:
    - aplicar filtro de borrado lógico
    - devolver CreatorProfileDTO y lista de sets

### Set detail público

- `GET /api/sets/{setId}`
  - Implementación: `SetDetailController#getSetDetail(setId)` → `viewerBffService.getSetDetail(setId)`
  - Misma lógica locked/unlocked que content

---

## 4) Trabajo esperado (pasos de Codex)

### Paso A: Inspección

1. Encontrar el mecanismo real de borrado lógico en entidades relevantes:

- set/package/post/media/creator/profile

2. Encontrar cómo detectar roles desde el contexto de seguridad (JWT opcional).
3. Verificar DTOs BFF existentes (`MenuDTO`, `SingleSetDTO`, `ContentSetDTO`, `SetDetailDTO`, `CreatorProfileDTO`) y ajustarlos a OpenAPI.

### Paso B: Implementación mínima

- Mantener Controllers delgados.
- Implementar lógica en `ViewerBffService` y `CreatorBffService`.
- Crear repos/queries específicas si los repos actuales no filtran borrado lógico.

### Paso C: Pruebas (mínimas pero útiles)

Agregar/ajustar tests para:

- menu anon vs autenticado (roles)
- listSets con headers de paginación
- content locked vs unlocked (downloadUrl solo si unlocked)
- 404 cuando entidad no existe o está borrada lógicamente
- verificación de que `deleteDate` no aparece en responses (aserción de JSON)

---

## 5) Checklist final antes de dar por terminado

- [ ] Los endpoints cumplen `specs/bff/openapi.yaml`.
- [ ] Ninguna respuesta incluye `deleteDate`.
- [ ] Todas las consultas filtran borrado lógico por el mecanismo real (no deleteDate).
- [ ] Endpoints públicos NO dependen de CRUD Resources.
- [ ] Paginación con headers JHipster en listados.
- [ ] Tests pasan.

---

## 6) Prompt sugerido para ejecutar con Codex

> Úsalo tal cual

Implementa y/o ajusta los endpoints públicos del BFF para cumplir el contrato en `specs/bff/openapi.yaml` y las reglas en `specs/bff/rules.md`.

Reglas críticas:

- No exponer `deleteDate` en DTOs/JSON/OpenAPI.
- Filtrar borrado lógico usando el mecanismo real del proyecto (NO deleteDate).
- No usar CRUD Resources para endpoints públicos.
- MVP: si está pagado/desbloqueado, devolver `downloadUrl` del ZIP; si no, solo previews públicos.

Tareas:

1. Asegurar `GET /api/viewer/menu` (roles + menu).
2. Asegurar `GET /api/viewer/set/list` (sort/type/paging + headers + filtro borrado lógico).
3. Asegurar `GET /api/viewer/set/content/{id}` (locked/unlocked + downloadUrl).
4. Asegurar `GET /api/creator/{creatorId}` (perfil + sets + filtro borrado lógico).
5. Asegurar `GET /api/sets/{setId}` (detalle locked/unlocked).

Agregar/ajustar tests y ejecutar el suite hasta que pase.
