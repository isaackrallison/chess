package dataaccess;

// Custom exception class extending RuntimeException
public class ExistsException extends RuntimeException {
    public ExistsException(String message) {
        super(message);  // Pass the message to the parent Exception class
    }
}

