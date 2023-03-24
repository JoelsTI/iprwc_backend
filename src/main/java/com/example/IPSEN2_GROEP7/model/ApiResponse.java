package com.example.IPSEN2_GROEP7.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse<Type> {
    private HttpStatus code;
    private Type payload;

    public ApiResponse(HttpStatus code, Type payload) {
        this.code = code;
        this.payload = payload;
    }

    public Type getPayload() {
        return payload;
    }

    public void setPayload(Type payload) {
        this.payload = payload;
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public ResponseEntity getResponse() {
        ApiResponseData apiResponseData = new ApiResponseData(this.getCode(), this.getPayload());

        return new ResponseEntity(apiResponseData, this.getCode());
    }
}
