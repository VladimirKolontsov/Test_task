package exceptions;

public class InvalidConnection extends RuntimeException {
    public InvalidConnection(String message) {
        super(message);
    }
}
