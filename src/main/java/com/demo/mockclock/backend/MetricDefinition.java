package com.demo.mockclock;

public class MetricDefinition {
    private String name;
    private long seedValue;
    private long growthPerSecond;

    // Jackson requires a no-args constructor
    public MetricDefinition() {}

    public MetricDefinition(String name, long seedValue, long growthPerSecond) {
        this.name = name;
        this.seedValue = seedValue;
        this.growthPerSecond = growthPerSecond;
    }

    public String getName() { return name; }
    public long getSeedValue() { return seedValue; }
    public long getGrowthPerSecond() { return growthPerSecond; }
}
