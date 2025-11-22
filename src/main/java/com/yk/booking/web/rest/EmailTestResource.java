package com.yk.booking.web.rest;

import com.yk.booking.domain.Booking;
import com.yk.booking.repository.BookingRepository;
import com.yk.booking.service.MailService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for testing email functionality.
 */
@RestController
@RequestMapping("/api/test")
public class EmailTestResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailTestResource.class);

    private final MailService mailService;
    private final BookingRepository bookingRepository;

    public EmailTestResource(MailService mailService, BookingRepository bookingRepository) {
        this.mailService = mailService;
        this.bookingRepository = bookingRepository;
    }

    /**
     * GET /test/email/simple : Test simple email sending
     */
    @GetMapping("/email/simple")
    public ResponseEntity<Map<String, String>> testSimpleEmail(@RequestParam String to) {
        Map<String, String> response = new HashMap<>();
        try {
            LOG.info("Testing simple email to: {}", to);
            mailService.sendEmail(
                to,
                "Test Email from Yk App",
                "This is a test email. If you receive this, email is working!",
                false,
                true
            );
            response.put("status", "success");
            response.put("message", "Email sent to " + to + ". Check your inbox and server logs.");
            LOG.info("Simple email test completed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error sending test email", e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("error", e.getClass().getName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * GET /test/email/booking/:id : Test booking email for a specific booking ID
     */
    @GetMapping("/email/booking/{id}")
    public ResponseEntity<Map<String, Object>> testBookingEmail(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            LOG.info("Testing booking email for booking ID: {}", id);

            Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

            // Log booking details
            response.put("bookingId", booking.getId());
            response.put("bookingStatus", booking.getStatus());

            if (booking.getUser() == null) {
                response.put("error", "Booking has no user associated");
                return ResponseEntity.badRequest().body(response);
            }

            response.put("userId", booking.getUser().getId());
            response.put("userLogin", booking.getUser().getLogin());
            response.put("userEmail", booking.getUser().getEmail());
            response.put("userLangKey", booking.getUser().getLangKey());

            if (booking.getUser().getEmail() == null || booking.getUser().getEmail().trim().isEmpty()) {
                response.put("error", "User has no email address");
                return ResponseEntity.badRequest().body(response);
            }

            // Force load relationships
            if (booking.getTimeSlot() != null) {
                booking.getTimeSlot().getStartTime();
                booking.getTimeSlot().getEndTime();
                response.put("timeSlotId", booking.getTimeSlot().getId());

                if (booking.getTimeSlot().getCourt() != null) {
                    booking.getTimeSlot().getCourt().getName();
                    response.put("courtName", booking.getTimeSlot().getCourt().getName());

                    if (booking.getTimeSlot().getCourt().getSport() != null) {
                        booking.getTimeSlot().getCourt().getSport().getName();
                        response.put("sportName", booking.getTimeSlot().getCourt().getSport().getName());
                    }
                }
            } else {
                response.put("warning", "Booking has no TimeSlot");
            }

            // Send the email
            LOG.info("Attempting to send booking confirmation email...");
            mailService.sendBookingConfirmationEmail(booking);

            response.put("status", "success");
            response.put(
                "message",
                "Booking confirmation email triggered. Check server logs and user inbox: " + booking.getUser().getEmail()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            LOG.error("Error testing booking email", e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("error", e.getClass().getName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * GET /test/email/config : Check email configuration
     */
    @GetMapping("/email/config")
    public ResponseEntity<Map<String, String>> checkEmailConfig() {
        Map<String, String> response = new HashMap<>();

        response.put("status", "Email configuration check");
        response.put("note", "Check application logs for actual SMTP settings");
        response.put("instruction", "Use /test/email/simple?to=youremail@example.com to test");

        return ResponseEntity.ok(response);
    }
}
