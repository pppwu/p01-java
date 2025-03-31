package com.pta.store;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Stack;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    private final Stack<Double> history = new Stack<>();
    private final Stack<Double> redoStack = new Stack<>();

    @PostMapping("/calculate")
    public ResponseEntity<CalculationResponse> calculate(@RequestParam double a, @RequestParam double b, @RequestParam String operator) {
        double result;
        String msg;
        switch (operator) {
            case "add":
                result = a + b;
                break;
            case "subtract":
                result = a - b;
                break;
            case "multiply":
                result = a * b;
                break;
            case "divide":
                if ( b == 0) return ResponseEntity.badRequest().body(new CalculationResponse("can't divide by zero.", null));
                result = a / b;
                break;
            default:
                return ResponseEntity.badRequest().body(new CalculationResponse("Invalid operator.", null));
        }
        history.push(result);
        redoStack.clear();
        return ResponseEntity.ok(new CalculationResponse("Calculate succeed.", result));
    }

    @GetMapping("/history")
    public ResponseEntity<CalculationResponse> history() {
        if (history.isEmpty()) {
            return ResponseEntity.badRequest().body(new CalculationResponse("History is empty.", null));
        }
        return ResponseEntity.ok(new CalculationResponse("History is existing", history));
    }

    @GetMapping("/history/undo")
    public ResponseEntity<CalculationResponse<Double>> undo() {
        if (history.isEmpty()) {
            return ResponseEntity.badRequest().body(new CalculationResponse<>("History is empty.", null));
        }

        double lastResult = history.pop();
        redoStack.push(lastResult);

        String message = history.isEmpty() ? "History is now empty after undo." : "Undo succeeded.";
        Double newTop = history.isEmpty() ? null : history.peek();

        return ResponseEntity.ok(new CalculationResponse<>(message, newTop));
    }

    @GetMapping("/history/redo")
    public ResponseEntity<CalculationResponse<Double>> redo() {

        if (redoStack.isEmpty()) {
            return ResponseEntity.noContent().build();  // 無可重做的內容時返回 204 No Content
        }

        double lastRedo = redoStack.pop();
        history.push(lastRedo);

        return ResponseEntity.ok(new CalculationResponse<>("Redo succeeded.", lastRedo));
    }
}
