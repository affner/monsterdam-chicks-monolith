package com.monsterdam.app.web.rest.bff.nologin;

import com.monsterdam.app.service.PostFeedService;
import com.monsterdam.app.service.UserLiteService;
import com.monsterdam.app.service.dto.PostFeedDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/bff/search")
@Tag(name = "BFF Search")
public class SearchController {

    private static final Logger LOG = LoggerFactory.getLogger(SearchController.class);

    private final PostFeedService postFeedService;
    private final UserLiteService userLiteService;

    public SearchController(PostFeedService postFeedService, UserLiteService userLiteService) {
        this.postFeedService = postFeedService;
        this.userLiteService = userLiteService;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostFeedDTO>> searchPosts(
        @RequestParam(value = "q", required = false) String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search posts from BFF query={}", query);
        List<PostFeedDTO> allPosts = postFeedService.findAll(Pageable.unpaged()).getContent();
        List<PostFeedDTO> filtered = allPosts
            .stream()
            .filter(post -> Boolean.TRUE.equals(post.getIsPublic()))
            .filter(post -> matchText(post.getPostContent(), query))
            .collect(Collectors.toList());

        Page<PostFeedDTO> page = paginate(filtered, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserLiteDTO>> searchUsers(
        @RequestParam(value = "q", required = false) String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search users from BFF query={}", query);
        List<UserLiteDTO> allUsers = userLiteService.findAll(Pageable.unpaged()).getContent();
        List<UserLiteDTO> filtered = allUsers
            .stream()
            .filter(user -> matchText(user.getNickName(), query) || matchText(user.getFullName(), query))
            .collect(Collectors.toList());

        Page<UserLiteDTO> page = paginate(filtered, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    private boolean matchText(String value, String query) {
        if (!StringUtils.hasText(query)) {
            return true;
        }
        return StringUtils.hasText(value) && value.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT));
    }

    private <T> Page<T> paginate(List<T> source, Pageable pageable) {
        if (pageable.isUnpaged()) {
            return new PageImpl<>(source);
        }
        int start = (int) pageable.getOffset();
        if (start >= source.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, source.size());
        }
        int end = Math.min(start + pageable.getPageSize(), source.size());
        return new PageImpl<>(source.subList(start, end), pageable, source.size());
    }
}
