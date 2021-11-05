package com.demo.flowchart.auth.result;

public class VerificationPasswordError extends AuthResult {

    public String message;

    public VerificationPasswordError(String message) {
        this.message = message;
    }
}
