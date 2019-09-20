package mb.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public interface WritableResource extends ReadableResource {
    boolean isWritable() throws IOException;

    void setLastModifiedTime(Instant time) throws IOException;

    OutputStream newOutputStream() throws IOException;

    default void writeBytes(byte[] bytes) throws IOException {
        try(final OutputStream outputStream = newOutputStream()) {
            outputStream.write(bytes);
            outputStream.flush();
        }
    }

    default void writeString(String string, Charset toBytesCharset) throws IOException {
        try(final OutputStream outputStream = newOutputStream()) {
            outputStream.write(string.getBytes(toBytesCharset));
            outputStream.flush();
        }
    }

    default void writeString(String string) throws IOException {
        writeString(string, StandardCharsets.UTF_8);
    }
}
