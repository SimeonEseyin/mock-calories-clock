package com.demo.mockclock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MetricStore {

    private final File file = new File("metric_values.json");
    private final ObjectMapper mapper;
    private Map<String, MetricResponse> metrics = new HashMap<>();

    public MetricStore() {
        // ← create mapper WITH modules
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        load();
    }

    private void load() {
        if (!file.exists()) return;
        try {
            metrics = mapper.readValue(file, new TypeReference<Map<String, MetricResponse>>() {});
            System.out.println("MetricStore loaded " + metrics.size() + " metrics");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, metrics);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MetricResponse getMetric(String name) {
        return metrics.get(name);
    }

    public void updateMetric(String key, MetricResponse updated) {
        metrics.put(key, updated);
        save();
    }

    /** Delete a metric by key */
    public void deleteMetric(String key) {
        metrics.remove(key);
        save();
    }
}
