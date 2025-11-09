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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RevenueService {

    private final BookingRepository bookingRepository;

    public RevenueService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Transactional(readOnly = true)
    public List<RevenueDTO> getDailyRevenue() {
        List<Booking> bookings = bookingRepository.findAllByStatus(BookingStatus.APPROVED);
        Map<String, BigDecimal> dailyRevenue = bookings
            .stream()
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
        List<Booking> bookings = bookingRepository.findAllByStatus(BookingStatus.APPROVED);
        Map<String, BigDecimal> weeklyRevenue = bookings
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
        List<Booking> bookings = bookingRepository.findAllByStatus(BookingStatus.APPROVED);
        Map<String, BigDecimal> monthlyRevenue = bookings
            .stream()
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
}
