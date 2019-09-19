package mb.resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public interface ReadableResource extends Resource, AutoCloseable {

    boolean exists() throws IOException;

    boolean isReadable() throws IOException;

    Instant getLastModifiedTime() throws IOException;

    long getSize() throws IOException;

    default Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    InputStream newInputStream() throws IOException;

    default byte[] readBytes() throws IOException {
        final InputStream inputStream = newInputStream();
        final byte[] buffer = new byte[4096];
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(buffer.length);
        int bytesRead;
        while((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    default String readString(Charset fromBytesCharset) throws IOException {
        return new String(readBytes(), fromBytesCharset);
    }

    default String readString() throws IOException {
        return readString(getCharset());
    }

    @Override void close() throws IOException;
}

