package spring.jsample.mvc.exceptions;

public class ConcurrentModificationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConcurrentModificationException(String message) {
        super(message);
    }
}
