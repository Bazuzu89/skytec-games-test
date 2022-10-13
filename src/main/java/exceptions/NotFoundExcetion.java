package exceptions;

public class NotFoundExcetion extends Throwable {
    private String message;

    public NotFoundExcetion(String message) {
        this.message = message;
    }
}
