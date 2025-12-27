package com.monsterdam.app.web.rest.bff.common;

import com.monsterdam.app.repository.UserLiteRepository;
import com.monsterdam.app.service.UserService;
import com.monsterdam.app.service.dto.AdminUserDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import com.monsterdam.app.service.mapper.UserLiteMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/account")
@Tag(name = "BFF Account")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private final UserService userService;
    private final UserLiteRepository userLiteRepository;
    private final UserLiteMapper userLiteMapper;

    public AccountController(UserService userService, UserLiteRepository userLiteRepository, UserLiteMapper userLiteMapper) {
        this.userService = userService;
        this.userLiteRepository = userLiteRepository;
        this.userLiteMapper = userLiteMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<AdminUserDTO> getCurrentAccount() {
        LOG.debug("REST request to get current account details");
        return ResponseEntity.of(userService.getUserWithAuthorities().map(AdminUserDTO::new));
    }

    @GetMapping("/users")
    public ResponseEntity<java.util.List<UserLiteDTO>> listUsers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get BFF users page");
        Page<UserLiteDTO> page = userLiteRepository.findAllByDeletedDateIsNull(pageable).map(userLiteMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserLiteDTO> getUser(@PathVariable Long id) {
        LOG.debug("REST request to get BFF user {}", id);
        return ResponseUtil.wrapOrNotFound(userLiteRepository.findByIdAndDeletedDateIsNull(id).map(userLiteMapper::toDto));
    }
}
