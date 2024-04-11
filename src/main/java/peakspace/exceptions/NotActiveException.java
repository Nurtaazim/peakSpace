package peakspace.exceptions;

public class NotActiveException extends RuntimeException{
    public NotActiveException() {
    }

    public NotActiveException(String message) {
        super(message);
    }
}
