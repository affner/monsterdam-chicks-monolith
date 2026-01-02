# Monsterdam Chicks

Guía práctica para levantar el proyecto en local y preparar entornos de desarrollo o producción. Este repositorio incluye backend y frontend en un mismo monolito (JHipster), con scripts listos para trabajo local y empaquetado.

## Requisitos

- **Java 17+** (recomendado para el backend)
- **Node.js** (ver versión mínima en `package.json` → `engines.node`)
- **Docker** (opcional, si necesitas servicios externos)

> Consejo: si tienes problemas con versiones de Node, usa la versión indicada en `package.json` para evitar incompatibilidades.

## Instalación rápida

```bash
./npmw install
```

`./npmw` es el wrapper recomendado por JHipster para usar la versión local de Node/NPM instalada por Maven cuando aplica.

## Modos de ejecución (lo más usado)

### 1) Todo en modo desarrollo (backend + frontend en caliente)

```bash
./npmw run watch
```

Esto inicia el backend y el frontend con recarga automática al cambiar archivos.  
Ideal para iterar rápido sin reconstruir.

### 2) Solo backend (perfil dev con H2)

```bash
./npmw run backend:start -- -Pdev
```

Esto levanta Spring Boot con el perfil **dev** (usa H2 por defecto).

### 3) Solo frontend (webpack dev server)

```bash
./npmw start
```

Levanta el servidor de desarrollo del frontend.  
Si también está corriendo el backend, se integra automáticamente.

## Scripts clave

Los comandos principales están en `package.json` → `scripts`:

- **`npm run watch`**: backend + frontend en modo desarrollo.
- **`npm run backend:start`**: solo backend.
- **`npm run start`**: solo frontend en modo desarrollo.
- **`npm run build`**: build de frontend en modo producción.
- **`npm run java:jar:dev`**: empaqueta el backend con perfil dev.
- **`npm run java:jar:prod`**: empaqueta el backend con perfil prod.

## Perfiles recomendados

### Desarrollo (H2, hot reload)

```bash
./npmw run backend:start -- -Pdev
./npmw start
```

### Producción (build completo)

```bash
./npmw run java:jar:prod
java -jar target/*.jar
```

## Servicios con Docker (opcional)

Si necesitas levantar servicios externos (bases de datos u otros):

```bash
docker compose -f src/main/docker/services.yml up --wait
```

Para detenerlos:

```bash
docker compose -f src/main/docker/services.yml down -v
```

## Notas útiles

- El backend expone por defecto en `http://localhost:18080`.
- Puedes ver todos los scripts disponibles con:

```bash
./npmw run
```

---

Este proyecto fue generado con **JHipster 8.11.0**.  
Documentación general: https://www.jhipster.tech/documentation-archive/v8.11.0
