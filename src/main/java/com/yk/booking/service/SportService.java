package com.yk.booking.service;

import com.yk.booking.domain.Sport;
import com.yk.booking.repository.SportRepository;
import com.yk.booking.service.dto.SportDTO;
import com.yk.booking.service.mapper.SportMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yk.booking.domain.Sport}.
 */
@Service
@Transactional
public class SportService {

    private static final Logger LOG = LoggerFactory.getLogger(SportService.class);

    private final SportRepository sportRepository;

    private final SportMapper sportMapper;

    public SportService(SportRepository sportRepository, SportMapper sportMapper) {
        this.sportRepository = sportRepository;
        this.sportMapper = sportMapper;
    }

    /**
     * Save a sport.
     *
     * @param sportDTO the entity to save.
     * @return the persisted entity.
     */
    public SportDTO save(SportDTO sportDTO) {
        LOG.debug("Request to save Sport : {}", sportDTO);
        Sport sport = sportMapper.toEntity(sportDTO);
        sport = sportRepository.save(sport);
        return sportMapper.toDto(sport);
    }

    /**
     * Update a sport.
     *
     * @param sportDTO the entity to save.
     * @return the persisted entity.
     */
    public SportDTO update(SportDTO sportDTO) {
        LOG.debug("Request to update Sport : {}", sportDTO);
        Sport sport = sportMapper.toEntity(sportDTO);
        sport = sportRepository.save(sport);
        return sportMapper.toDto(sport);
    }

    /**
     * Partially update a sport.
     *
     * @param sportDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SportDTO> partialUpdate(SportDTO sportDTO) {
        LOG.debug("Request to partially update Sport : {}", sportDTO);

        return sportRepository
            .findById(sportDTO.getId())
            .map(existingSport -> {
                sportMapper.partialUpdate(existingSport, sportDTO);

                return existingSport;
            })
            .map(sportRepository::save)
            .map(sportMapper::toDto);
    }

    /**
     * Get all the sports.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SportDTO> findAll() {
        LOG.debug("Request to get all Sports");
        return sportRepository.findAll().stream().map(sportMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one sport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SportDTO> findOne(Long id) {
        LOG.debug("Request to get Sport : {}", id);
        return sportRepository.findById(id).map(sportMapper::toDto);
    }

    /**
     * Delete the sport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Sport : {}", id);
        sportRepository.deleteById(id);
    }
}
