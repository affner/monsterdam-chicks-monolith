package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.LedgerEntry;
import com.monsterdam.app.service.dto.LedgerEntryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LedgerEntry} and its DTO {@link LedgerEntryDTO}.
 */
@Mapper(componentModel = "spring")
public interface LedgerEntryMapper extends EntityMapper<LedgerEntryDTO, LedgerEntry> {}
