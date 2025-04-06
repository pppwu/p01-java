package com.pta.store;

import java.util.Map;

public class SpinResultResponse {
    private String prize;
    private Boolean stockAvailable;
    private String message;
    private int remainingAttempts;
    private Map<String, Integer> remainingPrizes;

    public SpinResultResponse(String prize, Boolean stockAvailable, String message, int remainingAttempts, Map<String, Integer> remainingPrizes) {
        this.prize = prize;
        this.stockAvailable = stockAvailable;
        this.message = message;
        this.remainingAttempts = remainingAttempts;
        this.remainingPrizes = remainingPrizes;
    }

    public String getPrize() {
        return prize;
    }

    public Boolean getStockAvailable() {
        return stockAvailable;
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
