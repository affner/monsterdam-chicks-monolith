package com.monsterdam.app.web.rest.browse;

import com.monsterdam.app.service.ContentPackageService;
import com.monsterdam.app.service.dto.ContentPackageDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/public/browse")
public class CatalogController {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogController.class);

    private final ContentPackageService contentPackageService;

    public CatalogController(ContentPackageService contentPackageService) {
        this.contentPackageService = contentPackageService;
    }

    @GetMapping("/catalog/packages")
    public ResponseEntity<List<ContentPackageDTO>> browsePackages(
        @ParameterObject Pageable pageable,
        @RequestParam(value = "paid", required = false) Boolean paid
    ) {
        LOG.debug("REST request to browse content packages. Paid filter: {}", paid);
        Page<ContentPackageDTO> page = paid == null
            ? contentPackageService.findActive(pageable)
            : contentPackageService.findActiveByPaymentType(paid, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
