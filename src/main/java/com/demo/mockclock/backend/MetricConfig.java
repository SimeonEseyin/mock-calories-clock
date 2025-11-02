package com.demo.mockclock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MetricConfig {

    private final File configFile = new File("metric_definitions.json");
    private final ObjectMapper mapper = new ObjectMapper();
    private Map<String, MetricDefinition> definitions = new HashMap<>();

    public MetricConfig() {
        load(); //load from metric_definitions.json
    }

    private void load() {
        try {
            ClassPathResource resource = new ClassPathResource("metric_definitions.json");
            Map<String, MetricDefinition> map = mapper.readValue(
                    resource.getInputStream(),
                    mapper.getTypeFactory()
                            .constructMapType(Map.class, String.class, MetricDefinition.class)
            );
            definitions.putAll(map);
            System.out.println("metrics loaded keys=" + definitions.keySet());
        } catch (Exception e) {
            System.out.println("no metric_definitions.json on classpath");
        }
    }


    public MetricDefinition getDefinition(String metricKey) {
        return definitions.get(metricKey);
    }

    public Map<String, MetricDefinition> getAllDefinitions() {
        return definitions;
    }
}
