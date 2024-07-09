package mb.resource;

public class ResourceRuntimeException extends RuntimeException {
    public ResourceRuntimeException() {
        super();
    }

    public ResourceRuntimeException(String message) {
        super(message);
    }

    public ResourceRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceRuntimeException(Throwable cause) {
        super(cause);
    }
}
