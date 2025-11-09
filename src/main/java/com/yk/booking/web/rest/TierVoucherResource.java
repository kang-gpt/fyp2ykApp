package com.yk.booking.web.rest;

import com.yk.booking.domain.enumeration.ClientTier;
import com.yk.booking.repository.TierVoucherRepository;
import com.yk.booking.service.TierVoucherService;
import com.yk.booking.service.dto.TierVoucherDTO;
import com.yk.booking.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
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
 * REST controller for managing {@link com.yk.booking.domain.TierVoucher}.
 */
@RestController
@RequestMapping("/api/tier-vouchers")
public class TierVoucherResource {

    private static final Logger LOG = LoggerFactory.getLogger(TierVoucherResource.class);

    private static final String ENTITY_NAME = "tierVoucher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TierVoucherService tierVoucherService;

    private final TierVoucherRepository tierVoucherRepository;

    public TierVoucherResource(TierVoucherService tierVoucherService, TierVoucherRepository tierVoucherRepository) {
        this.tierVoucherService = tierVoucherService;
        this.tierVoucherRepository = tierVoucherRepository;
    }

    @PostMapping("")
    public ResponseEntity<TierVoucherDTO> createTierVoucher(@Valid @RequestBody TierVoucherDTO tierVoucherDTO) throws URISyntaxException {
        LOG.debug("REST request to save TierVoucher : {}", tierVoucherDTO);
        if (tierVoucherDTO.getId() != null) {
            throw new BadRequestAlertException("A new tierVoucher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tierVoucherDTO = tierVoucherService.save(tierVoucherDTO);
        return ResponseEntity.created(new URI("/api/tier-vouchers/" + tierVoucherDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tierVoucherDTO.getId().toString()))
            .body(tierVoucherDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TierVoucherDTO> updateTierVoucher(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TierVoucherDTO tierVoucherDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TierVoucher : {}, {}", id, tierVoucherDTO);
        if (tierVoucherDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tierVoucherDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tierVoucherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tierVoucherDTO = tierVoucherService.update(tierVoucherDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tierVoucherDTO.getId().toString()))
            .body(tierVoucherDTO);
    }

    @PutMapping("/tier/{tier}")
    public ResponseEntity<TierVoucherDTO> updateVoucherForTier(
        @PathVariable("tier") ClientTier tier,
        @RequestParam("voucherType") String voucherType
    ) {
        LOG.debug("REST request to update voucher for tier {} to {}", tier, voucherType);
        TierVoucherDTO result = tierVoucherService.updateVoucherForTier(tier, voucherType);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("")
    public List<TierVoucherDTO> getAllTierVouchers() {
        LOG.debug("REST request to get all TierVouchers");
        return tierVoucherService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TierVoucherDTO> getTierVoucher(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TierVoucher : {}", id);
        Optional<TierVoucherDTO> tierVoucherDTO = tierVoucherService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tierVoucherDTO);
    }

    @GetMapping("/tier/{tier}")
    public ResponseEntity<TierVoucherDTO> getTierVoucherByTier(@PathVariable("tier") ClientTier tier) {
        LOG.debug("REST request to get TierVoucher by tier : {}", tier);
        Optional<TierVoucherDTO> tierVoucherDTO = tierVoucherService.findByTier(tier);
        return ResponseUtil.wrapOrNotFound(tierVoucherDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTierVoucher(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TierVoucher : {}", id);
        tierVoucherService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
