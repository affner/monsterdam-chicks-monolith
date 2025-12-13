package com.monsterdam.app.web.rest.browse;

import com.monsterdam.app.service.ContentPackageService;
import com.monsterdam.app.service.PostFeedService;
import com.monsterdam.app.service.dto.ContentPackageDTO;
import com.monsterdam.app.service.dto.PostFeedDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/public/browse")
public class LandingController {

    private static final Logger LOG = LoggerFactory.getLogger(LandingController.class);

    private final PostFeedService postFeedService;
    private final ContentPackageService contentPackageService;

    public LandingController(PostFeedService postFeedService, ContentPackageService contentPackageService) {
        this.postFeedService = postFeedService;
        this.contentPackageService = contentPackageService;
    }

    @GetMapping("/landing/posts")
    public ResponseEntity<List<PostFeedDTO>> loadLandingPosts(@ParameterObject Pageable pageable) {
        LOG.debug("REST request to fetch landing posts");
        Page<PostFeedDTO> page = postFeedService.findPublicFeed(applyDefaultPostSort(pageable));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/landing/packages")
    public ResponseEntity<List<ContentPackageDTO>> loadLandingPackages(@ParameterObject Pageable pageable) {
        LOG.debug("REST request to fetch landing packages");
        Page<ContentPackageDTO> page = contentPackageService.findActive(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    private Pageable applyDefaultPostSort(Pageable pageable) {
        if (pageable.getSort().isSorted()) {
            return pageable;
        }
        Sort sort = Sort.by(Sort.Order.desc("pinnedPost"), Sort.Order.desc("createdDate"));
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
