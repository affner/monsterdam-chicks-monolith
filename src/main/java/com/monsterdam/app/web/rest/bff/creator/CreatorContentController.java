package com.monsterdam.app.web.rest.bff.creator;

import com.monsterdam.app.repository.ContentPackageRepository;
import com.monsterdam.app.repository.PostFeedRepository;
import com.monsterdam.app.repository.SingleAudioRepository;
import com.monsterdam.app.repository.SinglePhotoRepository;
import com.monsterdam.app.repository.SingleVideoRepository;
import com.monsterdam.app.service.ContentPackageService;
import com.monsterdam.app.service.PostFeedService;
import com.monsterdam.app.service.dto.ContentPackageDTO;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.dto.SingleAudioDTO;
import com.monsterdam.app.service.dto.SinglePhotoDTO;
import com.monsterdam.app.service.dto.SingleVideoDTO;
import com.monsterdam.app.service.mapper.ContentPackageMapper;
import com.monsterdam.app.service.mapper.PostFeedMapper;
import com.monsterdam.app.service.mapper.SingleAudioMapper;
import com.monsterdam.app.service.mapper.SinglePhotoMapper;
import com.monsterdam.app.service.mapper.SingleVideoMapper;
import com.monsterdam.app.web.rest.errors.BadRequestAlertException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/creator/content")
@Tag(name = "BFF Creator Content")
public class CreatorContentController {

    private static final Logger LOG = LoggerFactory.getLogger(CreatorContentController.class);
    private static final String POST_ENTITY_NAME = "postFeed";
    private static final String PACKAGE_ENTITY_NAME = "contentPackage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostFeedService postFeedService;
    private final ContentPackageService contentPackageService;
    private final PostFeedRepository postFeedRepository;
    private final ContentPackageRepository contentPackageRepository;
    private final SingleVideoRepository singleVideoRepository;
    private final SinglePhotoRepository singlePhotoRepository;
    private final SingleAudioRepository singleAudioRepository;
    private final PostFeedMapper postFeedMapper;
    private final SingleVideoMapper singleVideoMapper;
    private final SinglePhotoMapper singlePhotoMapper;
    private final SingleAudioMapper singleAudioMapper;
    private final ContentPackageMapper contentPackageMapper;

    public CreatorContentController(
        PostFeedService postFeedService,
        ContentPackageService contentPackageService,
        PostFeedRepository postFeedRepository,
        ContentPackageRepository contentPackageRepository,
        SingleVideoRepository singleVideoRepository,
        SinglePhotoRepository singlePhotoRepository,
        SingleAudioRepository singleAudioRepository,
        PostFeedMapper postFeedMapper,
        SingleVideoMapper singleVideoMapper,
        SinglePhotoMapper singlePhotoMapper,
        SingleAudioMapper singleAudioMapper,
        ContentPackageMapper contentPackageMapper
    ) {
        this.postFeedService = postFeedService;
        this.contentPackageService = contentPackageService;
        this.postFeedRepository = postFeedRepository;
        this.contentPackageRepository = contentPackageRepository;
        this.singleVideoRepository = singleVideoRepository;
        this.singlePhotoRepository = singlePhotoRepository;
        this.singleAudioRepository = singleAudioRepository;
        this.postFeedMapper = postFeedMapper;
        this.singleVideoMapper = singleVideoMapper;
        this.singlePhotoMapper = singlePhotoMapper;
        this.singleAudioMapper = singleAudioMapper;
        this.contentPackageMapper = contentPackageMapper;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostFeedDTO>> getCreatorPosts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get creator posts from BFF");
        Page<PostFeedDTO> page = postFeedRepository.findAllByDeletedDateIsNull(pageable).map(postFeedMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/posts")
    public ResponseEntity<PostFeedDTO> createCreatorPost(@Valid @RequestBody PostFeedDTO postFeedDTO) throws URISyntaxException {
        LOG.debug("REST request to create PostFeed from BFF : {}", postFeedDTO);
        if (postFeedDTO.getId() != null) {
            throw new BadRequestAlertException("A new postFeed cannot already have an ID", POST_ENTITY_NAME, "idexists");
        }
        PostFeedDTO result = postFeedService.save(postFeedDTO);
        return ResponseEntity.created(new URI("/api/bff/creator/content/posts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, POST_ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<PostFeedDTO> updateCreatorPost(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PostFeedDTO postFeedDTO
    ) {
        LOG.debug("REST request to update PostFeed from BFF : {}, {}", id, postFeedDTO);
        if (postFeedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", POST_ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postFeedDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", POST_ENTITY_NAME, "idinvalid");
        }
        if (!postFeedRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", POST_ENTITY_NAME, "idnotfound");
        }
        PostFeedDTO result = postFeedService.update(postFeedDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, POST_ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/videos")
    public ResponseEntity<List<SingleVideoDTO>> getCreatorVideos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get creator videos from BFF");
        Page<SingleVideoDTO> page = singleVideoRepository.findAllByDeletedDateIsNull(pageable).map(singleVideoMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/photos")
    public ResponseEntity<List<SinglePhotoDTO>> getCreatorPhotos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get creator photos from BFF");
        Page<SinglePhotoDTO> page = singlePhotoRepository.findAllByDeletedDateIsNull(pageable).map(singlePhotoMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/audios")
    public ResponseEntity<List<SingleAudioDTO>> getCreatorAudios(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get creator audios from BFF");
        Page<SingleAudioDTO> page = singleAudioRepository.findAllByDeletedDateIsNull(pageable).map(singleAudioMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/packages")
    public ResponseEntity<List<ContentPackageDTO>> getCreatorPackages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get creator content packages from BFF");
        Page<ContentPackageDTO> page = contentPackageRepository.findAllByDeletedDateIsNull(pageable).map(contentPackageMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/packages")
    public ResponseEntity<ContentPackageDTO> createCreatorPackage(@Valid @RequestBody ContentPackageDTO contentPackageDTO)
        throws URISyntaxException {
        LOG.debug("REST request to create ContentPackage from BFF : {}", contentPackageDTO);
        if (contentPackageDTO.getId() != null) {
            throw new BadRequestAlertException("A new contentPackage cannot already have an ID", PACKAGE_ENTITY_NAME, "idexists");
        }
        ContentPackageDTO result = contentPackageService.save(contentPackageDTO);
        return ResponseEntity.created(new URI("/api/bff/creator/content/packages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, PACKAGE_ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/packages/{id}")
    public ResponseEntity<ContentPackageDTO> updateCreatorPackage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContentPackageDTO contentPackageDTO
    ) {
        LOG.debug("REST request to update ContentPackage from BFF : {}, {}", id, contentPackageDTO);
        if (contentPackageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", PACKAGE_ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contentPackageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", PACKAGE_ENTITY_NAME, "idinvalid");
        }
        if (!contentPackageRepository.existsByIdAndDeletedDateIsNull(id)) {
            throw new BadRequestAlertException("Entity not found", PACKAGE_ENTITY_NAME, "idnotfound");
        }
        ContentPackageDTO result = contentPackageService.update(contentPackageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, PACKAGE_ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/packages/{id}")
    public ResponseEntity<ContentPackageDTO> getCreatorPackage(@PathVariable Long id) {
        LOG.debug("REST request to get ContentPackage from BFF : {}", id);
        return ResponseUtil.wrapOrNotFound(contentPackageRepository.findByIdAndDeletedDateIsNull(id).map(contentPackageMapper::toDto));
    }
}
