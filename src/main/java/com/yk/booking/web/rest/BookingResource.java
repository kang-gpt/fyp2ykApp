package com.yk.booking.web.rest;

import com.yk.booking.domain.Booking;
import com.yk.booking.domain.Court;
import com.yk.booking.repository.BookingRepository;
import com.yk.booking.repository.CourtRepository;
import com.yk.booking.repository.SportRepository;
import com.yk.booking.repository.UserRepository;
import com.yk.booking.service.ClientService;
import com.yk.booking.domain.User;
import com.yk.booking.domain.Client;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import com.yk.booking.web.rest.errors.BadRequestAlertException;
import com.yk.booking.security.SecurityUtils;

/**
 * REST controller for managing {@link com.yk.booking.domain.Booking}.
 */
@RestController
@RequestMapping("/api/bookings")
@Transactional
public class BookingResource {

    private final Logger log = LoggerFactory.getLogger(BookingResource.class);

    private static final String ENTITY_NAME = "booking";

    private static final BigDecimal PICKLEBALL_PRICE_PER_HOUR = new BigDecimal("10.00");
    private static final BigDecimal BADMINTON_PRICE_PER_HOUR = new BigDecimal("12.00");
    private static final BigDecimal BASKETBALL_PRICE_PER_HOUR = new BigDecimal("15.00");
    private static final BigDecimal FUTSAL_PRICE_PER_HOUR = new BigDecimal("20.00");

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingRepository bookingRepository;
    private final ClientService clientService;
    private final CourtRepository courtRepository;
    private final SportRepository sportRepository;
    private final UserRepository userRepository;

    public BookingResource(BookingRepository bookingRepository, ClientService clientService, CourtRepository courtRepository, SportRepository sportRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.clientService = clientService;
        this.courtRepository = courtRepository;
        this.sportRepository = sportRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code POST  /bookings} : Create a new booking.
     *
     * @param booking the booking to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new booking, or with status {@code 400 (Bad Request)} if the booking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", booking);
        if (booking.getId() != null) {
            throw new BadRequestAlertException("A new booking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (bookingRepository.findByCourtAndStartTimeAndEndTime(booking.getCourt(), booking.getStartTime(), booking.getEndTime()).isPresent()) {
            throw new BadRequestAlertException("A booking already exists for this court and time slot", ENTITY_NAME, "duplicateBooking");
        }
        booking = bookingRepository.save(booking);



        clientService.findOne(booking.getUser().getId()).ifPresent(clientService::updateClientTier);
        return ResponseEntity.created(new URI("/api/bookings/" + booking.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, booking.getId().toString()))
            .body(booking);
    }

    /**
     * {@code PUT  /bookings/:id} : Updates an existing booking.
     *
     * @param id the id of the booking to save.
     * @param booking the booking to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated booking,
     * or with status {@code 400 (Bad Request)} if the booking is not valid,
     * or with status {@code 500 (Internal Server Error)} if the booking couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable(value = "id", required = false) final Long id, @RequestBody Booking booking)
        throws URISyntaxException {
        log.debug("REST request to update Booking : {}, {}", id, booking);
        if (booking.getId() == null) {
            throw new RuntimeException("Invalid id");
        }
        if (!Objects.equals(id, booking.getId())) {
            throw new RuntimeException("Invalid ID");
        }

        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Entity not found");
        }

        booking = bookingRepository.save(booking);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, booking.getId().toString()))
            .body(booking);
    }

    /**
     * {@code GET  /bookings} : get all the bookings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookings in body.
     */
    @GetMapping("")
    public List<Booking> getAllBookings() {
        log.debug("REST request to get all Bookings");
        return bookingRepository.findAll();
    }

    /**
     * {@code GET  /my-bookings} : get all the bookings for the currently logged-in user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookings in body.
     */
    @GetMapping("/my-bookings")
    public List<Booking> getMyBookings() {
        log.debug("REST request to get all Bookings for current user");
        String userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Current user login not found", "user", "usernotfound"));
        User user = userRepository.findOneByLogin(userLogin).orElseThrow(() -> new BadRequestAlertException("User not found", "user", "usernotfound"));
        return bookingRepository.findByUser(user);
    }

    /**
     * {@code GET  /bookings/:id} : get the "id" booking.
     *
     * @param id the id of the booking to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the booking, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable("id") Long id) {
        log.debug("REST request to get Booking : {}", id);
        Optional<Booking> booking = bookingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(booking);
    }

    /**
     * {@code GET  /bookings/by-court/{courtId}} : get all the bookings for a specific court and date.
     *
     * @param courtId the id of the court.
     * @param date the date to retrieve bookings for.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookings in body.
     */
    @GetMapping("/by-court/{courtId}")
    public List<Booking> getBookingsByCourtAndDate(
        @PathVariable("courtId") Long courtId,
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.debug("REST request to get Bookings by court and date : {}, {}", courtId, date);
        Court court = new Court();
        court.setId(courtId);
        Instant start = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        return bookingRepository.findAllByCourtAndStartTimeBetween(court, start, end);
    }

    /**
     * {@code DELETE  /bookings/:id} : delete the "id" booking.
     *
     * @param id the id of the booking to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") Long id) {
        log.debug("REST request to delete Booking : {}", id);
        bookingRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
