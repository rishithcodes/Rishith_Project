package com.expensetracker.dto;
import java.time.LocalDateTime;
import java.util.List;

public class ApiError {
    private int status;
    private String message;
    private List<String> errors;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(int status, String message) { this.status = status; this.message = message; }
    public ApiError(int status, String message, List<String> errors) { this.status = status; this.message = message; this.errors = errors; }
    public int getStatus() { return status; }
    public String getMessage() { return message; }
    public List<String> getErrors() { return errors; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
