package com.yk.booking.service;

import com.yk.booking.domain.TierVoucher;
import com.yk.booking.domain.enumeration.ClientTier;
import com.yk.booking.repository.TierVoucherRepository;
import com.yk.booking.service.dto.TierVoucherDTO;
import com.yk.booking.service.mapper.TierVoucherMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yk.booking.domain.TierVoucher}.
 */
@Service
@Transactional
public class TierVoucherService {

    private static final Logger LOG = LoggerFactory.getLogger(TierVoucherService.class);

    private final TierVoucherRepository tierVoucherRepository;

    private final TierVoucherMapper tierVoucherMapper;

    public TierVoucherService(TierVoucherRepository tierVoucherRepository, TierVoucherMapper tierVoucherMapper) {
        this.tierVoucherRepository = tierVoucherRepository;
        this.tierVoucherMapper = tierVoucherMapper;
    }

    public TierVoucherDTO save(TierVoucherDTO tierVoucherDTO) {
        LOG.debug("Request to save TierVoucher : {}", tierVoucherDTO);
        TierVoucher tierVoucher = tierVoucherMapper.toEntity(tierVoucherDTO);
        tierVoucher = tierVoucherRepository.save(tierVoucher);
        return tierVoucherMapper.toDto(tierVoucher);
    }

    public TierVoucherDTO update(TierVoucherDTO tierVoucherDTO) {
        LOG.debug("Request to update TierVoucher : {}", tierVoucherDTO);
        TierVoucher tierVoucher = tierVoucherMapper.toEntity(tierVoucherDTO);
        tierVoucher = tierVoucherRepository.save(tierVoucher);
        return tierVoucherMapper.toDto(tierVoucher);
    }

    public TierVoucherDTO updateVoucherForTier(ClientTier tier, String voucherType) {
        LOG.debug("Request to update voucher for tier {} to {}", tier, voucherType);
        Optional<TierVoucher> existing = tierVoucherRepository.findByTier(tier);

        TierVoucher tierVoucher;
        if (existing.isPresent()) {
            tierVoucher = existing.get();
            tierVoucher.setVoucherType(voucherType);
        } else {
            tierVoucher = new TierVoucher();
            tierVoucher.setTier(tier);
            tierVoucher.setVoucherType(voucherType);
        }

        tierVoucher = tierVoucherRepository.save(tierVoucher);
        return tierVoucherMapper.toDto(tierVoucher);
    }

    @Transactional(readOnly = true)
    public List<TierVoucherDTO> findAll() {
        LOG.debug("Request to get all TierVouchers");
        return tierVoucherRepository.findAll().stream().map(tierVoucherMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<TierVoucherDTO> findOne(Long id) {
        LOG.debug("Request to get TierVoucher : {}", id);
        return tierVoucherRepository.findById(id).map(tierVoucherMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<TierVoucherDTO> findByTier(ClientTier tier) {
        LOG.debug("Request to get TierVoucher by tier : {}", tier);
        return tierVoucherRepository.findByTier(tier).map(tierVoucherMapper::toDto);
    }

    public void delete(Long id) {
        LOG.debug("Request to delete TierVoucher : {}", id);
        tierVoucherRepository.deleteById(id);
    }
}
