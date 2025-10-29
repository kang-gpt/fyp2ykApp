package com.yk.booking.repository;

import com.yk.booking.domain.Sport;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Sport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
    @Query("SELECT s FROM Sport s WHERE LOWER(s.name) = LOWER(:name)")
    Optional<Sport> findByNameIgnoreCase(@Param("name") String name);
}
