package exceptions;

public class NotFoundException extends Throwable {
    private String message;

    public NotFoundException(String message) {
        this.message = message;
    }
}
