package com.demo.mockclock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
        MetricDefinition def = config.getDefinition(metricKey);
        if (def == null) {
            throw new IllegalArgumentException("Unknown metric: " + metricKey);
        }

        MetricResponse current = store.getMetric(metricKey);
        if (current == null) {
            current = new MetricResponse(def.getName(), def.getSeedValue(), def.getGrowthPerSecond(), Instant.now());
        }

        // update seed based on elapsed time
        Instant now = Instant.now();
        long elapsedSeconds = Duration.between(current.updatedAt(), now).getSeconds();
        long newSeedValue = current.seedValue() + current.growthPerSecond() * elapsedSeconds;

        MetricResponse updated = new MetricResponse(def.getName(), newSeedValue, def.getGrowthPerSecond(), now);
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
}
