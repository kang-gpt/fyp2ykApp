package com.yk.booking.service.mapper;

import com.yk.booking.domain.Court;
import com.yk.booking.domain.Sport;
import com.yk.booking.service.dto.CourtDTO;
import com.yk.booking.service.dto.SportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Court} and its DTO {@link CourtDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourtMapper extends EntityMapper<CourtDTO, Court> {
    @Mapping(target = "sport", source = "sport", qualifiedByName = "sportId")
    CourtDTO toDto(Court s);

    @Named("sportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SportDTO toDtoSportId(Sport sport);
}
