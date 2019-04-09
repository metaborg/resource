package mb.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.Instant;

public interface ReadableResource extends Resource {
    boolean exists() throws IOException;

    boolean isReadable() throws IOException;

    Instant getLastModifiedTime() throws IOException;

    long getSize() throws IOException;


    InputStream newInputStream() throws IOException;

    byte[] readBytes() throws IOException;

    String readString(Charset fromBytesCharset) throws IOException;
}

