package com.yk.booking.repository;

import com.yk.booking.domain.Booking;
import com.yk.booking.domain.Court;
import com.yk.booking.domain.User;
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
    @Query("select booking from Booking booking where booking.court = :court and booking.startTime between :start and :end")
    List<Booking> findAllByCourtAndStartTimeBetween(@Param("court") Court court, @Param("start") Instant start, @Param("end") Instant end);

    @Query(
        "select booking from Booking booking where booking.court = :court and booking.startTime = :startTime and booking.endTime = :endTime"
    )
    Optional<Booking> findByCourtAndStartTimeAndEndTime(
        @Param("court") Court court,
        @Param("startTime") Instant startTime,
        @Param("endTime") Instant endTime
    );

    @Query("select count(booking) from Booking booking where booking.user = :user")
    Long countByUser(@Param("user") User user);

    List<Booking> findByUser(@Param("user") User user);
}
