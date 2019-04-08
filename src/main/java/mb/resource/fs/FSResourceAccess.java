package mb.resource.fs;

public interface FSResourceAccess {
    void read(FSResource resource);

    void write(FSResource resource);
}
