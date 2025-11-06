package com.yk.booking.service.mapper;

import com.yk.booking.domain.Client;
import com.yk.booking.domain.ClientTier;
import com.yk.booking.domain.User;
import com.yk.booking.service.dto.ClientDTO;
import com.yk.booking.service.dto.ClientTierDTO;
import com.yk.booking.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "clientTier", source = "clientTier", qualifiedByName = "clientTierId")
    ClientDTO toDto(Client s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("clientTierId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientTierDTO toDtoClientTierId(ClientTier clientTier);
}
