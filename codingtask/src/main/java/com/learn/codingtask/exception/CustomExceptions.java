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

    public static class NoExistingLeaveRequestException extends RuntimeException {
        public NoExistingLeaveRequestException() {
            super("No pending Leave requests");
        }
    }

    public static class NoLeaveRequestForEmployeeException extends RuntimeException {
        public NoLeaveRequestForEmployeeException(String userName) {
            super("No leave request found for employee: " + userName);
        }
    }

    public static class LeaveNotFoundException extends RuntimeException {
        public LeaveNotFoundException(Long leaveId) {
            super("Leave request with ID " + leaveId + " not found");
        }
    }


    // Invalid status
    public static class InvalidStatusException extends RuntimeException {
        public InvalidStatusException(String status) {
            super("Invalid status: " + status + ". Allowed values are APPROVED or REJECTED.");
        }
    }

    //  Unauthorized action (not admin)
    public static class UnauthorizedActionException extends RuntimeException {
        public UnauthorizedActionException(String userName) {
            super("User '" + userName + "' is not authorized to approve/reject leave requests");
        }
    }
    public static class UnauthorizedException extends RuntimeException{
        public UnauthorizedException(String message){
          super("Unauthorised:" +message) ;
        }
    }

    public static class EmailAlreadyExistsException extends RuntimeException{
        public EmailAlreadyExistsException(String email){
            super("email: "+email+" already registered");
        }
    }
    public static class InactiveProfileException extends RuntimeException{
        public InactiveProfileException(String message){
            super(message);
        }
    }

    public static class InvalidCredentialsException extends RuntimeException{
        public InvalidCredentialsException(String message){
            super(message);
        }
    }
}