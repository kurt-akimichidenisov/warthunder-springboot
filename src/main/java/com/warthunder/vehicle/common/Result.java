package com.warthunder.vehicle.common;

import lombok.Data;

@Data
public class Result {
    private Integer resultCode;   // 1=成功, 0=失败
    private String resultMessage;
    private Object resultData;

    public static Result success() {
        Result result = new Result();
        result.resultCode = 1;
        result.resultMessage = "success";
        return result;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.resultCode = 1;
        result.resultMessage = "success";
        result.resultData = object;
        return result;
    }

    public static Result error(String message) {
        Result result = new Result();
        result.resultCode = 0;
        result.resultMessage = message;
        return result;
    }
}