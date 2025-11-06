package com.yk.booking.service.scheduler;

import com.yk.booking.domain.Client;
import com.yk.booking.domain.ClientTier;
import com.yk.booking.repository.BookingRepository;
import com.yk.booking.repository.ClientRepository;
import com.yk.booking.repository.ClientTierRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserTierUpdateScheduler {

    private final Logger log = LoggerFactory.getLogger(UserTierUpdateScheduler.class);

    private final ClientRepository clientRepository;
    private final BookingRepository bookingRepository;
    private final ClientTierRepository clientTierRepository;

    public UserTierUpdateScheduler(
        ClientRepository clientRepository,
        BookingRepository bookingRepository,
        ClientTierRepository clientTierRepository
    ) {
        this.clientRepository = clientRepository;
        this.bookingRepository = bookingRepository;
        this.clientTierRepository = clientTierRepository;
    }

    // Run every 24 hours (86400000 ms)
    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void updateUserTiers() {
        log.info("Running scheduled task to update user tiers.");

        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            // Assuming a user has a client associated with them
            if (client.getUser() == null) {
                log.warn("Client {} has no associated user. Skipping tier update.", client.getId());
                continue;
            }

            // Count completed bookings for the user
            Long totalCompletedBookings = bookingRepository.countByUser(client.getUser());

            String tierName;
            if (totalCompletedBookings >= 21) {
                tierName = "PLATINUM";
            } else if (totalCompletedBookings >= 11) {
                tierName = "GOLD";
            } else if (totalCompletedBookings >= 6) {
                tierName = "IRON";
            } else {
                tierName = "LEAD";
            }

            ClientTier newClientTier = clientTierRepository.findByTierName(tierName).orElse(null);

            if (newClientTier != null && !newClientTier.equals(client.getClientTier())) {
                client.setClientTier(newClientTier);
                clientRepository.save(client);
                log.info(
                    "Updated client {} (user: {}) tier to {}. Total completed bookings: {}",
                    client.getId(),
                    client.getUser().getLogin(),
                    tierName,
                    totalCompletedBookings
                );
            }
        }
        log.info("Scheduled task to update user tiers completed.");
    }
}
