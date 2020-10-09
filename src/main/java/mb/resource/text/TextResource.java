package mb.resource.text;

import mb.resource.DefaultResourceKey;
import mb.resource.ReadableResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * An in-memory read-only text resource.
 */
public class TextResource implements ReadableResource, Serializable {
    public final String text;
    public final DefaultResourceKey key;

    /**
     * Initializes a new instance of the {@link TextResource} class.
     *
     * @param text The content of the resource.
     * @param id   The unique identifier of the resource.
     */
    /* package private */ TextResource(String text, String id) {
        this.text = text;
        this.key = new DefaultResourceKey(TextResourceRegistry.qualifier, id);
    }

    @Override public void close() {
        // Nothing to close.
    }


    @Override public DefaultResourceKey getKey() {
        return key;
    }

    @Override public boolean exists() {
        return true; // Always exists.
    }

    @Override public boolean isReadable() {
        return true; // Always readable.
    }

    @Override public Instant getLastModifiedTime() {
        return Instant.EPOCH; // Never modified.
    }

    @Override public long getSize() {
        return text.length() * 2; // UTF-16 is 2 bytes per character.
    }

    @Override
    public InputStream openRead() {
        return new ByteArrayInputStream(readBytes());
    }

    @Override
    public byte[] readBytes() {
        return text.getBytes(StandardCharsets.UTF_8); // Encode as UTF-8 bytes.
    }

    @Override
    public String readString(Charset fromCharset) {
        return text; // Ignore the character set, we do not need to decode from bytes.
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final TextResource that = (TextResource) o;
        return key.equals(that.key);
    }

    @Override public int hashCode() {
        return key.hashCode();
    }

    @Override public String toString() {
        return key.toString();
    }
}
