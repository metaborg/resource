package mb.resource.string;

import mb.resource.IORead;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringIORead implements IORead, Serializable {
    public final String str;
    public final Charset toBytesCharset;

    public StringIORead(String str) {
        this(str, StandardCharsets.UTF_8);
    }

    public StringIORead(String str, Charset toBytesCharset) {
        this.str = str;
        this.toBytesCharset = toBytesCharset;
    }

    @Override public boolean exists() {
        return true;
    }

    @Override public boolean isReadable() {
        return true;
    }

    @Override public long getSize() {
        return str.length() * 2; // 2 bytes per character.
    }

    @Override public InputStream newInputStream() {
        return new ByteArrayInputStream(str.getBytes(toBytesCharset));
    }

    @Override public byte[] readBytes() {
        return str.getBytes(toBytesCharset);
    }

    @Override public String readString(Charset charset) {
        return str; // Ignore charset, as this resource is already an encoded string.
    }
}
