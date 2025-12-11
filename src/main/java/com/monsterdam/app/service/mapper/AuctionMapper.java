package com.monsterdam.app.service.mapper;

import com.monsterdam.app.domain.Auction;
import com.monsterdam.app.domain.UserLite;
import com.monsterdam.app.service.dto.AuctionDTO;
import com.monsterdam.app.service.dto.UserLiteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Auction} and its DTO {@link AuctionDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuctionMapper extends EntityMapper<AuctionDTO, Auction> {
    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    @Mapping(target = "winner.profile.user", ignore = true)
    @Mapping(target = "winner.settings.user", ignore = true)
    Auction toEntity(AuctionDTO dto);

    @Override
    @Mapping(target = "creator.profile.user", ignore = true)
    @Mapping(target = "creator.settings.user", ignore = true)
    @Mapping(target = "winner.profile.user", ignore = true)
    @Mapping(target = "winner.settings.user", ignore = true)
    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget Auction entity, AuctionDTO dto);

    @Mapping(target = "creator", source = "creator", qualifiedByName = "userLiteId")
    @Mapping(target = "winner", source = "winner", qualifiedByName = "userLiteId")
    AuctionDTO toDto(Auction s);

    @Named("userLiteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserLiteDTO toDtoUserLiteId(UserLite userLite);
}
