package com.yk.booking.repository;

import com.yk.booking.domain.TierVoucher;
import com.yk.booking.domain.enumeration.ClientTier;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TierVoucher entity.
 */
@Repository
public interface TierVoucherRepository extends JpaRepository<TierVoucher, Long> {
    Optional<TierVoucher> findByTier(ClientTier tier);
}
