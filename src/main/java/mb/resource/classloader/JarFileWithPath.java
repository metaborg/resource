package mb.resource.classloader;

import mb.resource.fs.FSResource;

public class JarFileWithPath {
    public final FSResource file;
    public final String path;

    public JarFileWithPath(FSResource file, String path) {
        this.file = file;
        this.path = path;
    }
}
