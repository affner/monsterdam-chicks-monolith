package com.monsterdam.app.service.bff;

import com.monsterdam.app.domain.ContentPackage;
import com.monsterdam.app.domain.PostFeed;
import com.monsterdam.app.domain.SingleAudio;
import com.monsterdam.app.domain.SinglePhoto;
import com.monsterdam.app.domain.SingleVideo;
import com.monsterdam.app.domain.User;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.domain.UserProfile;
import com.monsterdam.app.repository.ContentPackageRepository;
import com.monsterdam.app.repository.PostFeedRepository;
import com.monsterdam.app.repository.SingleAudioRepository;
import com.monsterdam.app.repository.SinglePhotoRepository;
import com.monsterdam.app.repository.SingleVideoRepository;
import com.monsterdam.app.repository.UserLiteRepository;
import com.monsterdam.app.repository.UserProfileRepository;
import com.monsterdam.app.security.AuthoritiesConstants;
import com.monsterdam.app.service.dto.bff.ModelDto;
import com.monsterdam.app.service.dto.bff.ModelProfileDto;
import com.monsterdam.app.service.dto.bff.ModelSetPreviewDto;
import com.monsterdam.app.service.dto.bff.PackageDto;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service class implementing the browse logic for models and packages.  It
 * hides the details of the persistence layer and maps domain entities to
 * lightweight DTOs.  More advanced filtering (e.g. by style) can be
 * implemented here using Specifications or QueryDSL.
 */
@Service
public class ModelBrowseService {

    private final UserLiteRepository userLiteRepository;
    private final ContentPackageRepository contentPackageRepository;
    private final MediaTokenService mediaTokenService;
    private final PostFeedRepository postFeedRepository;
    private final SinglePhotoRepository singlePhotoRepository;
    private final SingleVideoRepository singleVideoRepository;
    private final SingleAudioRepository singleAudioRepository;
    private final UserProfileRepository userProfileRepository;

    public ModelBrowseService(
        UserLiteRepository userLiteRepository,
        ContentPackageRepository contentPackageRepository,
        MediaTokenService mediaTokenService,
        PostFeedRepository postFeedRepository,
        SinglePhotoRepository singlePhotoRepository,
        SingleVideoRepository singleVideoRepository,
        SingleAudioRepository singleAudioRepository,
        UserProfileRepository userProfileRepository
    ) {
        this.userLiteRepository = userLiteRepository;
        this.contentPackageRepository = contentPackageRepository;
        this.mediaTokenService = mediaTokenService;
        this.postFeedRepository = postFeedRepository;
        this.singlePhotoRepository = singlePhotoRepository;
        this.singleVideoRepository = singleVideoRepository;
        this.singleAudioRepository = singleAudioRepository;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Search for models matching the given criteria.
     *
     * @param query the free‑text search string to match against nickName and fullName
     * @param style an optional style filter (not yet implemented)
     * @param pageable pagination information
     * @return a page of ModelDto objects
     */
    public Page<ModelDto> searchModels(String query, String style, Pageable pageable) {
        Specification<UserLite> spec = Specification.where(null);

        spec = spec.and(hasCreatorRole());

        if (StringUtils.hasText(query)) {
            String like = "%" + query.toLowerCase(Locale.ROOT).trim() + "%";
            spec = spec.and((root, q, cb) -> {
                var nick = cb.like(cb.lower(root.get("nickName")), like);
                var full = cb.like(cb.lower(root.get("fullName")), like);
                return cb.or(nick, full);
            });
        }

        if (StringUtils.hasText(style)) {
            String like = "%" + style.toLowerCase(Locale.ROOT).trim() + "%";
            spec = spec.and((root, q, cb) -> {
                var profile = root.join("profile", JoinType.LEFT);
                return cb.like(cb.lower(profile.get("biography")), like);
            });
        }

        return userLiteRepository.findAllByDeletedDateIsNull(spec, pageable).map(this::mapToModelDto);
    }

    public Page<ModelDto> listModels(Pageable pageable) {
        return searchModels(null, null, pageable);
    }

    /**
     * Fetch full details for a single model.
     *
     * @param id the model id
     * @param limit optional limit to the number of posts/packages included in the DTO
     * @return an optional containing the ModelDto
     */
    public Optional<ModelDto> getModelDetails(Long id, Integer limit) {
        Specification<UserLite> spec = Specification.where(userLiteRepository.notDeletedSpec())
            .and(hasCreatorRole())
            .and((root, query, cb) -> cb.equal(root.get("id"), id));

        return userLiteRepository
            .findOne(spec)
            .map(userLite -> {
                ModelDto dto = mapToModelDto(userLite);
                // Fetch limited packages (posts omitted for brevity)
                Pageable pageable = Pageable.ofSize(limit == null ? 5 : limit);
                Page<PackageDto> packages = listPackagesByModel(id, null, pageable);
                dto.setPackages(packages.getContent());
                // TODO: include recent posts when PostFeedService is available
                return dto;
            });
    }

    /**
     * Fetch a complete profile payload for the profile screen.
     *
     * @param id the model id
     * @param limit optional limit to the number of sets included in the response
     * @return optional containing ModelProfileDto
     */
    public Optional<ModelProfileDto> getModelProfile(Long id, Integer limit) {
        Specification<UserLite> spec = Specification.where(userLiteRepository.notDeletedSpec())
            .and(hasCreatorRole())
            .and((root, query, cb) -> cb.equal(root.get("id"), id));

        return userLiteRepository
            .findOne(spec)
            .map(userLite -> {
                UserProfile profile = resolveProfile(userLite);
                ModelProfileDto dto = mapToModelProfileDto(userLite, profile);
                int size = limit != null && limit > 0 ? limit : 12;
                dto.setSets(listSetPreviews(id, size));
                return dto;
            });
    }

    /**
     * List packages belonging to a particular model.
     *
     * @param modelId the id of the creator
     * @param paid optional filter to restrict to paid/free content
     * @param pageable pagination information
     * @return a page of PackageDto objects
     */
    public Page<PackageDto> listPackagesByModel(Long modelId, Boolean paid, Pageable pageable) {
        Specification<ContentPackage> spec = Specification.where(null);
        if (modelId != null) {
            spec = spec.and(contentMatchesModel(modelId));
        }
        if (paid != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("isPaidContent"), paid));
        }
        return contentPackageRepository.findAllByDeletedDateIsNull(spec, pageable).map(this::mapToPackageDto);
    }

    /**
     * Retrieve a single package with its media IDs.
     *
     * @param packageId the package id
     * @return an Optional of PackageDto
     */
    public Optional<PackageDto> getPackageDetails(Long packageId) {
        return contentPackageRepository.findByIdAndDeletedDateIsNull(packageId).map(this::mapToPackageDto);
    }

    private Specification<UserLite> hasCreatorRole() {
        return (root, query, cb) -> {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<User> userRoot = subquery.from(User.class);
            var authorityJoin = userRoot.join("authorities");
            subquery.select(userRoot.get("id"));
            subquery.where(cb.equal(userRoot.get("id"), root.get("id")), cb.equal(authorityJoin.get("name"), AuthoritiesConstants.CREATOR));
            return cb.exists(subquery);
        };
    }

    private UserProfile resolveProfile(UserLite userLite) {
        if (userLite.getProfile() == null) {
            return null;
        }
        return userProfileRepository.findByIdAndDeletedDateIsNull(userLite.getProfile().getId()).orElse(null);
    }

    private List<ModelSetPreviewDto> listSetPreviews(Long modelId, int limit) {
        Specification<ContentPackage> spec = Specification.where(null).and(contentMatchesModel(modelId));
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdDate"));
        return contentPackageRepository
            .findAllByDeletedDateIsNull(spec, pageable)
            .stream()
            .map(this::toSetPreview)
            .flatMap(Optional::stream)
            .collect(Collectors.toList());
    }

    private Optional<ModelSetPreviewDto> toSetPreview(ContentPackage contentPackage) {
        Optional<PostFeed> post = loadPublicPost(contentPackage);
        if (post.isEmpty()) {
            return Optional.empty();
        }
        ModelSetPreviewDto dto = new ModelSetPreviewDto();
        dto.setId(contentPackage.getId());
        dto.setTitle(buildTitle(contentPackage, post.orElse(null)));
        dto.setDescriptionShort(shorten(buildDescription(post.orElse(null)), 140));
        dto.setCoverUrl(resolveCoverUrl(contentPackage.getId()));
        dto.setCreatedDate(contentPackage.getCreatedDate());
        dto.setPaid(Boolean.TRUE.equals(contentPackage.getIsPaidContent()));
        dto.setAmount(contentPackage.getAmount());
        return Optional.of(dto);
    }

    private Optional<PostFeed> loadPublicPost(ContentPackage contentPackage) {
        if (contentPackage.getPostId() == null) {
            return Optional.empty();
        }
        Optional<PostFeed> post = postFeedRepository.findByIdAndDeletedDateIsNull(contentPackage.getPostId());
        if (post.isEmpty()) {
            return Optional.empty();
        }
        if (!Boolean.TRUE.equals(post.orElseThrow(IllegalStateException::new).getIsPublic())) {
            return Optional.empty();
        }
        return post;
    }

    private Specification<ContentPackage> contentMatchesModel(Long modelId) {
        List<Long> postIds = resolvePostIdsForCreator(modelId);
        return (root, query, cb) -> {
            var createdByMatch = cb.equal(root.get("createdBy"), modelId.toString());
            if (postIds.isEmpty()) {
                return createdByMatch;
            }
            return cb.or(createdByMatch, root.get("postId").in(postIds));
        };
    }

    private List<Long> resolvePostIdsForCreator(Long modelId) {
        return postFeedRepository
            .findAllByCreator_IdAndDeletedDateIsNull(modelId)
            .stream()
            .map(PostFeed::getId)
            .collect(Collectors.toList());
    }

    private String resolveCoverUrl(Long contentPackageId) {
        return resolveCoverKey(contentPackageId).map(mediaTokenService::getTokenForKey).orElse(null);
    }

    private Optional<String> resolveCoverKey(Long contentPackageId) {
        List<SinglePhoto> photos = singlePhotoRepository.findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId);
        Optional<String> previewPhoto = photos
            .stream()
            .filter(photo -> Boolean.TRUE.equals(photo.getIsPreview()))
            .map(SinglePhoto::getThumbnailS3Key)
            .filter(StringUtils::hasText)
            .findFirst();
        if (previewPhoto.isPresent()) {
            return previewPhoto;
        }
        Optional<String> photoFallback = photos.stream().map(SinglePhoto::getThumbnailS3Key).filter(StringUtils::hasText).findFirst();
        if (photoFallback.isPresent()) {
            return photoFallback;
        }
        Optional<String> videoFallback = singleVideoRepository
            .findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId)
            .stream()
            .map(SingleVideo::getThumbnailS3Key)
            .filter(StringUtils::hasText)
            .findFirst();
        if (videoFallback.isPresent()) {
            return videoFallback;
        }
        return singleAudioRepository
            .findAllByContentPackage_IdAndDeletedDateIsNull(contentPackageId)
            .stream()
            .map(SingleAudio::getThumbnailS3Key)
            .filter(StringUtils::hasText)
            .findFirst();
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

    private ModelDto mapToModelDto(UserLite user) {
        ModelDto dto = new ModelDto();
        dto.setId(user.getId());
        dto.setNickName(user.getNickName());
        dto.setFullName(user.getFullName());
        if (user.getProfile() != null) {
            dto.setBiography(user.getProfile().getBiography());
            dto.setIsFree(Boolean.TRUE.equals(user.getProfile().getIsFree()));
            String profileKey = user.getProfile().getProfilePhotoS3Key();
            if (profileKey != null) {
                dto.setProfilePhotoUrl(mediaTokenService.getTokenForKey(profileKey));
            }
            String coverKey = user.getProfile().getCoverPhotoS3Key();
            if (coverKey != null) {
                dto.setCoverPhotoUrl(mediaTokenService.getTokenForKey(coverKey));
            }
        }
        String thumbKey = user.getThumbnailS3Key();
        if (thumbKey != null) {
            dto.setThumbnailUrl(mediaTokenService.getTokenForKey(thumbKey));
        }
        // TODO: populate followers count and other stats once available
        return dto;
    }

    private ModelProfileDto mapToModelProfileDto(UserLite user, UserProfile profile) {
        ModelProfileDto dto = new ModelProfileDto();
        dto.setId(user.getId());
        dto.setNickName(user.getNickName());
        dto.setFullName(user.getFullName());
        dto.setHandle(user.getNickName());
        if (profile != null) {
            dto.setBiography(profile.getBiography());
            dto.setIsFree(Boolean.TRUE.equals(profile.getIsFree()));
            String profileKey = profile.getProfilePhotoS3Key();
            if (profileKey != null) {
                dto.setProfilePhotoUrl(mediaTokenService.getTokenForKey(profileKey));
            }
            String coverKey = profile.getCoverPhotoS3Key();
            if (coverKey != null) {
                dto.setCoverPhotoUrl(mediaTokenService.getTokenForKey(coverKey));
            }
            dto.setLinks(collectLinks(profile));
        }
        String thumbKey = user.getThumbnailS3Key();
        if (thumbKey != null) {
            dto.setThumbnailUrl(mediaTokenService.getTokenForKey(thumbKey));
        }
        dto.setFollowersCount(null);
        return dto;
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

    private PackageDto mapToPackageDto(ContentPackage pkg) {
        PackageDto dto = new PackageDto();
        dto.setId(pkg.getId());
        dto.setAmount(pkg.getAmount());
        dto.setVideoCount(pkg.getVideoCount());
        dto.setImageCount(pkg.getImageCount());
        dto.setPaid(Boolean.TRUE.equals(pkg.getIsPaidContent()));
        // The createdBy field stores the model's id as a string; parse to long when needed
        if (pkg.getCreatedBy() != null) {
            try {
                dto.setModelId(Long.valueOf(pkg.getCreatedBy()));
            } catch (NumberFormatException ignored) {
                dto.setModelId(null);
            }
        }
        // Use the package id as a stand‑in for a thumbnail; front end will call /media/{id} to obtain the real URL
        dto.setThumbnailUrl(mediaTokenService.getTokenForMedia(pkg.getId()));
        return dto;
    }
}
