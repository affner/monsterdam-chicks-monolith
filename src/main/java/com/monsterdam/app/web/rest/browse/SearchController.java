package com.monsterdam.app.web.rest.browse;

import com.monsterdam.app.service.PostFeedService;
import com.monsterdam.app.service.dto.PostFeedDTO;
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
public class SearchController {

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    private final PostFeedService postFeedService;

    public SearchController(PostFeedService postFeedService) {
        this.postFeedService = postFeedService;
    }

    @GetMapping("/search/posts")
    public ResponseEntity<List<PostFeedDTO>> searchPosts(
        @RequestParam(name = "q", required = false, defaultValue = "") String query,
        @ParameterObject Pageable pageable
    ) {
        String safeQuery = query == null ? "" : query.trim();
        LOG.debug("REST request to search public posts with query: {}", safeQuery);
        Page<PostFeedDTO> page = safeQuery.isBlank()
            ? postFeedService.findPublicFeed(pageable)
            : postFeedService.searchPublicFeed(safeQuery, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
