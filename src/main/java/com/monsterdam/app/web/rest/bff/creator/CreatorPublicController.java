package com.monsterdam.app.web.rest.bff.creator;

import com.monsterdam.app.service.bff.CreatorBffService;
import com.monsterdam.app.service.dto.bff.CreatorProfileDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator")
@Tag(name = "Public Creator BFF")
public class CreatorPublicController {

    private static final Logger LOG = LoggerFactory.getLogger(CreatorPublicController.class);

    private final CreatorBffService creatorBffService;

    public CreatorPublicController(CreatorBffService creatorBffService) {
        this.creatorBffService = creatorBffService;
    }

    @GetMapping("/{creatorId}")
    public ResponseEntity<CreatorProfileDTO> getCreatorProfile(
        @PathVariable Long creatorId,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "20") int size
    ) {
        LOG.debug("REST request to get creator profile {} type={}", creatorId, type);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(creatorBffService.getCreatorProfile(creatorId, type, pageable));
    }
}
