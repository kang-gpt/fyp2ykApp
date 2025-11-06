package com.yk.booking.repository;

import com.yk.booking.domain.Booking;
import com.yk.booking.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Booking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByUser(User user);
}
