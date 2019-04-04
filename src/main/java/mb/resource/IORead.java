package mb.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public interface IORead {
    boolean exists() throws IOException;

    boolean isReadable() throws IOException;

    long getSize() throws IOException;


    InputStream newInputStream() throws IOException;

    byte[] readBytes() throws IOException;

    String readString(Charset charset) throws IOException;
}
