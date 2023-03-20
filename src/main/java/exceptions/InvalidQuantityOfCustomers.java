package exceptions;

public class InvalidQuantityOfCustomers extends RuntimeException {
    public InvalidQuantityOfCustomers(String message) {
        super(message);
    }
}
