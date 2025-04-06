package com.pta.store;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/fortune-wheel")
public class FortuneWheelController {

    private final Map<String, Integer> prizes = new HashMap<>();
    private final Map<String, Double> probabilities = new HashMap<>();
    private final Random random = new Random();
    private int remainingAttempts = 3;

    public FortuneWheelController() {
        resetPrizes();
        setProbabilities();
    }

    private void resetPrizes() {
        prizes.put("A", 10);
        prizes.put("B", 5);
        prizes.put("C", 2);
        prizes.put("None", 0);
    }

    private void setProbabilities() {
        probabilities.put("A", 0.3);
        probabilities.put("B", 0.2);
        probabilities.put("C", 0.1);
        probabilities.put("None", 0.4);
    }

    @GetMapping("/status")
    public ResponseEntity<StatusResponse> getStatus() {
        return ResponseEntity.ok(
                new StatusResponse(
                        remainingAttempts,
                        new HashMap<>(probabilities),
                        new HashMap<>(prizes)
                )
        );
    }

    @PostMapping("/set-attempts")
    public ResponseEntity<GameStatusResponse> setAttempts(@RequestBody Map<String, Integer> request) {
        int attempts = request.get("attempts");
        if (attempts < 1) {
            return ResponseEntity.badRequest().body(
                    new GameStatusResponse(
                            "Invalid attempt count. It must be greater then zero.",
                            remainingAttempts,
                            new HashMap<>(prizes)
                    )
            );
        }
        remainingAttempts = attempts;
        return ResponseEntity.ok(
                new GameStatusResponse(
                        "Attempts set successfully.",
                        remainingAttempts,
                        new HashMap<>(prizes)
                )
        );
    }


    @PostMapping("/reset")
    public ResponseEntity<GameStatusResponse> reset() {
        resetPrizes();
        remainingAttempts = 3;
        return ResponseEntity.ok(
                new GameStatusResponse(
                        "Game reset successfully.",
                        remainingAttempts,
                        new HashMap<>(prizes)
                )
        );
    }


    @PostMapping("/spin")
    public ResponseEntity<SpinResultResponse> spin() {
        if (remainingAttempts <= 0) {
            return ResponseEntity.badRequest().body(
                    new SpinResultResponse(
                            "None",
                            false,
                            "No more attempts left. Please reset to play again.",
                            remainingAttempts,
                            new HashMap<>(prizes)
                    )
            );
        }

        Map<String, Object> result = drawing();
        remainingAttempts--;

        String prize = (String) result.get("prize");

        return ResponseEntity.ok(
                new SpinResultResponse(
                        prize,
                        (Boolean) result.get("stockAvailable"),
                        (String) result.get("message"),
                        remainingAttempts,
                        new HashMap<>(prizes)
                )
        );
    }

    private int getPrizeId(String prize) {
        int id = 1;
        for (String key : probabilities.keySet()) {
            if (key.equals(prize)) {
                return id;
            }
            id++;
        }
        return 0; // None
    }

    private Map<String, Object> drawing() {
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;

        int totalRemainingPrizes = prizes.values().stream().mapToInt(Integer::intValue).sum();
        if (totalRemainingPrizes == 0) {
            return Map.of(
                    "prize", "None",
                    "stockAvailable", false,
                    "message", "All prizes are out of stock. Better luck next time!"
            );
        }

        for (Map.Entry<String, Double> entry : probabilities.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue <= cumulativeProbability) {
                String prize = entry.getKey();

                if (prize.equals("None")) {

                    return Map.of(
                            "prize", prize,
                            "stockAvailable", false,
                            "message", "You didn't win a prize. Better luck next time!"
                    );
                }

                if (prizes.get(prize) == 0) {

                    return Map.of(
                            "prize", prize,
                            "stockAvailable", false,
                            "message", "No more prize " + prize + " available, better luck next time!"
                    );
                }

                prizes.put(prize, prizes.get(prize) - 1);
                return Map.of(
                        "prize", prize,
                        "stockAvailable", true,
                        "message", "Congratulations! You won " + prize
                );
            }
        }
        return Map.of("prize", "None", "message", "Unexpected error occurred.");
    }
}
