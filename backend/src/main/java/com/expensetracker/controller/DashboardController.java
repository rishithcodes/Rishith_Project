package com.expensetracker.controller;

import com.expensetracker.dto.DashboardResponse;
import com.expensetracker.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

/**
 * Dashboard endpoint - returns all summary data needed for the dashboard page.
 */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired private DashboardService dashboardService;

    /** GET /api/dashboard?year=2024 */
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(
            Authentication auth,
            @RequestParam(defaultValue = "0") int year) {
        int selectedYear = year > 0 ? year : LocalDate.now().getYear();
        return ResponseEntity.ok(dashboardService.getDashboard(auth.getName(), selectedYear));
    }
}
