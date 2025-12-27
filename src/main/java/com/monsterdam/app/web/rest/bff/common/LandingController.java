package com.monsterdam.app.web.rest.bff.common;

import com.monsterdam.app.service.AdminAnnouncementService;
import com.monsterdam.app.service.HelpCategoryService;
import com.monsterdam.app.service.dto.AdminAnnouncementDTO;
import com.monsterdam.app.service.dto.HelpCategoryDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/landing")
@Tag(name = "BFF Landing")
public class LandingController {

    private static final Logger LOG = LoggerFactory.getLogger(LandingController.class);

    private final AdminAnnouncementService adminAnnouncementService;
    private final HelpCategoryService helpCategoryService;

    public LandingController(AdminAnnouncementService adminAnnouncementService, HelpCategoryService helpCategoryService) {
        this.adminAnnouncementService = adminAnnouncementService;
        this.helpCategoryService = helpCategoryService;
    }

    @GetMapping("/announcements")
    public ResponseEntity<List<AdminAnnouncementDTO>> getAnnouncements(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get landing announcements from BFF");
        Page<AdminAnnouncementDTO> page = adminAnnouncementService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/announcements/latest")
    public ResponseEntity<AdminAnnouncementDTO> getLatestAnnouncement() {
        LOG.debug("REST request to get latest announcement from BFF");
        Page<AdminAnnouncementDTO> page = adminAnnouncementService.findAll(Pageable.ofSize(1));
        return ResponseUtil.wrapOrNotFound(page.stream().findFirst());
    }

    @GetMapping("/help-categories")
    public ResponseEntity<List<HelpCategoryDTO>> getHelpCategories(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get help categories from BFF");
        Page<HelpCategoryDTO> page = helpCategoryService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
