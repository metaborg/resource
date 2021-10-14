package mb.resource.classloader;

import mb.resource.ReadableResource;
import mb.resource.fs.FSResource;
import mb.resource.hierarchical.HierarchicalResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class ClassLoaderResourceLocations<R extends HierarchicalResource> implements Serializable {
    public final ArrayList<R> directories;
    public final ArrayList<JarFileWithPath<R>> jarFiles;
    public final ArrayList<URL> unrecognizedUrls;

    public ClassLoaderResourceLocations(
        ArrayList<R> directories,
        ArrayList<JarFileWithPath<R>> jarFiles,
        ArrayList<URL> unrecognizedUrls
    ) {
        this.directories = directories;
        this.jarFiles = jarFiles;
        this.unrecognizedUrls = unrecognizedUrls;
    }

    public ClassLoaderResourceLocations() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ClassLoaderResourceLocations<?> that = (ClassLoaderResourceLocations<?>)o;
        if(!directories.equals(that.directories)) return false;
        if(!jarFiles.equals(that.jarFiles)) return false;
        return unrecognizedUrls.equals(that.unrecognizedUrls);
    }

    @Override public int hashCode() {
        int result = directories.hashCode();
        result = 31 * result + jarFiles.hashCode();
        result = 31 * result + unrecognizedUrls.hashCode();
        return result;
    }

    @Override public String toString() {
        return "ClassLoaderResourceLocations{" +
            "directories=" + directories +
            ", jarFiles=" + jarFiles +
            ", unrecognizedUrls=" + unrecognizedUrls +
            '}';
    }
}
