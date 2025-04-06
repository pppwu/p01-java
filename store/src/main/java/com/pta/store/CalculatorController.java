package com.pta.store;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    private final Stack<Double> history = new Stack<>();
    private final Stack<Double> redoStack = new Stack<>();

    @PostMapping("/calculate")
    public ResponseEntity<CalculationResponse> calculate(@RequestBody String expression) {

        try {
            double result = evaluate(expression);
            history.push(result);
            redoStack.clear();
            return ResponseEntity.ok(new CalculationResponse("Calculate succeed.", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new CalculationResponse(e.getMessage(), null));
        }
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
            return ResponseEntity.noContent().build();
        }

        double lastRedo = redoStack.pop();
        history.push(lastRedo);

        return ResponseEntity.ok(new CalculationResponse<>("Redo succeeded.", lastRedo));
    }

    private double evaluate(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i));
                    i++;
                }
                i--;
                numbers.push(Double.parseDouble(sb.toString()));
            }

            else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operations.isEmpty() && precedence(ch) <= precedence(operations.peek())) {
                    evaluateOp(numbers, operations);
                }
                operations.push(ch);
            }
        }

        while (!operations.isEmpty()) {
            evaluateOp(numbers, operations);
        }

        double num = BigDecimal.valueOf(numbers.pop()).setScale(9, RoundingMode.HALF_UP).doubleValue();
        return num % 1 == 0 ? (int) num : num;
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private void evaluateOp(Stack<Double> numbers, Stack<Character> operations) {
        if (numbers.size() < 2) return ;

        double b = numbers.pop();
        double a = numbers.pop();
        char op = operations.pop();

        double result = 0;
        switch (op) {
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
            case '*':
                result = a * b;
                break;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                result = a / b;
                break;
            default:
                throw new IllegalArgumentException("Invalid operator: " + op);
        }
        numbers.push(result);

    }
}
