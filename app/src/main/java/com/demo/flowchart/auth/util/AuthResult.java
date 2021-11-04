package com.demo.flowchart.auth.util;

public class AuthResult {

    public static class PasswordError extends AuthResult {

    }

    public static class EmailError extends AuthResult {

    }

    public static class AuthError extends AuthResult {

    }

    public static class AuthSuccess extends AuthResult {

    }

    public static class PasswordsAreNotEqual extends AuthResult {

    }

    public static class PasswordIsShorterThanSixCharacters extends AuthResult {

    }

    public static class EmailNotValid extends AuthResult {

    }
}
