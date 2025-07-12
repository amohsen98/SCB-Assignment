package com.scb.application.scheduler;

import com.scb.application.service.DailySummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailySummaryScheduler {

    private final DailySummaryService dailySummaryService;

    @Scheduled(cron = "0 0 9 * * ?")
    public void generateDailySummary() {
        log.info("Starting scheduled task to generate daily summary");
        LocalDate today = LocalDate.now();
        
        try {
            dailySummaryService.generateDailySummary(today);
            log.info("Successfully generated daily summary for {}", today);
        } catch (Exception e) {
            log.error("Error generating daily summary: {}", e.getMessage(), e);
        }
    }
}