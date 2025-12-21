package com.monsterdam.app.service.bff;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.domain.SingleAudio;
import com.monsterdam.app.domain.SinglePhoto;
import com.monsterdam.app.domain.SingleVideo;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.UserProfile;
import com.monsterdam.app.repository.ContentPackageRepository;
import com.monsterdam.app.repository.PostFeedRepository;
import com.monsterdam.app.repository.PurchasedContentRepository;
import com.monsterdam.app.repository.SingleAudioRepository;
import com.monsterdam.app.repository.SinglePhotoRepository;
import com.monsterdam.app.repository.SingleVideoRepository;
import com.monsterdam.app.repository.UserLiteRepository;
import com.monsterdam.app.repository.UserProfileRepository;
import com.monsterdam.app.security.AuthoritiesConstants;
import com.monsterdam.app.security.SecurityUtils;
import com.monsterdam.app.service.dto.bff.ContentSetDTO;
import com.monsterdam.app.service.dto.bff.CreatorProfileDTO;
import com.monsterdam.app.service.dto.bff.MediaItemDTO;
import com.monsterdam.app.service.dto.bff.MenuDTO;
import com.monsterdam.app.service.dto.bff.SetDetailDTO;
import com.monsterdam.app.service.dto.bff.SingleSetDTO;
import com.monsterdam.app.service.mapper.bff.ViewerBffMapper;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ViewerBffService {

    public enum SortOption {
        RATING,
        RANDOM,
        RECENT,
    }

    public enum TypeFilter {
        FREE,
        PAID,
        ALL,
    }

    private static final int DESCRIPTION_PREVIEW_LENGTH = 140;
    private static final Logger LOG = LoggerFactory.getLogger(ViewerBffService.class);

    private final ContentPackageRepository contentPackageRepository;
    private final PostFeedRepository postFeedRepository;
    private final SinglePhotoRepository singlePhotoRepository;
    private final SingleVideoRepository singleVideoRepository;
    private final SingleAudioRepository singleAudioRepository;
    private final PurchasedContentRepository purchasedContentRepository;
    private final UserLiteRepository userLiteRepository;
    private final UserProfileRepository userProfileRepository;
    private final ViewerBffMapper viewerBffMapper;

    public ViewerBffService(
        ContentPackageRepository contentPackageRepository,
        PostFeedRepository postFeedRepository,
        SinglePhotoRepository singlePhotoRepository,
        SingleVideoRepository singleVideoRepository,
        SingleAudioRepository singleAudioRepository,
        PurchasedContentRepository purchasedContentRepository,
        UserLiteRepository userLiteRepository,
        UserProfileRepository userProfileRepository,
        ViewerBffMapper viewerBffMapper
    ) {
        this.contentPackageRepository = contentPackageRepository;
        this.postFeedRepository = postFeedRepository;
        this.singlePhotoRepository = singlePhotoRepository;
        this.singleVideoRepository = singleVideoRepository;
        this.singleAudioRepository = singleAudioRepository;
        this.purchasedContentRepository = purchasedContentRepository;
        this.userLiteRepository = userLiteRepository;
        this.userProfileRepository = userProfileRepository;
        this.viewerBffMapper = viewerBffMapper;
    }

    @Transactional(readOnly = true)
    public MenuDTO buildMenu() {
        UserContext context = resolveUserContext();
        List<String> sections = new ArrayList<>();
        sections.add("home");
        sections.add("discover");
        if (context.roles().contains(AuthoritiesConstants.VIEWER)) {
            sections.add("library");
            sections.add("wallet");
        }
        if (context.roles().contains(AuthoritiesConstants.CREATOR)) {
            sections.add("creator-dashboard");
            sections.add("creator-content");
        }
        if (context.roles().contains(AuthoritiesConstants.ADMIN)) {
            sections.add("admin");
        }
        return viewerBffMapper.toMenu(context.roles(), sections);
    }

    @Transactional(readOnly = true)
    public Page<SingleSetDTO> listSets(String sort, String type, Pageable pageable) {
        UserContext context = resolveUserContext();
        SortOption sortOption = parseSort(sort);
        TypeFilter typeFilter = parseType(type);

        List<ContentPackage> activePackages = contentPackageRepository.findAllByDeletedDateIsNull(Pageable.unpaged()).getContent();
        List<SingleSetDTO> mapped = activePackages
            .stream()
            .filter(pkg -> matchesType(pkg, typeFilter))
            .map(pkg -> toSingleSet(pkg, context))
            .flatMap(Optional::stream)
            .collect(Collectors.toList());

        sortSets(mapped, sortOption);
        Page<SingleSetDTO> page = paginate(mapped, pageable);
        LOG.debug("Viewer set list prepared with {} elements (filtered from {})", page.getContent().size(), activePackages.size());
        return page;
    }

    @Transactional(readOnly = true)
    public ContentSetDTO getContentSet(Long id) {
        UserContext context = resolveUserContext();
        ContentPackage contentPackage = contentPackageRepository
            .findByIdAndDeletedDateIsNull(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Set not found"));

        Optional<PostFeed> postOptional = loadVisiblePost(contentPackage, context);
        if (context.anonymous() && postOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Set not available for anonymous users");
        }
        PostFeed post = postOptional.orElse(null);
        boolean unlocked = isUnlockedForCurrentUser(contentPackage.getId());
        if (!unlocked && Boolean.FALSE.equals(contentPackage.getIsPaidContent())) {
            unlocked = true;
        }
        boolean canUnlock = Boolean.TRUE.equals(contentPackage.getIsPaidContent()) && !unlocked;
        List<MediaItemDTO> items = buildMediaItems(contentPackage.getId(), unlocked);
        String downloadUrl = unlocked ? "/api/viewer/set/content/" + id + "/download" : null;

        return viewerBffMapper.toContentSet(
            contentPackage,
            buildTitle(contentPackage, post),
            buildDescription(post),
            unlocked,
            canUnlock,
            downloadUrl,
            items,
            resolveCurrency()
        );
    }

    @Transactional(readOnly = true)
    public SetDetailDTO getSetDetail(Long id) {
        ContentSetDTO contentSet = getContentSet(id);
        SetDetailDTO detail = new SetDetailDTO();
        detail.setId(contentSet.getId());
        detail.setTitle(contentSet.getTitle());
        detail.setDescription(contentSet.getDescription());
        detail.setUnlocked(contentSet.isUnlocked());
        detail.setCanUnlock(contentSet.isCanUnlock());
        detail.setPrice(contentSet.getPrice());
        detail.setCurrency(contentSet.getCurrency());
        detail.setDownloadUrl(contentSet.getDownloadUrl());
        detail.setItems(contentSet.getItems());
        return detail;
    }

    @Transactional(readOnly = true)
    public CreatorProfileDTO getCreatorProfile(Long creatorId, String type, Pageable pageable) {
        UserLite creator = userLiteRepository
            .findByIdAndDeletedDateIsNull(creatorId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Creator not found"));
        List<SingleSetDTO> creatorSets = listSets("RECENT", type, Pageable.unpaged())
            .stream()
            .filter(dto -> creatorId.equals(dto.getCreatorId()))
            .collect(Collectors.toList());
        Page<SingleSetDTO> paged = paginate(creatorSets, pageable);
        UserProfile profile = resolveProfile(creator);
        List<String> links = collectLinks(profile);
        return viewerBffMapper.toCreatorProfile(creator, profile, links, paged.getContent());
    }

    private Optional<SingleSetDTO> toSingleSet(ContentPackage contentPackage, UserContext context) {
        Optional<PostFeed> post = loadVisiblePost(contentPackage, context);
        if (context.anonymous() && post.isEmpty()) {
            return Optional.empty();
        }
        boolean unlocked = isUnlockedForCurrentUser(contentPackage.getId());
        if (!unlocked && Boolean.FALSE.equals(contentPackage.getIsPaidContent())) {
            unlocked = true;
        }
        boolean canUnlock = Boolean.TRUE.equals(contentPackage.getIsPaidContent()) && !unlocked;
        UserLite creator = resolveCreator(post.orElse(null), contentPackage.getId());
        Double rating = purchasedContentRepository.findAverageRatingByContentPackageId(contentPackage.getId()).orElse(null);
        String coverUrl = resolveCoverUrl(contentPackage.getId());

        SingleSetDTO dto = viewerBffMapper.toSingleSet(
            contentPackage,
            buildTitle(contentPackage, post.orElse(null)),
            shorten(buildDescription(post.orElse(null)), DESCRIPTION_PREVIEW_LENGTH),
            creator,
            rating,
            coverUrl,
            unlocked,
            canUnlock,
            resolveCurrency()
        );
        return Optional.of(dto);
    }

    private List<MediaItemDTO> buildMediaItems(Long contentPackageId, boolean unlocked) {
        List<MediaItemDTO> items = new ArrayList<>();
        List<SinglePhoto> photos = singlePhotoRepository.findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId);
        List<SingleVideo> videos = singleVideoRepository.findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId);
        List<SingleAudio> audios = singleAudioRepository.findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId);

        photos
            .stream()
            .filter(photo -> unlocked || Boolean.TRUE.equals(photo.getIsPreview()))
            .forEach(photo -> {
                MediaItemDTO dto = new MediaItemDTO();
                dto.setId(photo.getId());
                dto.setType(MediaItemDTO.MediaType.PHOTO);
                dto.setThumbnailUrl(photo.getThumbnailS3Key());
                dto.setContentUrl(unlocked ? photo.getContentS3Key() : null);
                dto.setPreview(Boolean.TRUE.equals(photo.getIsPreview()));
                dto.setDurationMinutes(null);
                items.add(dto);
            });

        videos
            .stream()
            .filter(video -> unlocked || Boolean.TRUE.equals(video.getIsPreview()))
            .forEach(video -> {
                MediaItemDTO dto = new MediaItemDTO();
                dto.setId(video.getId());
                dto.setType(MediaItemDTO.MediaType.VIDEO);
                dto.setThumbnailUrl(video.getThumbnailS3Key());
                dto.setContentUrl(unlocked ? video.getContentS3Key() : null);
                dto.setPreview(Boolean.TRUE.equals(video.getIsPreview()));
                dto.setDurationMinutes(toMinutes(video.getDuration()));
                items.add(dto);
            });

        audios
            .stream()
            .filter(audio -> unlocked || Boolean.TRUE.equals(audio.getIsPreview()))
            .forEach(audio -> {
                MediaItemDTO dto = new MediaItemDTO();
                dto.setId(audio.getId());
                dto.setType(MediaItemDTO.MediaType.AUDIO);
                dto.setThumbnailUrl(audio.getThumbnailS3Key());
                dto.setContentUrl(unlocked ? audio.getContentS3Key() : null);
                dto.setPreview(Boolean.TRUE.equals(audio.getIsPreview()));
                dto.setDurationMinutes(toMinutes(audio.getDuration()));
                items.add(dto);
            });

        return items;
    }

    private String resolveCoverUrl(Long contentPackageId) {
        return singlePhotoRepository
            .findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId)
            .stream()
            .filter(photo -> Boolean.TRUE.equals(photo.getIsPreview()))
            .map(SinglePhoto::getThumbnailS3Key)
            .findFirst()
            .orElseGet(() ->
                singleVideoRepository
                    .findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId)
                    .stream()
                    .map(SingleVideo::getThumbnailS3Key)
                    .findFirst()
                    .orElseGet(() ->
                        singleAudioRepository
                            .findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId)
                            .stream()
                            .map(SingleAudio::getThumbnailS3Key)
                            .findFirst()
                            .orElse(null)
                    )
            );
    }

    private UserLite resolveCreator(PostFeed post, Long contentPackageId) {
        if (post != null && post.getCreator() != null && post.getCreator().getDeletedDate() == null) {
            return post.getCreator();
        }
        return singlePhotoRepository
            .findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId)
            .stream()
            .map(SinglePhoto::getCreator)
            .filter(creator -> creator != null && creator.getDeletedDate() == null)
            .findFirst()
            .orElseGet(() ->
                singleVideoRepository
                    .findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId)
                    .stream()
                    .map(SingleVideo::getCreator)
                    .filter(creator -> creator != null && creator.getDeletedDate() == null)
                    .findFirst()
                    .orElseGet(() ->
                        singleAudioRepository
                            .findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId)
                            .stream()
                            .map(SingleAudio::getCreator)
                            .filter(creator -> creator != null && creator.getDeletedDate() == null)
                            .findFirst()
                            .orElse(null)
                    )
            );
    }

    private Optional<PostFeed> loadVisiblePost(ContentPackage contentPackage, UserContext context) {
        if (contentPackage.getPostId() == null) {
            return Optional.empty();
        }
        Optional<PostFeed> post = postFeedRepository.findByIdAndDeletedDateIsNull(contentPackage.getPostId());
        if (post.isEmpty()) {
            return Optional.empty();
        }
        if (context.anonymous() && !Boolean.TRUE.equals(post.orElseThrow(IllegalStateException::new).getIsPublic())) {
            return Optional.empty();
        }
        return post;
    }

    private boolean matchesType(ContentPackage contentPackage, TypeFilter filter) {
        return switch (filter) {
            case FREE -> Boolean.FALSE.equals(contentPackage.getIsPaidContent());
            case PAID -> Boolean.TRUE.equals(contentPackage.getIsPaidContent());
            default -> true;
        };
    }

    private void sortSets(List<SingleSetDTO> sets, SortOption sortOption) {
        Comparator<SingleSetDTO> comparator =
            switch (sortOption) {
                case RATING -> Comparator.comparing(SingleSetDTO::getRating, Comparator.nullsLast(Comparator.reverseOrder()));
                case RANDOM -> null;
                default -> Comparator.comparing(SingleSetDTO::getCreatedDate, Comparator.nullsLast(Comparator.reverseOrder()));
            };
        if (sortOption == SortOption.RANDOM) {
            Collections.shuffle(sets);
        } else if (comparator != null) {
            sets.sort(comparator);
        }
    }

    private SortOption parseSort(String sort) {
        if (!StringUtils.hasText(sort)) {
            return SortOption.RECENT;
        }
        try {
            return SortOption.valueOf(sort.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return SortOption.RECENT;
        }
    }

    private TypeFilter parseType(String type) {
        if (!StringUtils.hasText(type)) {
            return TypeFilter.ALL;
        }
        try {
            return TypeFilter.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            return TypeFilter.ALL;
        }
    }

    private UserContext resolveUserContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = authentication != null
            ? authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
            : new ArrayList<>();
        boolean anonymous = authentication == null || roles.contains(AuthoritiesConstants.ANONYMOUS);
        if (roles.isEmpty()) {
            roles = List.of(AuthoritiesConstants.ANONYMOUS);
        }
        Optional<Long> userId = SecurityUtils.getCurrentUserId();
        return new UserContext(userId.orElse(null), roles, anonymous);
    }

    private boolean isUnlockedForCurrentUser(Long contentPackageId) {
        Optional<Long> currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId.isEmpty()) {
            return false;
        }
        return purchasedContentRepository.existsByContentPackage_IdAndViewer_IdAndDeletedDateIsNull(
            contentPackageId,
            currentUserId.orElseThrow(IllegalStateException::new)
        );
    }

    private String buildTitle(ContentPackage contentPackage, PostFeed post) {
        if (post != null && StringUtils.hasText(post.getPostContent())) {
            String content = post.getPostContent().trim();
            return shorten(content, 60);
        }
        return "Set #" + contentPackage.getId();
    }

    private String buildDescription(PostFeed post) {
        if (post != null && StringUtils.hasText(post.getPostContent())) {
            return post.getPostContent();
        }
        return null;
    }

    private String shorten(String text, int maxLength) {
        if (!StringUtils.hasText(text)) {
            return null;
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }

    private Page<SingleSetDTO> paginate(List<SingleSetDTO> source, Pageable pageable) {
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

    private Long toMinutes(Duration duration) {
        return duration != null ? duration.toMinutes() : null;
    }

    private List<String> collectLinks(UserProfile profile) {
        if (profile == null) {
            return List.of();
        }
        List<String> links = new ArrayList<>();
        if (StringUtils.hasText(profile.getWebsiteUrl())) {
            links.add(profile.getWebsiteUrl());
        }
        if (StringUtils.hasText(profile.getAmazonWishlistUrl())) {
            links.add(profile.getAmazonWishlistUrl());
        }
        if (StringUtils.hasText(profile.getMainContentUrl())) {
            links.add(profile.getMainContentUrl());
        }
        return links;
    }

    private UserProfile resolveProfile(UserLite creator) {
        if (creator.getProfile() == null) {
            return null;
        }
        return userProfileRepository.findByIdAndDeletedDateIsNull(creator.getProfile().getId()).orElse(null);
    }

    private String resolveCurrency() {
        return "USD";
    }

    private record UserContext(Long userId, List<String> roles, boolean anonymous) {}
}
