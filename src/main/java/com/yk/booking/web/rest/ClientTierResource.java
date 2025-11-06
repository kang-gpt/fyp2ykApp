package com.yk.booking.web.rest;

import com.yk.booking.repository.ClientTierRepository;
import com.yk.booking.service.ClientTierService;
import com.yk.booking.service.dto.ClientTierDTO;
import com.yk.booking.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yk.booking.domain.ClientTier}.
 */
@RestController
@RequestMapping("/api/client-tiers")
public class ClientTierResource {

    private static final Logger LOG = LoggerFactory.getLogger(ClientTierResource.class);

    private static final String ENTITY_NAME = "clientTier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientTierService clientTierService;

    private final ClientTierRepository clientTierRepository;

    public ClientTierResource(ClientTierService clientTierService, ClientTierRepository clientTierRepository) {
        this.clientTierService = clientTierService;
        this.clientTierRepository = clientTierRepository;
    }

    /**
     * {@code POST  /client-tiers} : Create a new clientTier.
     *
     * @param clientTierDTO the clientTierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientTierDTO, or with status {@code 400 (Bad Request)} if the clientTier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ClientTierDTO> createClientTier(@Valid @RequestBody ClientTierDTO clientTierDTO) throws URISyntaxException {
        LOG.debug("REST request to save ClientTier : {}", clientTierDTO);
        if (clientTierDTO.getId() != null) {
            throw new BadRequestAlertException("A new clientTier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        clientTierDTO = clientTierService.save(clientTierDTO);
        return ResponseEntity.created(new URI("/api/client-tiers/" + clientTierDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, clientTierDTO.getId().toString()))
            .body(clientTierDTO);
    }

    /**
     * {@code PUT  /client-tiers/:id} : Updates an existing clientTier.
     *
     * @param id the id of the clientTierDTO to save.
     * @param clientTierDTO the clientTierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientTierDTO,
     * or with status {@code 400 (Bad Request)} if the clientTierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientTierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientTierDTO> updateClientTier(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ClientTierDTO clientTierDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ClientTier : {}, {}", id, clientTierDTO);
        if (clientTierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientTierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientTierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        clientTierDTO = clientTierService.update(clientTierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientTierDTO.getId().toString()))
            .body(clientTierDTO);
    }

    /**
     * {@code PATCH  /client-tiers/:id} : Partial updates given fields of an existing clientTier, field will ignore if it is null
     *
     * @param id the id of the clientTierDTO to save.
     * @param clientTierDTO the clientTierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientTierDTO,
     * or with status {@code 400 (Bad Request)} if the clientTierDTO is not valid,
     * or with status {@code 404 (Not Found)} if the clientTierDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientTierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ClientTierDTO> partialUpdateClientTier(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ClientTierDTO clientTierDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ClientTier partially : {}, {}", id, clientTierDTO);
        if (clientTierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientTierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientTierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClientTierDTO> result = clientTierService.partialUpdate(clientTierDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, clientTierDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /client-tiers} : get all the clientTiers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientTiers in body.
     */
    @GetMapping("")
    public List<ClientTierDTO> getAllClientTiers() {
        LOG.debug("REST request to get all ClientTiers");
        return clientTierService.findAll();
    }

    /**
     * {@code GET  /client-tiers/:id} : get the "id" clientTier.
     *
     * @param id the id of the clientTierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientTierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientTierDTO> getClientTier(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ClientTier : {}", id);
        Optional<ClientTierDTO> clientTierDTO = clientTierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clientTierDTO);
    }

    /**
     * {@code DELETE  /client-tiers/:id} : delete the "id" clientTier.
     *
     * @param id the id of the clientTierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientTier(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ClientTier : {}", id);
        clientTierService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
