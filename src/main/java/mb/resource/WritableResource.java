package mb.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.time.Instant;


/**
 * A writable resource.
 */
public interface WritableResource extends ReadableResource {

    /**
     * Gets whether the resource is accessible and writable.
     *
     * @return {@code true} when the resource is accessible and writable;
     * otherwise, {@code false}.
     * @throws IOException An I/O exception occurred.
     */
    boolean isWritable() throws IOException;

    /**
     * Sets the moment the resource was last modified.
     *
     * @param moment An {@link Instant} with the moment the resource was last modified.
     * @throws IOException An I/O exception occurred.
     */
    void setLastModifiedTime(Instant moment) throws IOException;

    /**
     * Creates a new instance of the resource and opens it for writing.
     *
     * This method throws an error when the file exists.
     *
     * @return The output stream to write to.
     * @throws FileAlreadyExistsException The file already exists.
     * @throws IOException An I/O exception occurred.
     */
    OutputStream createNew() throws IOException;

    /**
     * Opens the resource for writing.
     *
     * This method throws an error when the files does not exist.
     *
     * Close the output stream when you are done with it.
     *
     * @return The output stream to write to.
     * @throws FileNotFoundException The resource does not exist.
     * @throws IOException An I/O exception occurred.
     */
    OutputStream openWrite() throws IOException;

    /**
     * Opens the resource for writing; or creates the resource if it does not exist.
     *
     * This method overwrites any existing content in the file.
     *
     * Close the output stream when you are done with it.
     *
     * @return The output stream to write to.
     * @throws IOException An I/O exception occurred.
     */
    OutputStream openWriteOrCreate() throws IOException;

    /**
     * Writes the content of the resource as an array of bytes.
     *
     * @param bytes An array with the bytes to write.
     * @throws IOException An I/O exception occurred.
     */
    default void writeBytes(byte[] bytes) throws IOException {
        try(final OutputStream outputStream = openWrite()) {
            outputStream.write(bytes);
            outputStream.flush();
        }
    }

    /**
     * Writes the content of the resource as a string.
     *
     * @param string A string with the data to write.
     * @param toCharset The character set that the content is encoded with.
     * @throws IOException An I/O exception occurred.
     */
    default void writeString(String string, Charset toCharset) throws IOException {
        try(final OutputStream outputStream = openWrite()) {
            outputStream.write(string.getBytes(toCharset));
            outputStream.flush();
        }
    }

    /**
     * Writes the content of the resource as a string
     * using the default character set.
     *
     * @param string A string with the data to write.
     * @throws IOException An I/O exception occurred.
     */
    default void writeString(String string) throws IOException {
        writeString(string, StandardCharsets.UTF_8);
    }
}
