package com.yk.booking.service.mapper;

import com.yk.booking.domain.Court;
import com.yk.booking.domain.Sport;
import com.yk.booking.domain.TimeSlot;
import com.yk.booking.service.dto.CourtDTO;
import com.yk.booking.service.dto.SportDTO;
import com.yk.booking.service.dto.TimeSlotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeSlot} and its DTO {@link TimeSlotDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimeSlotMapper extends EntityMapper<TimeSlotDTO, TimeSlot> {
    @Mapping(target = "court", source = "court", qualifiedByName = "courtWithSport")
    TimeSlotDTO toDto(TimeSlot s);

    @Named("courtWithSport")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "sport", source = "sport", qualifiedByName = "sportIdAndName")
    CourtDTO toDtoCourtId(Court court);

    @Named("sportIdAndName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SportDTO toDtoSportId(Sport sport);
}
