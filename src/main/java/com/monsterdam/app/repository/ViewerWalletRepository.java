package com.monsterdam.app.repository;

import com.monsterdam.app.domain.ViewerWallet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ViewerWallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ViewerWalletRepository extends LogicalDeletionRepository<ViewerWallet> {}
