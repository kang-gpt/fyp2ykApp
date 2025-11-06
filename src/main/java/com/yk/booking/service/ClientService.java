package com.yk.booking.service;

import com.yk.booking.domain.Client;
import com.yk.booking.domain.ClientTier;
import com.yk.booking.repository.BookingRepository;
import com.yk.booking.repository.ClientRepository;
import com.yk.booking.repository.ClientTierRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yk.booking.domain.Client}.
 */
@Service
@Transactional
public class ClientService {

    private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;
    private final ClientTierRepository clientTierRepository;

    public ClientService(
        ClientRepository clientRepository,
        BookingRepository bookingRepository,
        ClientTierRepository clientTierRepository
    ) {
        this.clientRepository = clientRepository;
        this.bookingRepository = bookingRepository;
        this.clientTierRepository = clientTierRepository;
    }

    /**
     * Save a client.
     *
     * @param client the entity to save.
     * @return the persisted entity.
     */
    public Client save(Client client) {
        LOG.debug("Request to save Client : {}", client);
        return clientRepository.save(client);
    }

    /**
     * Update a client.
     *
     * @param client the entity to save.
     * @return the persisted entity.
     */
    public Client update(Client client) {
        LOG.debug("Request to update Client : {}", client);
        return clientRepository.save(client);
    }

    /**
     * Partially update a client.
     *
     * @param client the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Client> partialUpdate(Client client) {
        LOG.debug("Request to partially update Client : {}", client);

        return clientRepository
            .findById(client.getId())
            .map(existingClient -> {
                if (client.getName() != null) {
                    existingClient.setName(client.getName());
                }
                if (client.getDescription() != null) {
                    existingClient.setDescription(client.getDescription());
                }
                if (client.getAge() != null) {
                    existingClient.setAge(client.getAge());
                }
                if (client.getDob() != null) {
                    existingClient.setDob(client.getDob());
                }
                if (client.getClientTier() != null) {
                    existingClient.setClientTier(client.getClientTier());
                }

                return existingClient;
            })
            .map(clientRepository::save);
    }

    /**
     * Get all the clients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Client> findAll(Pageable pageable) {
        LOG.debug("Request to get all Clients");
        return clientRepository.findAll(pageable);
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Client> findOne(Long id) {
        LOG.debug("Request to get Client : {}", id);
        return clientRepository.findById(id);
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }

    /**
     * Update client tier based on total bookings.
     *
     * @param client the client to update.
     * @return the updated client.
     */
    public Client updateClientTier(Client client) {
        LOG.debug("Request to update Client Tier for Client : {}", client);

        Long totalBookings = bookingRepository.countByUser(client.getUser());
        String tierName;

        if (totalBookings >= 21) {
            tierName = "PLATINUM";
        } else if (totalBookings >= 11) {
            tierName = "GOLD";
        } else if (totalBookings >= 6) {
            tierName = "IRON";
        } else {
            tierName = "LEAD";
        }

        ClientTier tier = clientTierRepository
            .findByTierName(tierName)
            .orElseThrow(() -> new RuntimeException("ClientTier not found: " + tierName));
        client.setClientTier(tier);
        return clientRepository.save(client);
    }
}
