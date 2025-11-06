package com.yk.booking.web.rest;

import com.yk.booking.domain.Court;
import com.yk.booking.domain.Sport;
import com.yk.booking.repository.CourtRepository;
import com.yk.booking.repository.SportRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yk.booking.domain.Court}.
 */
@RestController
@RequestMapping("/api/courts")
@Transactional
public class CourtResource {

    private final Logger log = LoggerFactory.getLogger(CourtResource.class);

    private static final String ENTITY_NAME = "court";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourtRepository courtRepository;
    private final SportRepository sportRepository;

    public CourtResource(CourtRepository courtRepository, SportRepository sportRepository) {
        this.courtRepository = courtRepository;
        this.sportRepository = sportRepository;
    }

    /**
     * {@code POST  /courts} : Create a new court.
     *
     * @param court the court to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new court, or with status {@code 400 (Bad Request)} if the court has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Court> createCourt(@RequestBody Court court) throws URISyntaxException {
        log.debug("REST request to save Court : {}", court);
        if (court.getId() != null) {
            throw new RuntimeException("A new court cannot already have an ID");
        }
        court = courtRepository.save(court);
        return ResponseEntity.created(new URI("/api/courts/" + court.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, court.getId().toString()))
            .body(court);
    }

    /**
     * {@code PUT  /courts/:id} : Updates an existing court.
     *
     * @param id the id of the court to save.
     * @param court the court to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated court,
     * or with status {@code 400 (Bad Request)} if the court is not valid,
     * or with status {@code 500 (Internal Server Error)} if the court couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Court> updateCourt(@PathVariable(value = "id", required = false) final Long id, @RequestBody Court court)
        throws URISyntaxException {
        log.debug("REST request to update Court : {}, {}", id, court);
        if (court.getId() == null) {
            throw new RuntimeException("Invalid id");
        }
        if (!Objects.equals(id, court.getId())) {
            throw new RuntimeException("Invalid ID");
        }

        if (!courtRepository.existsById(id)) {
            throw new RuntimeException("Entity not found");
        }

        court = courtRepository.save(court);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, court.getId().toString()))
            .body(court);
    }

    /**
     * {@code GET  /courts} : get all the courts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courts in body.
     */
    @GetMapping("")
    public List<Court> getAllCourts(@RequestParam(required = false) String sportName) {
        log.debug("REST request to get all Courts, with sportName: {}", sportName);
        if (sportName != null && !sportName.isEmpty()) {
            List<Sport> allSports = sportRepository.findAll();
            log.debug("All sports in DB: {}", allSports);
            Optional<Sport> foundSport = sportRepository.findByNameIgnoreCase(sportName);
            if (foundSport.isPresent()) {
                List<Court> courtsBySport = courtRepository.findBySport(foundSport.orElseThrow());
                log.debug(
                    "Found sport {} with ID {} and {} courts.",
                    foundSport.orElseThrow().getName(),
                    foundSport.orElseThrow().getId(),
                    courtsBySport.size()
                );
                return courtsBySport;
            } else {
                log.debug("No Sport found for name: {}", sportName);
                return List.of();
            }
        } else {
            List<Court> allCourts = courtRepository.findAll();
            log.debug("No sportName parameter provided. Returning all {} courts.", allCourts.size());
            return allCourts;
        }
    }

    /**
     * {@code GET  /courts/:id} : get the "id" court.
     *
     * @param id the id of the court to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the court, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Court> getCourt(@PathVariable("id") Long id) {
        log.debug("REST request to get Court : {}", id);
        Optional<Court> court = courtRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(court);
    }

    /**
     * {@code DELETE  /courts/:id} : delete the "id" court.
     *
     * @param id the id of the court to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable("id") Long id) {
        log.debug("REST request to delete Court : {}", id);
        courtRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
