package com.demo.mockclock;

import java.time.Instant;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public record MetricUpdateRequest(
        String metricName,
        Long seedValue,
        Long growthPerSecond
) {}

