package com.yk.booking.service.mapper;

import com.yk.booking.domain.Court;
import com.yk.booking.domain.TimeSlot;
import com.yk.booking.service.dto.CourtDTO;
import com.yk.booking.service.dto.TimeSlotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TimeSlot} and its DTO {@link TimeSlotDTO}.
 */
@Mapper(componentModel = "spring")
public interface TimeSlotMapper extends EntityMapper<TimeSlotDTO, TimeSlot> {
    @Mapping(target = "court", source = "court", qualifiedByName = "courtId")
    TimeSlotDTO toDto(TimeSlot s);

    @Named("courtId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourtDTO toDtoCourtId(Court court);
}
