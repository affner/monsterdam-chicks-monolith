package com.monsterdam.app.web.rest.bff.creator;

import com.monsterdam.app.service.MoneyEarningService;
import com.monsterdam.app.service.PostFeedService;
import com.monsterdam.app.service.SingleAudioService;
import com.monsterdam.app.service.SinglePhotoService;
import com.monsterdam.app.service.SingleVideoService;
import com.monsterdam.app.service.dto.MoneyEarningDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/creator/dashboard")
@Tag(name = "BFF Creator Dashboard")
public class CreatorDashboardController {

    private static final Logger LOG = LoggerFactory.getLogger(CreatorDashboardController.class);

    private final PostFeedService postFeedService;
    private final SingleVideoService singleVideoService;
    private final SinglePhotoService singlePhotoService;
    private final SingleAudioService singleAudioService;
    private final MoneyEarningService moneyEarningService;

    public CreatorDashboardController(
        PostFeedService postFeedService,
        SingleVideoService singleVideoService,
        SinglePhotoService singlePhotoService,
        SingleAudioService singleAudioService,
        MoneyEarningService moneyEarningService
    ) {
        this.postFeedService = postFeedService;
        this.singleVideoService = singleVideoService;
        this.singlePhotoService = singlePhotoService;
        this.singleAudioService = singleAudioService;
        this.moneyEarningService = moneyEarningService;
    }

    @GetMapping("/summary")
    public ResponseEntity<CreatorDashboardSummary> getDashboardSummary() {
        LOG.debug("REST request to get creator dashboard summary from BFF");
        long postCount = postFeedService.findAll(Pageable.unpaged()).getTotalElements();
        long videoCount = singleVideoService.findAll(Pageable.unpaged()).getTotalElements();
        long photoCount = singlePhotoService.findAll(Pageable.unpaged()).getTotalElements();
        long audioCount = singleAudioService.findAll(Pageable.unpaged()).getTotalElements();

        Page<MoneyEarningDTO> earningsPage = moneyEarningService.findAll(Pageable.unpaged());
        BigDecimal totalEarnings = earningsPage
            .getContent()
            .stream()
            .map(MoneyEarningDTO::getAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        CreatorDashboardSummary summary = new CreatorDashboardSummary(postCount, videoCount, photoCount, audioCount, totalEarnings);
        return ResponseEntity.ok(summary);
    }

    @Schema(description = "Simple summary for creator dashboard BFF endpoints")
    public static class CreatorDashboardSummary {

        private final long posts;
        private final long videos;
        private final long photos;
        private final long audios;
        private final BigDecimal totalEarnings;

        public CreatorDashboardSummary(long posts, long videos, long photos, long audios, BigDecimal totalEarnings) {
            this.posts = posts;
            this.videos = videos;
            this.photos = photos;
            this.audios = audios;
            this.totalEarnings = totalEarnings;
        }

        public long getPosts() {
            return posts;
        }

        public long getVideos() {
            return videos;
        }

        public long getPhotos() {
            return photos;
        }

        public long getAudios() {
            return audios;
        }

        public BigDecimal getTotalEarnings() {
            return totalEarnings;
        }
    }
}
