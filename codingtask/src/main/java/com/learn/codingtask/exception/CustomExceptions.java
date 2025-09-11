package com.learn.codingtask.exception;

public class CustomExceptions {

    // Employee not found
    public static class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException(String userName) {
            super("Employee not found with username: " + userName);
        }
    }

    // Username already exists
    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String userName) {
            super("Username already exists: " + userName);
        }
    }

    // Generic invalid role exception
    public static class InvalidRoleException extends RuntimeException {
        public InvalidRoleException(String role) {
            super("Invalid role: " + role + ". Allowed roles are ADMIN or USER.");
        }
    }
}