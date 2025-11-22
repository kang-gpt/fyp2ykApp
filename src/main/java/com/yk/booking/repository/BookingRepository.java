package com.yk.booking.repository;

import com.yk.booking.domain.Booking;
import com.yk.booking.domain.User;
import com.yk.booking.domain.enumeration.BookingStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Booking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    long countByUser(User user);

    List<Booking> findAllByStatus(BookingStatus status);

    List<Booking> findAllByStatusAndBookingDateBetween(BookingStatus status, Instant start, Instant end);

    Optional<Booking> findByBookingId(String bookingId);

    @Query(
        "SELECT SUM(b.payment.amount) FROM Booking b WHERE b.status = :status AND b.bookingDate >= :startOfDay AND b.bookingDate < :endOfDay"
    )
    Optional<BigDecimal> findTotalRevenueByStatusAndBookingDateBetween(
        @Param("status") BookingStatus status,
        @Param("startOfDay") Instant startOfDay,
        @Param("endOfDay") Instant endOfDay
    );
}
