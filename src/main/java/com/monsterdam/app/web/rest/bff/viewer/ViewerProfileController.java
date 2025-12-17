package com.monsterdam.app.web.rest.bff.viewer;

import com.monsterdam.app.service.UserLiteService;
import com.monsterdam.app.service.UserProfileService;
import com.monsterdam.app.service.UserSettingsService;
import com.monsterdam.app.service.dto.UserLiteDTO;
import com.monsterdam.app.service.dto.UserProfileDTO;
import com.monsterdam.app.service.dto.UserSettingsDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/bff/viewer/profile")
@Tag(name = "BFF Viewer Profile")
public class ViewerProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(ViewerProfileController.class);

    private final UserLiteService userLiteService;
    private final UserProfileService userProfileService;
    private final UserSettingsService userSettingsService;

    public ViewerProfileController(
        UserLiteService userLiteService,
        UserProfileService userProfileService,
        UserSettingsService userSettingsService
    ) {
        this.userLiteService = userLiteService;
        this.userProfileService = userProfileService;
        this.userSettingsService = userSettingsService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserLiteDTO>> getUserProfiles(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get user lite profiles from BFF viewer");
        Page<UserLiteDTO> page = userLiteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserLiteDTO> getUserProfile(@PathVariable Long id) {
        LOG.debug("REST request to get user lite profile from BFF viewer : {}", id);
        return ResponseUtil.wrapOrNotFound(userLiteService.findOne(id));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfileDTO>> getProfiles(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get profiles from BFF viewer");
        Page<UserProfileDTO> page = userProfileService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/profiles/{id}")
    public ResponseEntity<UserProfileDTO> getProfile(@PathVariable Long id) {
        LOG.debug("REST request to get profile from BFF viewer : {}", id);
        return ResponseUtil.wrapOrNotFound(userProfileService.findOne(id));
    }

    @GetMapping("/settings/{id}")
    public ResponseEntity<UserSettingsDTO> getSettings(@PathVariable Long id) {
        LOG.debug("REST request to get settings from BFF viewer : {}", id);
        return ResponseUtil.wrapOrNotFound(userSettingsService.findOne(id));
    }
}
