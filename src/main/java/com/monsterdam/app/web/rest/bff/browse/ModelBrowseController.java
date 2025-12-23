package com.monsterdam.app.web.rest.bff.browse;

import com.monsterdam.app.service.bff.MediaTokenService;
import com.monsterdam.app.service.bff.ModelBrowseService;
import com.monsterdam.app.service.dto.bff.ModelDto;
import com.monsterdam.app.service.dto.bff.PackageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for browsing models and their content.  The methods
 * implemented here expose read‑only endpoints under `/api/public/browse` that
 * return lightweight DTOs for consumption by the front end.  These DTOs
 * include precomputed URLs for the creators' profile pictures and content
 * thumbnails as well as counts for posts and packages.  All pagination and
 * search parameters follow the conventions established by other browse
 * controllers.
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
     * GET  /models : return a paginated list of models.
     *
     * Clients may search by nick name or full name using the optional
     * {@code q} query parameter.  Additionally, a simple text filter
     * {@code style} can be provided to filter by the creator's primary style
     * (e.g. “goth”, “punk”).  When no parameters are provided all active
     * creators are returned ordered by the most recently created profiles.
     *
     * @param pageable the pagination information
     * @param q the free text search query
     * @param style the style filter
     * @return the list of creators, with pagination headers
     */
    @GetMapping("/models")
    @Operation(
        summary = "List models",
        description = "Return a paginated list of models.  Results can be filtered via optional text search and style parameters."
    )
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "The list of models",
                content = @Content(schema = @Schema(implementation = ModelDto.class))
            ),
        }
    )
    public ResponseEntity<Page<ModelDto>> listModels(
        @ParameterObject Pageable pageable,
        @RequestParam(value = "q", required = false) String q,
        @RequestParam(value = "style", required = false) String style
    ) {
        Page<ModelDto> page = modelBrowseService.searchModels(q, style, pageable);
        // Each DTO already contains the correct image URLs; no further processing required.
        return ResponseEntity.ok(page);
    }

    /**
     * GET  /models/{id} : get the detailed profile for a single model.
     *
     * The response includes the creator's public profile information, a
     * selection of their recent posts and a summary of packages they offer.
     * Clients may supply an optional {@code limit} parameter to cap the number
     * of posts and packages returned.  This method defers to the
     * {@link ModelBrowseService} for all business logic.
     *
     * @param id the id of the model to retrieve
     * @param limit the maximum number of posts and packages to include
     * @return the response entity with status 200 (OK) and the model details
     */
    @GetMapping("/models/{id}")
    @Operation(
        summary = "Get model details",
        description = "Retrieve the public profile of a creator along with a preview of their posts and packages."
    )
    @ApiResponses(
        {
            @ApiResponse(
                responseCode = "200",
                description = "The model profile",
                content = @Content(schema = @Schema(implementation = ModelDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Model not found", content = @Content),
        }
    )
    public ResponseEntity<ModelDto> getModel(@PathVariable("id") Long id, @RequestParam(value = "limit", required = false) Integer limit) {
        return modelBrowseService.getModelDetails(id, limit).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET  /models/{modelId}/packages : list packages for a specific model.
     *
     * Returns a paginated list of the content packages created by the given
     * model.  Each package entry includes counts of videos and images as well
     * as a thumbnail URL representing the package.  Clients can restrict
     * results to only free or only paid content via the {@code paid}
     * parameter.
     *
     * @param modelId the id of the model
     * @param pageable the pagination information
     * @param paid if true, return only paid packages; if false, only free packages; null returns all
     * @return a page of PackageDto objects
     */
    @GetMapping("/models/{modelId}/packages")
    @Operation(summary = "List a model's packages", description = "Paginated listing of content packages offered by a specific creator.")
    public ResponseEntity<Page<PackageDto>> listModelPackages(
        @PathVariable("modelId") Long modelId,
        @ParameterObject Pageable pageable,
        @RequestParam(value = "paid", required = false) Boolean paid
    ) {
        Page<PackageDto> page = modelBrowseService.listPackagesByModel(modelId, paid, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * GET  /packages/{packageId} : get full details for a content package.
     *
     * Returns the meta‑data for the package along with lists of photo and
     * video identifiers.  The front end may call the media endpoint to
     * exchange those identifiers for temporary download URLs.  In the
     * initial phase, the identifiers map directly to filesystem paths.
     *
     * @param packageId the id of the package
     * @return the package detail with its media ids
     */
    @GetMapping("/packages/{packageId}")
    @Operation(
        summary = "Get package details",
        description = "Retrieve the name, description, price and media item identifiers for a package."
    )
    public ResponseEntity<PackageDto> getPackage(@PathVariable("packageId") Long packageId) {
        return modelBrowseService.getPackageDetails(packageId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /media/{mediaId} : exchange a media id for a download URL.
     *
     * This endpoint provides an abstraction layer over the underlying
     * storage.  During early development it returns a filesystem path to
     * serve as a stand‑in for a pre‑signed S3 URL.  Once the front end has
     * direct access to the database the implementation can be swapped out to
     * generate real pre‑signed URLs without requiring changes to the UI.
     *
     * @param mediaId the id of the media item (photo or video)
     * @return a plain string containing the download URL
     */
    @GetMapping("/media/{mediaId}")
    @Operation(summary = "Get media URL", description = "Given a media identifier, return a temporary URL for download.")
    public ResponseEntity<String> getMediaUrl(@PathVariable("mediaId") Long mediaId) {
        return ResponseEntity.ok(mediaTokenService.getTokenForMedia(mediaId));
    }
}
