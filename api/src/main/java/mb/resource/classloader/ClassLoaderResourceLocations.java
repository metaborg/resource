package mb.resource.classloader;

import mb.resource.fs.FSResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class ClassLoaderResourceLocations implements Serializable {
    public final ArrayList<FSResource> directories;
    public final ArrayList<JarFileWithPath> jarFiles;

    public ClassLoaderResourceLocations(ArrayList<FSResource> directories, ArrayList<JarFileWithPath> jarFiles) {
        this.directories = directories;
        this.jarFiles = jarFiles;
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ClassLoaderResourceLocations that = (ClassLoaderResourceLocations)o;
        return directories.equals(that.directories) && jarFiles.equals(that.jarFiles);
    }

    @Override public int hashCode() {
        return Objects.hash(directories, jarFiles);
    }

    @Override public String toString() {
        return "ClassLoaderResourceLocations{" +
            "directories=" + directories +
            ", jarFiles=" + jarFiles +
            '}';
    }
}
