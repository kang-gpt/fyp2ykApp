package com.yk.booking.service.mapper;

import com.yk.booking.domain.Payment;
import com.yk.booking.domain.User;
import com.yk.booking.service.dto.PaymentDTO;
import com.yk.booking.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    PaymentDTO toDto(Payment s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
