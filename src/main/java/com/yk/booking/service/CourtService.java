package com.yk.booking.service;

import com.yk.booking.domain.Court;
import com.yk.booking.repository.CourtRepository;
import com.yk.booking.service.dto.CourtDTO;
import com.yk.booking.service.mapper.CourtMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yk.booking.domain.Court}.
 */
@Service
@Transactional
public class CourtService {

    private static final Logger LOG = LoggerFactory.getLogger(CourtService.class);

    private final CourtRepository courtRepository;

    private final CourtMapper courtMapper;

    public CourtService(CourtRepository courtRepository, CourtMapper courtMapper) {
        this.courtRepository = courtRepository;
        this.courtMapper = courtMapper;
    }

    /**
     * Save a court.
     *
     * @param courtDTO the entity to save.
     * @return the persisted entity.
     */
    public CourtDTO save(CourtDTO courtDTO) {
        LOG.debug("Request to save Court : {}", courtDTO);
        Court court = courtMapper.toEntity(courtDTO);
        court = courtRepository.save(court);
        return courtMapper.toDto(court);
    }

    /**
     * Update a court.
     *
     * @param courtDTO the entity to save.
     * @return the persisted entity.
     */
    public CourtDTO update(CourtDTO courtDTO) {
        LOG.debug("Request to update Court : {}", courtDTO);
        Court court = courtMapper.toEntity(courtDTO);
        court = courtRepository.save(court);
        return courtMapper.toDto(court);
    }

    /**
     * Partially update a court.
     *
     * @param courtDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CourtDTO> partialUpdate(CourtDTO courtDTO) {
        LOG.debug("Request to partially update Court : {}", courtDTO);

        return courtRepository
            .findById(courtDTO.getId())
            .map(existingCourt -> {
                courtMapper.partialUpdate(existingCourt, courtDTO);

                return existingCourt;
            })
            .map(courtRepository::save)
            .map(courtMapper::toDto);
    }

    /**
     * Get all the courts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CourtDTO> findAll() {
        LOG.debug("Request to get all Courts");
        return courtRepository.findAll().stream().map(courtMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one court by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CourtDTO> findOne(Long id) {
        LOG.debug("Request to get Court : {}", id);
        return courtRepository.findById(id).map(courtMapper::toDto);
    }

    /**
     * Delete the court by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Court : {}", id);
        courtRepository.deleteById(id);
    }
}
