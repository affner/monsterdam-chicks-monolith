package com.monsterdam.app.web.rest.bff.creator;

import com.monsterdam.app.service.bff.CreatorDashboardService;
import com.monsterdam.app.service.dto.bff.CreatorDashboardSummaryDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator/dashboard")
@Tag(name = "BFF Creator Dashboard")
public class CreatorDashboardController {

    private static final Logger LOG = LoggerFactory.getLogger(CreatorDashboardController.class);

    private final CreatorDashboardService creatorDashboardService;

    public CreatorDashboardController(CreatorDashboardService creatorDashboardService) {
        this.creatorDashboardService = creatorDashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<CreatorDashboardSummaryDTO> getDashboardSummary() {
        LOG.debug("REST request to get creator dashboard summary from BFF");
        return ResponseEntity.ok(creatorDashboardService.getSummary());
    }
}
