package com.yk.booking.service;

import com.yk.booking.domain.Booking;
import com.yk.booking.domain.Court;
import com.yk.booking.domain.TimeSlot;
import com.yk.booking.domain.User;
import com.yk.booking.domain.enumeration.BookingStatus;
import com.yk.booking.repository.BookingRepository;
import com.yk.booking.repository.CourtRepository;
import com.yk.booking.repository.TimeSlotRepository;
import com.yk.booking.repository.UserRepository;
import com.yk.booking.service.dto.BookingDTO;
import com.yk.booking.service.mapper.BookingMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.yk.booking.domain.Booking}.
 */
@Service
@Transactional
public class BookingService {

    private static final Logger LOG = LoggerFactory.getLogger(BookingService.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final TimeSlotRepository timeSlotRepository;

    private final CourtRepository courtRepository;

    private final UserRepository userRepository;

    @Autowired
    public BookingService(
        BookingRepository bookingRepository,
        BookingMapper bookingMapper,
        TimeSlotRepository timeSlotRepository,
        CourtRepository courtRepository,
        UserRepository userRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.timeSlotRepository = timeSlotRepository;
        this.courtRepository = courtRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a booking.
     *
     * @param bookingDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingDTO save(BookingDTO bookingDTO) {
        LOG.debug("Request to save Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);

        // Handle User reference
        if (bookingDTO.getUser() != null && bookingDTO.getUser().getId() != null) {
            User user = userRepository.findById(bookingDTO.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
            booking.setUser(user);
        }

        // Handle TimeSlot creation if provided
        if (bookingDTO.getTimeSlot() != null) {
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setStartTime(bookingDTO.getTimeSlot().getStartTime());
            timeSlot.setEndTime(bookingDTO.getTimeSlot().getEndTime());

            // Handle Court reference
            if (bookingDTO.getTimeSlot().getCourt() != null && bookingDTO.getTimeSlot().getCourt().getId() != null) {
                Court court = courtRepository
                    .findById(bookingDTO.getTimeSlot().getCourt().getId())
                    .orElseThrow(() -> new RuntimeException("Court not found"));
                timeSlot.setCourt(court);
            }

            // Save the TimeSlot first
            timeSlot = timeSlotRepository.save(timeSlot);
            booking.setTimeSlot(timeSlot);
        }

        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    /**
     * Update a booking.
     *
     * @param bookingDTO the entity to save.
     * @return the persisted entity.
     */
    public BookingDTO update(BookingDTO bookingDTO) {
        LOG.debug("Request to update Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    /**
     * Partially update a booking.
     *
     * @param bookingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BookingDTO> partialUpdate(BookingDTO bookingDTO) {
        LOG.debug("Request to partially update Booking : {}", bookingDTO);

        return bookingRepository
            .findById(bookingDTO.getId())
            .map(existingBooking -> {
                bookingMapper.partialUpdate(existingBooking, bookingDTO);

                return existingBooking;
            })
            .map(bookingRepository::save)
            .map(bookingMapper::toDto);
    }

    /**
     * Get all the bookings.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BookingDTO> findAll() {
        LOG.debug("Request to get all Bookings");
        return bookingRepository.findAll().stream().map(bookingMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one booking by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BookingDTO> findOne(Long id) {
        LOG.debug("Request to get Booking : {}", id);
        return bookingRepository.findById(id).map(bookingMapper::toDto);
    }

    /**
     * Approve a booking.
     *
     * @param id the id of the booking to approve.
     * @return the persisted entity.
     */
    public Optional<BookingDTO> approveBooking(Long id) {
        LOG.debug("Request to approve Booking : {}", id);
        return bookingRepository
            .findById(id)
            .map(booking -> {
                booking.setStatus(BookingStatus.APPROVED);
                return booking;
            })
            .map(bookingRepository::save)
            .map(bookingMapper::toDto);
    }

    /**
     * Reject a booking.
     *
     * @param id the id of the booking to reject.
     * @return the persisted entity.
     */
    public Optional<BookingDTO> rejectBooking(Long id) {
        LOG.debug("Request to reject Booking : {}", id);
        return bookingRepository
            .findById(id)
            .map(booking -> {
                booking.setStatus(BookingStatus.REJECTED);
                return booking;
            })
            .map(bookingRepository::save)
            .map(bookingMapper::toDto);
    }

    /**
     * Delete the booking by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Booking : {}", id);
        bookingRepository.deleteById(id);
    }
}
