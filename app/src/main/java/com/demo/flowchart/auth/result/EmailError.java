package com.demo.flowchart.auth.result;

public class EmailError extends AuthResult {

    public String message;

    public EmailError(String message) {
        this.message = message;
    }
}
