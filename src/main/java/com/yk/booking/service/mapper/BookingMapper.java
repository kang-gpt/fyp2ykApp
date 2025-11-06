package com.yk.booking.service.mapper;

import com.yk.booking.domain.Booking;
import com.yk.booking.domain.TimeSlot;
import com.yk.booking.service.dto.BookingDTO;
import com.yk.booking.service.dto.TimeSlotDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "timeSlot", source = "timeSlot", qualifiedByName = "timeSlotId")
    BookingDTO toDto(Booking s);

    @Named("timeSlotId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TimeSlotDTO toDtoTimeSlotId(TimeSlot timeSlot);
}
