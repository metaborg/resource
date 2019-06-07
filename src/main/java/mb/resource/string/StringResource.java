package mb.resource.string;

import mb.resource.DefaultResourceKey;
import mb.resource.ReadableResource;
import mb.resource.ResourceKey;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class StringResource implements ReadableResource, Serializable {
    public static final String qualifier = "string";

    public final String str;
    public final ResourceKey key;

    public StringResource(String str, String id) {
        this.str = str;
        this.key = new DefaultResourceKey(qualifier, id);
    }

    @Override public ResourceKey getKey() {
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
        return str.length() * 2; // UTF-16 is 2 bytes per character.
    }

    @Override public InputStream newInputStream() {
        return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)); // Encode as UTF-8 bytes.
    }

    @Override public byte[] readBytes() {
        return str.getBytes(StandardCharsets.UTF_8); // Encode as UTF-8 bytes.
    }

    @Override public String readString(Charset fromBytesCharset) {
        return str; // Ignore fromBytesCharset, we do not need to decode from bytes.
    }

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final StringResource that = (StringResource) o;
        return key.equals(that.key);
    }

    @Override public int hashCode() {
        return key.hashCode();
    }

    @Override public String toString() {
        return key.toString();
    }
}
