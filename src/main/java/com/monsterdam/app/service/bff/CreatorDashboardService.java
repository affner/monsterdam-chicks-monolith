package com.monsterdam.app.service.bff;

import com.monsterdam.app.repository.MoneyEarningRepository;
import com.monsterdam.app.repository.PostFeedRepository;
import com.monsterdam.app.repository.SingleAudioRepository;
import com.monsterdam.app.repository.SinglePhotoRepository;
import com.monsterdam.app.repository.SingleVideoRepository;
import com.monsterdam.app.service.dto.bff.CreatorDashboardSummaryDTO;
import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CreatorDashboardService {

    private final PostFeedRepository postFeedRepository;
    private final SingleVideoRepository singleVideoRepository;
    private final SinglePhotoRepository singlePhotoRepository;
    private final SingleAudioRepository singleAudioRepository;
    private final MoneyEarningRepository moneyEarningRepository;

    public CreatorDashboardService(
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

    public CreatorDashboardSummaryDTO getSummary() {
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

        return new CreatorDashboardSummaryDTO(postCount, videoCount, photoCount, audioCount, totalEarnings);
    }
}
