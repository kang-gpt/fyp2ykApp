package com.yk.booking.service;

import com.yk.booking.domain.ClientTier;
import com.yk.booking.repository.ClientTierRepository;
import com.yk.booking.service.dto.ClientTierDTO;
import com.yk.booking.service.mapper.ClientTierMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yk.booking.domain.ClientTier}.
 */
@Service
@Transactional
public class ClientTierService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientTierService.class);

    private final ClientTierRepository clientTierRepository;

    private final ClientTierMapper clientTierMapper;

    public ClientTierService(ClientTierRepository clientTierRepository, ClientTierMapper clientTierMapper) {
        this.clientTierRepository = clientTierRepository;
        this.clientTierMapper = clientTierMapper;
    }

    /**
     * Save a clientTier.
     *
     * @param clientTierDTO the entity to save.
     * @return the persisted entity.
     */
    public ClientTierDTO save(ClientTierDTO clientTierDTO) {
        LOG.debug("Request to save ClientTier : {}", clientTierDTO);
        ClientTier clientTier = clientTierMapper.toEntity(clientTierDTO);
        clientTier = clientTierRepository.save(clientTier);
        return clientTierMapper.toDto(clientTier);
    }

    /**
     * Update a clientTier.
     *
     * @param clientTierDTO the entity to save.
     * @return the persisted entity.
     */
    public ClientTierDTO update(ClientTierDTO clientTierDTO) {
        LOG.debug("Request to update ClientTier : {}", clientTierDTO);
        ClientTier clientTier = clientTierMapper.toEntity(clientTierDTO);
        clientTier = clientTierRepository.save(clientTier);
        return clientTierMapper.toDto(clientTier);
    }

    /**
     * Partially update a clientTier.
     *
     * @param clientTierDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ClientTierDTO> partialUpdate(ClientTierDTO clientTierDTO) {
        LOG.debug("Request to partially update ClientTier : {}", clientTierDTO);

        return clientTierRepository
            .findById(clientTierDTO.getId())
            .map(existingClientTier -> {
                clientTierMapper.partialUpdate(existingClientTier, clientTierDTO);

                return existingClientTier;
            })
            .map(clientTierRepository::save)
            .map(clientTierMapper::toDto);
    }

    /**
     * Get all the clientTiers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ClientTierDTO> findAll() {
        LOG.debug("Request to get all ClientTiers");
        return clientTierRepository.findAll().stream().map(clientTierMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one clientTier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ClientTierDTO> findOne(Long id) {
        LOG.debug("Request to get ClientTier : {}", id);
        return clientTierRepository.findById(id).map(clientTierMapper::toDto);
    }

    /**
     * Delete the clientTier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ClientTier : {}", id);
        clientTierRepository.deleteById(id);
    }
}
