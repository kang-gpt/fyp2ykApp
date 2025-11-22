package com.yk.booking.service;

import com.yk.booking.domain.Booking;
import com.yk.booking.domain.enumeration.BookingStatus;
import com.yk.booking.repository.BookingRepository;
import com.yk.booking.service.dto.RevenueDTO;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RevenueService {

    private static final Logger LOG = LoggerFactory.getLogger(RevenueService.class);

    private final BookingRepository bookingRepository;

    public RevenueService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public List<RevenueDTO> getDailyRevenue() {
        LOG.debug("Request to get daily revenue");
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Instant startOfWeek = today.with(DayOfWeek.MONDAY).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfWeek = today.with(DayOfWeek.SUNDAY).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        List<Booking> bookings = bookingRepository.findAllByStatusAndBookingDateBetween(BookingStatus.APPROVED, startOfWeek, endOfWeek);
        LOG.debug("Found {} approved bookings for daily revenue", bookings.size());

        if (bookings.isEmpty()) {
            LOG.debug("No approved bookings found, returning empty list");
            return java.util.Collections.emptyList();
        }

        Map<String, BigDecimal> dailyRevenue = bookings
            .stream()
            .filter(b -> b.getPayment() != null && b.getPayment().getAmount() != null)
            .collect(
                Collectors.groupingBy(
                    b -> LocalDate.ofInstant(b.getBookingDate(), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("E")),
                    Collectors.mapping(b -> b.getPayment().getAmount(), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                )
            );

        return dailyRevenue.entrySet().stream().map(entry -> new RevenueDTO(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RevenueDTO> getWeeklyRevenue() {
        LOG.debug("Request to get weekly revenue");
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Instant startOfMonth = today.withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfMonth = today.plusMonths(1).withDayOfMonth(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        List<Booking> bookings = bookingRepository.findAllByStatusAndBookingDateBetween(BookingStatus.APPROVED, startOfMonth, endOfMonth);
        LOG.debug("Found {} approved bookings", bookings.size());

        List<Booking> bookingsWithPayment = bookings
            .stream()
            .filter(b -> {
                boolean hasPayment = b.getPayment() != null && b.getPayment().getAmount() != null;
                if (!hasPayment) {
                    LOG.warn("Booking ID {} has no payment information", b.getId());
                }
                return hasPayment;
            })
            .collect(Collectors.toList());
        LOG.debug("{} bookings have payment information", bookingsWithPayment.size());

        Map<String, BigDecimal> weeklyRevenue = bookingsWithPayment
            .stream()
            .collect(
                Collectors.groupingBy(
                    b ->
                        "Week " +
                        LocalDate.ofInstant(b.getBookingDate(), ZoneOffset.UTC).get(
                            java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear()
                        ),
                    Collectors.mapping(b -> b.getPayment().getAmount(), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                )
            );

        return weeklyRevenue
            .entrySet()
            .stream()
            .map(entry -> new RevenueDTO(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RevenueDTO> getMonthlyRevenue() {
        LOG.debug("Request to get monthly revenue");
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Instant startOfYear = today.withDayOfYear(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfYear = today.plusYears(1).withDayOfYear(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        List<Booking> bookings = bookingRepository.findAllByStatusAndBookingDateBetween(BookingStatus.APPROVED, startOfYear, endOfYear);
        LOG.debug("Found {} approved bookings for monthly revenue", bookings.size());

        if (bookings.isEmpty()) {
            LOG.debug("No approved bookings found, returning empty list");
            return java.util.Collections.emptyList();
        }

        Map<String, BigDecimal> monthlyRevenue = bookings
            .stream()
            .filter(b -> b.getPayment() != null && b.getPayment().getAmount() != null)
            .collect(
                Collectors.groupingBy(
                    b -> LocalDate.ofInstant(b.getBookingDate(), ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("MMM")),
                    Collectors.mapping(b -> b.getPayment().getAmount(), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                )
            );

        return monthlyRevenue
            .entrySet()
            .stream()
            .map(entry -> new RevenueDTO(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalDailyRevenue() {
        LOG.debug("Request to get total daily revenue for today");
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Instant startOfDay = today.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        return bookingRepository
            .findTotalRevenueByStatusAndBookingDateBetween(BookingStatus.APPROVED, startOfDay, endOfDay)
            .orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalWeeklyRevenue() {
        LOG.debug("Request to get total weekly revenue for current week");
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = startOfWeek.plusWeeks(1);

        Instant startInstant = startOfWeek.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endInstant = endOfWeek.atStartOfDay().toInstant(ZoneOffset.UTC);

        return bookingRepository
            .findTotalRevenueByStatusAndBookingDateBetween(BookingStatus.APPROVED, startInstant, endInstant)
            .orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalMonthlyRevenue() {
        LOG.debug("Request to get total monthly revenue for current month");
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.firstDayOfNextMonth());

        Instant startInstant = startOfMonth.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endInstant = endOfMonth.atStartOfDay().toInstant(ZoneOffset.UTC);

        return bookingRepository
            .findTotalRevenueByStatusAndBookingDateBetween(BookingStatus.APPROVED, startInstant, endInstant)
            .orElse(BigDecimal.ZERO);
    }
}
