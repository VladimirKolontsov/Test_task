package exceptions;

public class InvalidExpenses extends RuntimeException {
    public InvalidExpenses(String message) {
        super(message);
    }
}
