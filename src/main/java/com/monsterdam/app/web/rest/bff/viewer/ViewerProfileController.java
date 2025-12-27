package com.monsterdam.app.web.rest.bff.viewer;

import com.monsterdam.app.repository.UserLiteRepository;
import com.monsterdam.app.repository.UserProfileRepository;
import com.monsterdam.app.repository.UserSettingsRepository;
import com.monsterdam.app.service.dto.UserLiteDTO;
import com.monsterdam.app.service.dto.UserProfileDTO;
import com.monsterdam.app.service.dto.UserSettingsDTO;
import com.monsterdam.app.service.mapper.UserLiteMapper;
import com.monsterdam.app.service.mapper.UserProfileMapper;
import com.monsterdam.app.service.mapper.UserSettingsMapper;
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
@RequestMapping("/api/viewer/profile")
@Tag(name = "BFF Viewer Profile")
public class ViewerProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(ViewerProfileController.class);

    private final UserLiteRepository userLiteRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserSettingsRepository userSettingsRepository;
    private final UserLiteMapper userLiteMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserSettingsMapper userSettingsMapper;

    public ViewerProfileController(
        UserLiteRepository userLiteRepository,
        UserProfileRepository userProfileRepository,
        UserSettingsRepository userSettingsRepository,
        UserLiteMapper userLiteMapper,
        UserProfileMapper userProfileMapper,
        UserSettingsMapper userSettingsMapper
    ) {
        this.userLiteRepository = userLiteRepository;
        this.userProfileRepository = userProfileRepository;
        this.userSettingsRepository = userSettingsRepository;
        this.userLiteMapper = userLiteMapper;
        this.userProfileMapper = userProfileMapper;
        this.userSettingsMapper = userSettingsMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserLiteDTO>> getUserProfiles(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get user lite profiles from BFF viewer");
        Page<UserLiteDTO> page = userLiteRepository.findAllByDeletedDateIsNull(pageable).map(userLiteMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserLiteDTO> getUserProfile(@PathVariable Long id) {
        LOG.debug("REST request to get user lite profile from BFF viewer : {}", id);
        return ResponseUtil.wrapOrNotFound(userLiteRepository.findByIdAndDeletedDateIsNull(id).map(userLiteMapper::toDto));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<UserProfileDTO>> getProfiles(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get profiles from BFF viewer");
        Page<UserProfileDTO> page = userProfileRepository.findAllByDeletedDateIsNull(pageable).map(userProfileMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/profiles/{id}")
    public ResponseEntity<UserProfileDTO> getProfile(@PathVariable Long id) {
        LOG.debug("REST request to get profile from BFF viewer : {}", id);
        return ResponseUtil.wrapOrNotFound(userProfileRepository.findByIdAndDeletedDateIsNull(id).map(userProfileMapper::toDto));
    }

    @GetMapping("/settings/{id}")
    public ResponseEntity<UserSettingsDTO> getSettings(@PathVariable Long id) {
        LOG.debug("REST request to get settings from BFF viewer : {}", id);
        return ResponseUtil.wrapOrNotFound(userSettingsRepository.findByIdAndDeletedDateIsNull(id).map(userSettingsMapper::toDto));
    }
}
