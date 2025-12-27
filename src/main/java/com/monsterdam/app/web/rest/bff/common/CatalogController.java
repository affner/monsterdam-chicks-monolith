package com.monsterdam.app.web.rest.bff.common;

import com.monsterdam.app.repository.ContentPackageRepository;
import com.monsterdam.app.repository.PostFeedRepository;
import com.monsterdam.app.service.dto.ContentPackageDTO;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.mapper.ContentPackageMapper;
import com.monsterdam.app.service.mapper.PostFeedMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/catalog")
@Tag(name = "BFF Catalog")
public class CatalogController {

    private static final Logger LOG = LoggerFactory.getLogger(CatalogController.class);

    private final PostFeedRepository postFeedRepository;
    private final ContentPackageRepository contentPackageRepository;
    private final PostFeedMapper postFeedMapper;
    private final ContentPackageMapper contentPackageMapper;

    public CatalogController(
        PostFeedRepository postFeedRepository,
        ContentPackageRepository contentPackageRepository,
        PostFeedMapper postFeedMapper,
        ContentPackageMapper contentPackageMapper
    ) {
        this.postFeedRepository = postFeedRepository;
        this.contentPackageRepository = contentPackageRepository;
        this.postFeedMapper = postFeedMapper;
        this.contentPackageMapper = contentPackageMapper;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostFeedDTO>> getPublicPosts(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(value = "publicOnly", defaultValue = "true") boolean publicOnly
    ) {
        LOG.debug("REST request to get catalog posts with publicOnly={}", publicOnly);
        Page<PostFeedDTO> page = postFeedRepository.findAllByDeletedDateIsNull(pageable).map(postFeedMapper::toDto);
        List<PostFeedDTO> visiblePosts = publicOnly
            ? page.getContent().stream().filter(post -> Boolean.TRUE.equals(post.getIsPublic())).collect(Collectors.toList())
            : page.getContent();
        Page<PostFeedDTO> resultPage = publicOnly ? new PageImpl<>(visiblePosts, pageable, visiblePosts.size()) : page;
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), resultPage);
        return ResponseEntity.ok().headers(headers).body(resultPage.getContent());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostFeedDTO> getPublicPost(@PathVariable Long id) {
        LOG.debug("REST request to get catalog post {}", id);
        return ResponseUtil.wrapOrNotFound(
            postFeedRepository
                .findByIdAndDeletedDateIsNull(id)
                .filter(post -> post.getIsPublic() == null || Boolean.TRUE.equals(post.getIsPublic()))
                .map(postFeedMapper::toDto)
        );
    }

    @GetMapping("/packages")
    public ResponseEntity<List<ContentPackageDTO>> getContentPackages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get catalog content packages");
        Page<ContentPackageDTO> page = contentPackageRepository.findAllByDeletedDateIsNull(pageable).map(contentPackageMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/packages/{id}")
    public ResponseEntity<ContentPackageDTO> getContentPackage(@PathVariable Long id) {
        LOG.debug("REST request to get catalog content package {}", id);
        return ResponseUtil.wrapOrNotFound(contentPackageRepository.findByIdAndDeletedDateIsNull(id).map(contentPackageMapper::toDto));
    }
}
