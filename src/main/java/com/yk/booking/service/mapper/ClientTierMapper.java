package com.yk.booking.service.mapper;

import com.yk.booking.domain.ClientTier;
import com.yk.booking.service.dto.ClientTierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClientTier} and its DTO {@link ClientTierDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientTierMapper extends EntityMapper<ClientTierDTO, ClientTier> {}
