package com.demo.mockclock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;

@RestController
public class MetricController {

    private static final Logger log = LoggerFactory.getLogger(MetricController.class);

    @GetMapping("/metric/calories_us_daily")
    public MetricResponse getMetric() {
        // Base value and growth rate
        long seedValue = 604_800_000_000L;   // starting point
        long growthPerSecond = 7_000_000L;   // units per second
        Instant updatedAt = Instant.now();   // current timestamp

        // Construct typed response
        MetricResponse response = new MetricResponse (
                "US Calories Burned Today",
                seedValue,
                growthPerSecond,
                updatedAt
        );
        // log request and response for debugging
        log.info("GET /metric/calories_us_daily -> {}", response);
        return response;
    }
}
