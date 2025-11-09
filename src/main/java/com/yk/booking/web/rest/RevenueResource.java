package com.yk.booking.web.rest;

import com.yk.booking.service.RevenueService;
import com.yk.booking.service.dto.RevenueDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/revenue")
public class RevenueResource {

    private final RevenueService revenueService;

    public RevenueResource(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @GetMapping
    public ResponseEntity<List<RevenueDTO>> getRevenue(@RequestParam String period) {
        List<RevenueDTO> revenueData;
        switch (period) {
            case "weekly":
                revenueData = revenueService.getWeeklyRevenue();
                break;
            case "monthly":
                revenueData = revenueService.getMonthlyRevenue();
                break;
            case "daily":
            default:
                revenueData = revenueService.getDailyRevenue();
                break;
        }
        return ResponseEntity.ok(revenueData);
    }
}
