package com.monsterdam.app.repository;

import com.monsterdam.app.domain.SinglePhoto;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SinglePhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SinglePhotoRepository extends LogicalDeletionRepository<SinglePhoto> {
    List<SinglePhoto> findAllByContentPackage_IdAndDeletedDateIsNull(Long contentPackageId);
}
