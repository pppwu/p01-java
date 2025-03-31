package com.pta.store;

import java.util.List;

public class CalculationResponse<T> {
    private String msg;
    private T value;

    public CalculationResponse(String msg, T value) {
        this.msg = msg;
        this.value = value;
    }

    public String getMsg() {
        return msg;
    }

    public T getValue() {
        return value;
    }
}
