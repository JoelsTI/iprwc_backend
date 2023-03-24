package com.example.IPSEN2_GROEP7.model;

import org.springframework.http.HttpStatus;

public class ApiResponseData<Type> {
    private HttpStatus code;
    private Type payload;

    public ApiResponseData(HttpStatus code, Type payload) {
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
}
