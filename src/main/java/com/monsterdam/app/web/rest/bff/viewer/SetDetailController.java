package com.monsterdam.app.web.rest.bff.viewer;

import com.monsterdam.app.service.bff.ViewerBffService;
import com.monsterdam.app.service.dto.bff.SetDetailDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Public Set Detail BFF")
public class SetDetailController {

    private static final Logger LOG = LoggerFactory.getLogger(SetDetailController.class);

    private final ViewerBffService viewerBffService;

    public SetDetailController(ViewerBffService viewerBffService) {
        this.viewerBffService = viewerBffService;
    }

    @GetMapping("/sets/{setId}")
    public ResponseEntity<SetDetailDTO> getSetDetail(@PathVariable("setId") Long setId) {
        LOG.debug("REST request to get set detail {}", setId);
        return ResponseEntity.ok(viewerBffService.getSetDetailCanonical(setId));
    }
}
