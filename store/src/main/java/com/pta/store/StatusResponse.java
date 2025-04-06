package com.pta.store;

import java.util.Map;

public class StatusResponse {
    private Integer remainingAttempts;
    private Map<String, Double> probabilities;
    private Map<String, Integer> prizes;

    public StatusResponse(
            Integer remainingAttempts,
            Map<String, Double> probabilities,
            Map<String, Integer> prizes
    ) {
        this.remainingAttempts = remainingAttempts;
        this.probabilities = probabilities;
        this.prizes = prizes;
    }

    public Integer getRemainingAttempts() {
        return remainingAttempts;
    }

    public Map<String, Double> getProbabilities() {
        return probabilities;
    }

    public Map<String, Integer> getPrizes() {
        return prizes;
    }
}
