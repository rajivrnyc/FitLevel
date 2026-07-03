package edu.northeastern.a6atyourservice_team12;

public class NetworkException extends Exception {

    public enum ErrorType {
        nointernet,
        timeout,
        citynotfound,
        invalid_response,
        Servererror,
    }

    private ErrorType errorType;

    public NetworkException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public NetworkException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public String getUserFriendlyMessage() {
        switch (errorType) {
            case nointernet:
                return "No internet connection. Please check your network.";
            case timeout:
                return "Request timed out. Please try again.";
            case citynotfound:
                return "City not found. Please check the spelling.";
            case invalid_response:
                return "Invalid response from server. Please try again.";
            case Servererror:
                return "Server error. Please try again later.";
            default:
                return "An error occurred. Please try again.";
        }
    }
}