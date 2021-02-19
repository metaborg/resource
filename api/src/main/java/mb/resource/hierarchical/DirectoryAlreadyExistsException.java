package mb.resource.hierarchical;

import java.io.IOException;

public class DirectoryAlreadyExistsException extends IOException {
    public DirectoryAlreadyExistsException() {
        super();
    }

    public DirectoryAlreadyExistsException(String message) {
        super(message);
    }

    public DirectoryAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectoryAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
