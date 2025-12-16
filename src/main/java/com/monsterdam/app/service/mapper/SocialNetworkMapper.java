package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.SocialNetwork;
import com.monsterdam.app.service.dto.SocialNetworkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialNetwork} and its DTO {@link SocialNetworkDTO}.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SocialNetworkMapper extends EntityMapper<SocialNetworkDTO, SocialNetwork> {}
