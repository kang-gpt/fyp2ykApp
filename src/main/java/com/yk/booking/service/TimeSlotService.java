package com.yk.booking.service;

import com.yk.booking.domain.Booking;
import com.yk.booking.domain.TimeSlot;
import com.yk.booking.repository.BookingRepository;
import com.yk.booking.repository.TimeSlotRepository;
import com.yk.booking.service.dto.TimeSlotDTO;
import com.yk.booking.service.mapper.TimeSlotMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yk.booking.domain.TimeSlot}.
 */
@Service
@Transactional
public class TimeSlotService {

    private static final Logger LOG = LoggerFactory.getLogger(TimeSlotService.class);

    private final TimeSlotRepository timeSlotRepository;

    private final TimeSlotMapper timeSlotMapper;

    private final BookingRepository bookingRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, TimeSlotMapper timeSlotMapper, BookingRepository bookingRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotMapper = timeSlotMapper;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Save a timeSlot.
     *
     * @param timeSlotDTO the entity to save.
     * @return the persisted entity.
     */
    public TimeSlotDTO save(TimeSlotDTO timeSlotDTO) {
        LOG.debug("Request to save TimeSlot : {}", timeSlotDTO);
        TimeSlot timeSlot = timeSlotMapper.toEntity(timeSlotDTO);
        timeSlot = timeSlotRepository.save(timeSlot);
        return timeSlotMapper.toDto(timeSlot);
    }

    /**
     * Update a timeSlot.
     *
     * @param timeSlotDTO the entity to save.
     * @return the persisted entity.
     */
    public TimeSlotDTO update(TimeSlotDTO timeSlotDTO) {
        LOG.debug("Request to update TimeSlot : {}", timeSlotDTO);
        TimeSlot timeSlot = timeSlotMapper.toEntity(timeSlotDTO);
        timeSlot = timeSlotRepository.save(timeSlot);
        return timeSlotMapper.toDto(timeSlot);
    }

    /**
     * Partially update a timeSlot.
     *
     * @param timeSlotDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TimeSlotDTO> partialUpdate(TimeSlotDTO timeSlotDTO) {
        LOG.debug("Request to partially update TimeSlot : {}", timeSlotDTO);

        return timeSlotRepository
            .findById(timeSlotDTO.getId())
            .map(existingTimeSlot -> {
                timeSlotMapper.partialUpdate(existingTimeSlot, timeSlotDTO);

                return existingTimeSlot;
            })
            .map(timeSlotRepository::save)
            .map(timeSlotMapper::toDto);
    }

    /**
     * Get all the timeSlots.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TimeSlotDTO> findAll() {
        LOG.debug("Request to get all TimeSlots");
        return timeSlotRepository.findAll().stream().map(timeSlotMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the timeSlots where Booking is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TimeSlotDTO> findAllWhereBookingIsNull() {
        LOG.debug("Request to get all timeSlots where Booking is null");
        return StreamSupport.stream(timeSlotRepository.findAll().spliterator(), false)
            .filter(timeSlot -> timeSlot.getBooking() == null)
            .map(timeSlotMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one timeSlot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TimeSlotDTO> findOne(Long id) {
        LOG.debug("Request to get TimeSlot : {}", id);
        return timeSlotRepository.findById(id).map(timeSlotMapper::toDto);
    }

    /**
     * Delete the timeSlot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TimeSlot : {}", id);
        timeSlotRepository.deleteById(id);
    }

    /**
     * Get time slots for a specific court and date.
     *
     * @param courtId the id of the court.
     * @param dateStr the date in YYYY-MM-DD format.
     * @return the list of time slots with booking status.
     */
    @Transactional(readOnly = true)
    public List<TimeSlotDTO> findByCourtAndDate(Long courtId, String dateStr) {
        LOG.debug("Request to get TimeSlots for court {} on date {}", courtId, dateStr);

        // Parse the date
        LocalDate date = LocalDate.parse(dateStr);
        Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Get all bookings for this court and date
        List<Booking> bookings = bookingRepository
            .findAll()
            .stream()
            .filter(
                booking ->
                    booking.getTimeSlot() != null &&
                    booking.getTimeSlot().getCourt() != null &&
                    booking.getTimeSlot().getCourt().getId().equals(courtId) &&
                    booking.getBookingDate() != null &&
                    !booking.getBookingDate().isBefore(startOfDay) &&
                    booking.getBookingDate().isBefore(endOfDay) &&
                    "APPROVED".equals(booking.getStatus().name())
            )
            .collect(Collectors.toList());

        // Get all time slots for approved bookings
        return bookings.stream().map(Booking::getTimeSlot).map(timeSlotMapper::toDto).collect(Collectors.toList());
    }
}
