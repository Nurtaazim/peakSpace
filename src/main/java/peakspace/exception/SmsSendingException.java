package peakspace.exception;

public class SmsSendingException extends RuntimeException {
    public SmsSendingException() {}

    public SmsSendingException(String message) {
        super(message);
    }
}
