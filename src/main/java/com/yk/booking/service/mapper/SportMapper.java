package com.yk.booking.service.mapper;

import com.yk.booking.domain.Sport;
import com.yk.booking.service.dto.SportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sport} and its DTO {@link SportDTO}.
 */
@Mapper(componentModel = "spring")
public interface SportMapper extends EntityMapper<SportDTO, Sport> {}
