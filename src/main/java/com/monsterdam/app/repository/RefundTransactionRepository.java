package com.monsterdam.app.repository;

import com.monsterdam.app.domain.RefundTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RefundTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RefundTransactionRepository extends LogicalDeletionRepository<RefundTransaction> {}
