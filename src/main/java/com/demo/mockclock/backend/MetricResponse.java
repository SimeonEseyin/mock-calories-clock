package com.demo.mockclock;

import java.time.Instant;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public record MetricResponse(
        String metricName,
        long seedValue,
        long growthPerSecond,
        Instant updatedAt
) {}
