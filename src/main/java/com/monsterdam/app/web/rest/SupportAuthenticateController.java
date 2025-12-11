package com.monsterdam.app.web.rest;

import com.monsterdam.app.repository.SupportUserRepository;
import com.monsterdam.app.security.AuthenticationTokenService;
import com.monsterdam.app.security.SecurityUtils;
import com.monsterdam.app.web.rest.vm.JWTToken;
import com.monsterdam.app.web.rest.vm.LoginVM;
import jakarta.validation.Valid;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to authenticate support users using a dedicated entry point.
 */
@RestController
@RequestMapping("/api/support")
public class SupportAuthenticateController {

    private static final Logger LOG = LoggerFactory.getLogger(SupportAuthenticateController.class);

    private final AuthenticationTokenService authenticationTokenService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final SupportUserRepository supportUserRepository;

    public SupportAuthenticateController(
        AuthenticationTokenService authenticationTokenService,
        AuthenticationManagerBuilder authenticationManagerBuilder,
        SupportUserRepository supportUserRepository
    ) {
        this.authenticationTokenService = authenticationTokenService;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.supportUserRepository = supportUserRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long userId = SecurityUtils.getCurrentUserId().orElse(null);
        if (userId == null || supportUserRepository.findOneByUserId(userId).isEmpty()) {
            SecurityContextHolder.clearContext();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwt = authenticationTokenService.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * {@code GET /support/authenticate} : check if the current user is authenticated as support.
     */
    @GetMapping("/authenticate")
    public ResponseEntity<Void> isAuthenticated(Principal principal) {
        LOG.debug("REST request to check if the current support user is authenticated");
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean supportUserExists =
            supportUserRepository.findOneByUser_LoginIgnoreCase(principal.getName()).isPresent() ||
            supportUserRepository.findOneByUser_EmailIgnoreCase(principal.getName()).isPresent();
        return ResponseEntity.status(supportUserExists ? HttpStatus.NO_CONTENT : HttpStatus.UNAUTHORIZED).build();
    }
}
