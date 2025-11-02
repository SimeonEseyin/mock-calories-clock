package com.demo.mockclock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.Duration;
import java.util.Collection;
import java.util.Set;

@RestController
public class MetricController {

    private static final Logger log = LoggerFactory.getLogger(MetricController.class);

    private final MetricStore store = new MetricStore();
    private final MetricConfig config = new MetricConfig();

    /**
     * Get a metric by key, dynamically updating its seed based on elapsed time.
     */
    @GetMapping("/metric/{metricKey}")
    public MetricResponse getMetric(@PathVariable String metricKey) {
        // Try to get the metric from the store first
        MetricResponse current = store.getMetric(metricKey);

        // Only use the definition as default if it doesn't exist in the store
        if (current == null) {
            MetricDefinition def = config.getDefinition(metricKey);
            if (def != null) {
                current = new MetricResponse(
                        metricKey,
                        def.getName(),
                        def.getSeedValue(),
                        def.getGrowthPerSecond(),
                        Instant.now()
                );
            } else {
                throw new IllegalArgumentException("Unknown metric: " + metricKey);
            }
        }

        // update seed based on elapsed time
        Instant now = Instant.now();
        long elapsedSeconds = Duration.between(current.updatedAt(), now).getSeconds();
        long newSeedValue = current.seedValue() + current.growthPerSecond() * elapsedSeconds;

        MetricResponse updated = new MetricResponse(current.metricKey(), current.metricName(), newSeedValue, current.growthPerSecond(), now);
        store.updateMetric(metricKey, updated);

        log.info("GET /metric/{} -> {}", metricKey, updated);
        return updated;
    }

    /**
     * Return all available metric keys.
     */
    @GetMapping("/metrics")
    public Set<String> listMetricKeys() {
        return config.getAllDefinitions().keySet();
    }

    /**
     * Return all metric definitions (name, seed, growth rate)
     */
    @GetMapping("/metrics/definitions")
    public Collection<MetricDefinition> listMetricDefinitions() {
        return config.getAllDefinitions().values();
    }

    @PutMapping("/metric/{metricKey}")
    public MetricResponse updateMetric(
            @PathVariable String metricKey,
            @RequestBody MetricResponse req
    ){
        MetricResponse existing = store.getMetric(metricKey);
        if (existing == null) throw new IllegalArgumentException("Unknown metric: " + metricKey);

        Instant now = Instant.now();

        MetricResponse updated = new MetricResponse(
                metricKey,
                req.metricName(),
                req.seedValue(),
                req.growthPerSecond(),
                now
        );

        store.updateMetric(metricKey, updated);
        return updated;
    }

}
