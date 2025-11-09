package com.yk.booking.service.mapper;

import com.yk.booking.domain.Booking;
import com.yk.booking.domain.Payment;
import com.yk.booking.domain.TimeSlot;
import com.yk.booking.domain.User;
import com.yk.booking.service.dto.BookingDTO;
import com.yk.booking.service.dto.PaymentDTO;
import com.yk.booking.service.dto.TimeSlotDTO;
import com.yk.booking.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "timeSlot", source = "timeSlot", qualifiedByName = "timeSlotId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "payment", source = "payment", qualifiedByName = "paymentId")
    BookingDTO toDto(Booking s);

    @Named("timeSlotId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TimeSlotDTO toDtoTimeSlotId(TimeSlot timeSlot);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserId(User user);

    @Named("paymentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PaymentDTO toDtoPaymentId(Payment payment);
}
