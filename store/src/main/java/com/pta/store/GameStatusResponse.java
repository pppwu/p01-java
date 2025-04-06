package com.pta.store;

import java.util.Map;

public class GameStatusResponse {
    private String message;
    private int remainingAttempts;
    private Map<String, Integer> remainingPrizes;

    public GameStatusResponse(String message, int remainingAttempts, Map<String, Integer> remainingPrizes) {
        this.message = message;
        this.remainingAttempts = remainingAttempts;
        this.remainingPrizes = remainingPrizes;
    }

    public String getMessage() {
        return message;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public Map<String, Integer> getRemainingPrizes() {
        return remainingPrizes;
    }
}
