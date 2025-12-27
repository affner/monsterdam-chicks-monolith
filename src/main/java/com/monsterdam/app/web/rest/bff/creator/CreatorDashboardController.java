package com.monsterdam.app.web.rest.bff.creator;

import com.monsterdam.app.repository.MoneyEarningRepository;
import com.monsterdam.app.repository.PostFeedRepository;
import com.monsterdam.app.repository.SingleAudioRepository;
import com.monsterdam.app.repository.SinglePhotoRepository;
import com.monsterdam.app.repository.SingleVideoRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final PostFeedRepository postFeedRepository;
    private final SingleVideoRepository singleVideoRepository;
    private final SinglePhotoRepository singlePhotoRepository;
    private final SingleAudioRepository singleAudioRepository;
    private final MoneyEarningRepository moneyEarningRepository;

    public CreatorDashboardController(
        PostFeedRepository postFeedRepository,
        SingleVideoRepository singleVideoRepository,
        SinglePhotoRepository singlePhotoRepository,
        SingleAudioRepository singleAudioRepository,
        MoneyEarningRepository moneyEarningRepository
    ) {
        this.postFeedRepository = postFeedRepository;
        this.singleVideoRepository = singleVideoRepository;
        this.singlePhotoRepository = singlePhotoRepository;
        this.singleAudioRepository = singleAudioRepository;
        this.moneyEarningRepository = moneyEarningRepository;
    }

    @GetMapping("/summary")
    public ResponseEntity<CreatorDashboardSummary> getDashboardSummary() {
        LOG.debug("REST request to get creator dashboard summary from BFF");
        long postCount = postFeedRepository.findAllByDeletedDateIsNull(Pageable.unpaged()).getTotalElements();
        long videoCount = singleVideoRepository.findAllByDeletedDateIsNull(Pageable.unpaged()).getTotalElements();
        long photoCount = singlePhotoRepository.findAllByDeletedDateIsNull(Pageable.unpaged()).getTotalElements();
        long audioCount = singleAudioRepository.findAllByDeletedDateIsNull(Pageable.unpaged()).getTotalElements();

        BigDecimal totalEarnings = moneyEarningRepository
            .findAllByDeletedDateIsNull(Pageable.unpaged())
            .stream()
            .map(earning -> earning.getAmount())
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
