package mb.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.Instant;

public interface WritableResource extends ReadableResource {
    boolean isWritable() throws IOException;

    void setLastModifiedTime(Instant time) throws IOException;


    OutputStream newOutputStream() throws IOException;

    void writeBytes(byte[] bytes) throws IOException;

    void writeString(String string, Charset toBytesCharset) throws IOException;
}
