package com.yk.booking.repository;

import com.yk.booking.domain.Court;
import com.yk.booking.domain.Sport;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Court entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    List<Court> findBySport(Sport sport);

    @Override
    @EntityGraph(attributePaths = "sport")
    List<Court> findAll();
}
