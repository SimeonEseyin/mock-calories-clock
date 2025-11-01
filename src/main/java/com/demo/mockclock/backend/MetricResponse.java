package com.demo.mockclock;

import java.time.Instant;

public record MetricResponse(
        String name,
        long seedValue,
        long growthPerSecond,
        Instant updatedAt
) {}
