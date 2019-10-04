package mb.resource.text;

import mb.resource.ResourceKey;
import mb.resource.WritableResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.time.Instant;


/**
 * A read/write in-memory text resource.
 */
public final class MutableTextResource implements WritableResource, Serializable {

    private String name;
    // While 'content' is null, the resource does not exist.
    @Nullable
    private String content;
    private Instant lastModificationTime;

    @Override
    public boolean isWritable() throws IOException {
        return content != null;
    }

    @Override
    public void setLastModifiedTime(Instant moment) throws IOException {
        this.lastModificationTime = moment;
    }

    @Override
    public OutputStream createNew() throws IOException {
        if (exists()) throw new FileAlreadyExistsException("The text resource already exists: " + name);
        this.content = "";
        PrintStream ps;
//new OnCloseByteArrayOutputStream()
        return null;
    }

    @Override
    public OutputStream openWrite() throws IOException {
        return null;
    }

    @Override
    public OutputStream openWriteOrCreate() throws IOException {
        return null;
    }

    @Override
    public boolean exists() throws IOException {
        return false;
    }

    @Override
    public boolean isReadable() throws IOException {
        return false;
    }

    @Override
    public Instant getLastModifiedTime() throws IOException {
        return null;
    }

    @Override
    public long getSize() throws IOException {
        return 0;
    }

    @Override
    public InputStream openRead() throws IOException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public ResourceKey getKey() {
        return null;
    }

}
