package exceptions;

public class InvalidProductOrTimes extends RuntimeException {
    public InvalidProductOrTimes(String message) {
        super(message);
    }
}
