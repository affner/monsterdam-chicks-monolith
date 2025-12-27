package com.monsterdam.app.web.rest.bff.viewer;

import com.monsterdam.app.service.PurchasedContentService;
import com.monsterdam.app.service.PurchasedSubscriptionService;
import com.monsterdam.app.service.ViewerWalletService;
import com.monsterdam.app.service.dto.PurchasedContentDTO;
import com.monsterdam.app.service.dto.PurchasedSubscriptionDTO;
import com.monsterdam.app.service.dto.ViewerWalletDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/viewer/library")
@Tag(name = "BFF Viewer Library")
public class ViewerLibraryController {

    private static final Logger LOG = LoggerFactory.getLogger(ViewerLibraryController.class);

    private final PurchasedContentService purchasedContentService;
    private final PurchasedSubscriptionService purchasedSubscriptionService;
    private final ViewerWalletService viewerWalletService;

    public ViewerLibraryController(
        PurchasedContentService purchasedContentService,
        PurchasedSubscriptionService purchasedSubscriptionService,
        ViewerWalletService viewerWalletService
    ) {
        this.purchasedContentService = purchasedContentService;
        this.purchasedSubscriptionService = purchasedSubscriptionService;
        this.viewerWalletService = viewerWalletService;
    }

    @GetMapping("/content")
    public ResponseEntity<List<PurchasedContentDTO>> getPurchasedContent(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get purchased content from BFF");
        Page<PurchasedContentDTO> page = purchasedContentService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<PurchasedSubscriptionDTO>> getPurchasedSubscriptions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get purchased subscriptions from BFF");
        Page<PurchasedSubscriptionDTO> page = purchasedSubscriptionService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/wallets")
    public ResponseEntity<List<ViewerWalletDTO>> getViewerWallets(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get viewer wallets from BFF library");
        Page<ViewerWalletDTO> page = viewerWalletService.logicalFindAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/packages/{packageId}/download")
    public ResponseEntity<Resource> downloadPackage(@PathVariable("packageId") Long packageId) {
        LOG.debug("REST request to download package {}", packageId);
        // TODO: real download with validation/payment checks.
        String filename = "package-" + packageId + ".zip";
        byte[] payload = ("Dummy package download for " + packageId).getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(payload);
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType("application/zip"))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
            .body(resource);
    }
}
