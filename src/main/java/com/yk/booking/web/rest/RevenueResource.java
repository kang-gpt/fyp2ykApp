package com.yk.booking.web.rest;

import com.yk.booking.service.RevenueService;
import com.yk.booking.service.dto.RevenueDTO;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/revenue")
public class RevenueResource {

    private static final Logger LOG = LoggerFactory.getLogger(RevenueResource.class);

    private final RevenueService revenueService;

    public RevenueResource(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping
    public ResponseEntity<List<RevenueDTO>> getRevenue(@RequestParam String period) {
        LOG.debug("REST request to get revenue for period: {}", period);
        try {
            List<RevenueDTO> revenueData;
            switch (period) {
                case "weekly":
                    LOG.debug("Fetching weekly revenue");
                    revenueData = revenueService.getWeeklyRevenue();
                    break;
                case "monthly":
                    LOG.debug("Fetching monthly revenue");
                    revenueData = revenueService.getMonthlyRevenue();
                    break;
                case "daily":
                default:
                    LOG.debug("Fetching daily revenue");
                    revenueData = revenueService.getDailyRevenue();
                    break;
            }
            LOG.debug("Returning {} revenue records", revenueData.size());
            return ResponseEntity.ok(revenueData);
        } catch (Exception e) {
            LOG.error("Error fetching revenue for period {}: {}", period, e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalRevenue(@RequestParam String period) {
        LOG.debug("REST request to get total revenue for period: {}", period);
        try {
            BigDecimal total;
            switch (period) {
                case "weekly":
                    LOG.debug("Fetching total weekly revenue");
                    total = revenueService.getTotalWeeklyRevenue();
                    break;
                case "monthly":
                    LOG.debug("Fetching total monthly revenue");
                    total = revenueService.getTotalMonthlyRevenue();
                    break;
                case "daily":
                default:
                    LOG.debug("Fetching total daily revenue");
                    total = revenueService.getTotalDailyRevenue();
                    break;
            }
            LOG.debug("Returning total revenue: {}", total);
            return ResponseEntity.ok(total);
        } catch (Exception e) {
            LOG.error("Error fetching total revenue for period {}: {}", period, e.getMessage(), e);
            throw e;
        }
    }
}
