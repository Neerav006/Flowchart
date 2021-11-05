package com.demo.flowchart.auth.result;

public class PasswordError extends AuthResult {

    public String message;

    public PasswordError(String message) {
        this.message = message;
    }
}
