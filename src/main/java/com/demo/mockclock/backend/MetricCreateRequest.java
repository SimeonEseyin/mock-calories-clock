package com.demo.mockclock;

import java.time.Instant;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public record MetricCreateRequest(
        String metricKey,
        String metricName,
        long seedValue,
        long growthPerSecond
) {}

