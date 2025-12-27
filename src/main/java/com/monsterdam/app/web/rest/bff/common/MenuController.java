package com.monsterdam.app.web.rest.bff.common;

import com.monsterdam.app.service.bff.ViewerBffService;
import com.monsterdam.app.service.dto.bff.MenuDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
@Tag(name = "Menu Principal")
public class MenuController {

    private static final Logger LOG = LoggerFactory.getLogger(MenuController.class);
    private final ViewerBffService viewerBffService;

    public MenuController(ViewerBffService viewerBffService) {
        this.viewerBffService = viewerBffService;
    }

    @GetMapping("/menu")
    public ResponseEntity<MenuDTO> getMenu() {
        LOG.debug("REST request to get viewer menu");
        return ResponseEntity.ok(viewerBffService.buildMenu());
    }
}
