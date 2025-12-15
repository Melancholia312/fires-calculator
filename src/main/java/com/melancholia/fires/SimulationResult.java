package com.melancholia.fires;

import java.util.ArrayList;
import java.util.List;

public class SimulationResult {
    private List<Double> timePoints;
    private List<Double> fireRiskLevels;
    private List<Double> temperatureValues;
    private List<Double> oxygenConcentration;
    private List<Double> smokeConcentration;

    private Double maxFireRisk;
    private Double timeToCritical;
    private String riskLevel;
    private List<String> recommendations;

    private Double maxTemperature;
    private Double minOxygen;

    public SimulationResult() {
        this.timePoints = new ArrayList<>();
        this.fireRiskLevels = new ArrayList<>();
        this.temperatureValues = new ArrayList<>();
        this.oxygenConcentration = new ArrayList<>();
        this.smokeConcentration = new ArrayList<>();
        this.recommendations = new ArrayList<>();
    }

    // Геттеры и сеттеры
    public List<Double> getTimePoints() { return timePoints; }
    public void setTimePoints(List<Double> timePoints) { this.timePoints = timePoints; }

    public List<Double> getFireRiskLevels() { return fireRiskLevels; }
    public void setFireRiskLevels(List<Double> fireRiskLevels) { this.fireRiskLevels = fireRiskLevels; }

    public List<Double> getTemperatureValues() { return temperatureValues; }
    public void setTemperatureValues(List<Double> temperatureValues) { this.temperatureValues = temperatureValues; }

    public List<Double> getOxygenConcentration() { return oxygenConcentration; }
    public void setOxygenConcentration(List<Double> oxygenConcentration) { this.oxygenConcentration = oxygenConcentration; }

    public List<Double> getSmokeConcentration() { return smokeConcentration; }
    public void setSmokeConcentration(List<Double> smokeConcentration) { this.smokeConcentration = smokeConcentration; }

    public Double getMaxFireRisk() { return maxFireRisk; }
    public void setMaxFireRisk(Double maxFireRisk) { this.maxFireRisk = maxFireRisk; }

    public Double getTimeToCritical() { return timeToCritical; }
    public void setTimeToCritical(Double timeToCritical) { this.timeToCritical = timeToCritical; }

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public void setMinOxygen(Double minOxygen) {
        this.minOxygen = minOxygen;
    }

    public Double getMaxTemperature() {
        if (maxTemperature == null && temperatureValues != null && !temperatureValues.isEmpty()) {
            maxTemperature = temperatureValues.stream()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .orElse(0.0);
        }
        return maxTemperature;
    }

    public Double getMinOxygen() {
        if (minOxygen == null && oxygenConcentration != null && !oxygenConcentration.isEmpty()) {
            minOxygen = oxygenConcentration.stream()
                    .mapToDouble(Double::doubleValue)
                    .min()
                    .orElse(21.0);
        }
        return minOxygen;
    }

}