package com.monsterdam.app.repository;

import com.monsterdam.app.domain.PurchasedContent;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PurchasedContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchasedContentRepository extends LogicalDeletionRepository<PurchasedContent> {
    @Query(
        "select avg(p.rating) from PurchasedContent p where p.deletedDate is null and p.contentPackage.id = :contentPackageId and p.rating is not null"
    )
    Optional<Double> findAverageRatingByContentPackageId(Long contentPackageId);

    boolean existsByContentPackage_IdAndViewer_IdAndDeletedDateIsNull(Long contentPackageId, Long viewerId);
}
