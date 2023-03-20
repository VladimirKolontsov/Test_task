package exceptions;

public class InvalidSurname extends RuntimeException {
    public InvalidSurname(String message) {
        super(message);
    }
}
