package mb.resource;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * A readable resource.
 */
public interface ReadableResource extends Resource, AutoCloseable {

    /**
     * Gets whether the resource exists.
     *
     * @return {@code true} when the resource exists; otherwise, {@code false}.
     * @throws IOException An I/O exception occurred.
     */
    boolean exists() throws IOException;

    /**
     * Gets whether the resource is accessible and readable.
     *
     * @return {@code true} when the resource is accessible and readable; otherwise, {@code false}.
     * @throws IOException An I/O exception occurred.
     */
    boolean isReadable() throws IOException;

    /**
     * Gets the moment the resource was last modified.
     *
     * @return An {@link Instant} with the moment the resource was last modified.
     * @throws IOException An I/O exception occurred.
     */
    Instant getLastModifiedTime() throws IOException;

    /**
     * Gets the size of the content of the resource.
     *
     * @return The size of the content of the resource, in bytes.
     * @throws IOException An I/O exception occurred.
     */
    long getSize() throws IOException;

    /**
     * Opens the resource for reading.
     * <p>
     * Close the input stream when you are done with it.
     *
     * @return The input stream to read from.
     * @throws FileNotFoundException The resource does not exist.
     * @throws IOException           An I/O exception occurred.
     */
    InputStream openRead() throws IOException;

    /**
     * Reads the content of the resource as an array of bytes.
     *
     * @return An array with the read bytes.
     * @throws IOException An I/O exception occurred.
     */
    default byte[] readBytes() throws IOException {
        final InputStream inputStream = openRead();
        final byte[] buffer = new byte[4096];
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(buffer.length);
        int bytesRead;
        while((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    /**
     * Reads the content of the resource as a string.
     *
     * @param fromCharset The character set that the content is encoded with.
     * @return A string with the read data.
     * @throws IOException An I/O exception occurred.
     */
    default String readString(Charset fromCharset) throws IOException {
        return new String(readBytes(), fromCharset);
    }

    /**
     * Reads the content of the resource as a string using the default character set.
     *
     * @return A string with the read data.
     * @throws IOException An I/O exception occurred.
     */
    default String readString() throws IOException {
        return readString(StandardCharsets.UTF_8);
    }

    /**
     * Closes the resource.
     *
     * @throws IOException An I/O exception occurred.
     */
    @Override void close() throws IOException;

}
