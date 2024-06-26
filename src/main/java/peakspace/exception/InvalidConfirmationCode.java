package peakspace.exception;

public class InvalidConfirmationCode extends RuntimeException {

    public InvalidConfirmationCode(String message) {
        super(message);
    }

}
