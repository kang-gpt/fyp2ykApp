package com.yk.booking.service.mapper;

import com.yk.booking.domain.Booking;
import com.yk.booking.domain.Court;
import com.yk.booking.domain.Payment;
import com.yk.booking.domain.Sport;
import com.yk.booking.domain.TimeSlot;
import com.yk.booking.domain.User;
import com.yk.booking.service.dto.BookingDTO;
import com.yk.booking.service.dto.CourtDTO;
import com.yk.booking.service.dto.PaymentDTO;
import com.yk.booking.service.dto.SportDTO;
import com.yk.booking.service.dto.TimeSlotDTO;
import com.yk.booking.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "timeSlot", source = "timeSlot", qualifiedByName = "timeSlotWithCourtAndSport")
    @Mapping(target = "user", source = "user", qualifiedByName = "userWithDetails")
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentWithStatus")
    BookingDTO toDto(Booking s);

    @Named("timeSlotWithCourtAndSport")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    @Mapping(target = "court", source = "court", qualifiedByName = "courtWithSport")
    TimeSlotDTO toDtoTimeSlotId(TimeSlot timeSlot);

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

    @Named("userWithDetails")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserId(User user);

    @Named("paymentWithStatus")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "status", source = "status")
    PaymentDTO toDtoPaymentId(Payment payment);
}
