package com.yk.booking.service.mapper;

import com.yk.booking.domain.TierVoucher;
import com.yk.booking.service.dto.TierVoucherDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TierVoucher} and its DTO {@link TierVoucherDTO}.
 */
@Mapper(componentModel = "spring")
public interface TierVoucherMapper extends EntityMapper<TierVoucherDTO, TierVoucher> {}
