package com.monsterdam.app.web.rest.bff.browse;

import com.monsterdam.app.service.bff.MediaTokenService;
import com.monsterdam.app.service.bff.ModelBrowseService;
import com.monsterdam.app.service.dto.bff.ModelDto;
import com.monsterdam.app.service.dto.bff.PackageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST (BFF) para navegación pública de modelos y su contenido.
 *
 * Expone endpoints de solo lectura bajo `/api/public/browse` para que el front
 * pueda:
 * - listar modelos (con búsqueda y filtros),
 * - ver el detalle público de un modelo (preview),
 * - listar paquetes de un modelo,
 * - ver detalle de un paquete,
 * - obtener una URL temporal (token) para descargar un media.
 *
 * Nota: Los DTOs ya deben venir “listos para UI” (con URLs calculadas, contadores, etc.)
 * desde el service, para no meter lógica en el controller.
 */
@RestController
@RequestMapping("/api/public/browse")
public class ModelBrowseController {

    private final ModelBrowseService modelBrowseService;
    private final MediaTokenService mediaTokenService;

    public ModelBrowseController(ModelBrowseService modelBrowseService, MediaTokenService mediaTokenService) {
        this.modelBrowseService = modelBrowseService;
        this.mediaTokenService = mediaTokenService;
    }

    /**
     * GET /models : regresa una lista paginada de modelos.
     *
     * Permite:
     * - búsqueda libre por nick o nombre completo con `textName`
     * - filtro simple por estilo principal con `style` (ej: "goth", "punk")
     *
     * Si no mandas parámetros, regresa todos los modelos activos ordenados por los más recientes.
     */
    @GetMapping("/models")
    @Operation(
        summary = "Listar modelos",
        description = "Regresa una lista paginada de modelos. Se puede filtrar con búsqueda libre (q) y estilo (style)."
    )
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Lista paginada de modelos",
                content = @Content(schema = @Schema(implementation = ModelDto.class))
            ),
        }
    )
    public ResponseEntity<Page<ModelDto>> listModels(
        @ParameterObject Pageable pageable,
        @RequestParam(value = "textName", required = false) String textName,
        @RequestParam(value = "style", required = false) String style
    ) {
        Page<ModelDto> page = modelBrowseService.searchModels(textName, style, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * GET /models/{idModel} : obtiene el perfil público detallado de un modelo.
     *
     * Incluye información pública del modelo + preview (posts recientes y paquetes).
     * `limit` es un tope opcional para limitar cuántos items se regresan en el preview.
     */
    @GetMapping("/models/{idModel}")
    @Operation(
        summary = "Obtener detalle de modelo",
        description = "Obtiene el perfil público del modelo, junto con un preview de posts y paquetes."
    )
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Detalle del modelo",
                content = @Content(schema = @Schema(implementation = ModelDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado", content = @Content),
        }
    )
    public ResponseEntity<ModelDto> getModel(
        @PathVariable("idModel") Long idModel,
        @RequestParam(value = "limit", required = false) Integer limit
    ) {
        return modelBrowseService.getModelDetails(idModel, limit).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /models/{idModel}/packages : lista paginada de paquetes de un modelo.
     *
     * `paid`:
     * - true  => solo paquetes de pago
     * - false => solo paquetes gratis
     * - null  => todos
     */
    @GetMapping("/models/{idModel}/packages")
    @Operation(
        summary = "Listar paquetes de un modelo",
        description = "Regresa una lista paginada de paquetes ofrecidos por un modelo, con filtro opcional por contenido pagado."
    )
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Lista paginada de paquetes",
                content = @Content(schema = @Schema(implementation = PackageDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Modelo no encontrado", content = @Content),
        }
    )
    public ResponseEntity<Page<PackageDto>> listModelPackages(
        @PathVariable("idModel") Long idModel,
        @ParameterObject Pageable pageable,
        @RequestParam(value = "paid", required = false) Boolean paid
    ) {
        Page<PackageDto> page = modelBrowseService.listPackagesByModel(idModel, paid, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * GET /packages/{packageId} : obtiene el detalle completo de un paquete.
     *
     * Regresa metadatos + lista de ids de fotos/videos.
     * El front usa /media/{mediaId} para intercambiar ids por URLs temporales de descarga.
     *
     * En fase inicial, estos ids pueden mapear directo a filesystem; después se puede migrar a pre-signed URLs (S3).
     */
    @GetMapping("/packages/{packageId}")
    @Operation(
        summary = "Obtener detalle de paquete",
        description = "Obtiene nombre, descripción, precio e identificadores de media (fotos/videos) de un paquete."
    )
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "Detalle del paquete",
                content = @Content(schema = @Schema(implementation = PackageDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Paquete no encontrado", content = @Content),
        }
    )
    public ResponseEntity<PackageDto> getPackage(@PathVariable("packageId") Long packageId) {
        return modelBrowseService.getPackageDetails(packageId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /media/{mediaId} : intercambia un id de media por una URL temporal.
     *
     * Esto abstrae el storage:
     * - hoy puede ser filesystem (o un “token” simple),
     * - mañana puede ser una pre-signed URL real (S3/MinIO),
     * sin que el front tenga que cambiar su lógica.
     */
    @GetMapping("/media/{mediaId}")
    @Operation(
        summary = "Obtener URL temporal de media",
        description = "Dado un mediaId (foto o video), regresa una URL temporal para descarga."
    )
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "URL temporal de descarga", content = @Content),
            @ApiResponse(responseCode = "404", description = "Media no encontrado", content = @Content),
        }
    )
    public ResponseEntity<String> getMediaUrl(@PathVariable("mediaId") Long mediaId) {
        return ResponseEntity.ok(mediaTokenService.getTokenForMedia(mediaId));
    }
}
