package mb.resource.classloader;

import mb.resource.fs.FSResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class JarFileWithPath implements Serializable {
    public final FSResource file;
    public final String path;

    public JarFileWithPath(FSResource file, String path) {
        this.file = file;
        this.path = path;
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final JarFileWithPath that = (JarFileWithPath)o;
        return file.equals(that.file) && path.equals(that.path);
    }

    @Override public int hashCode() {
        return Objects.hash(file, path);
    }

    @Override public String toString() {
        return "JarFileWithPath{" +
            "file=" + file +
            ", path='" + path + '\'' +
            '}';
    }
}
