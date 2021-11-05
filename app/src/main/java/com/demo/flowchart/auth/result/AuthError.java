package com.demo.flowchart.auth.result;

public class AuthError extends AuthResult {

    public String message;

    public AuthError(String message) {
        this.message = message;
    }
}
