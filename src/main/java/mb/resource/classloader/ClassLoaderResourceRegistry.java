package mb.resource.classloader;

import mb.resource.Resource;
import mb.resource.ResourceKey;
import mb.resource.ResourceKeyString;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;
import mb.resource.hierarchical.SegmentsPath;
import mb.resource.util.SeparatorUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.util.Objects;

public class ClassLoaderResourceRegistry implements ResourceRegistry {
    private static final String defaultQualifier = "classloader-resource";

    private final String qualifier;
    private final ClassLoader classLoader;


    public ClassLoaderResourceRegistry(String qualifier, ClassLoader classLoader) {
        this.qualifier = qualifier;
        this.classLoader = classLoader;
    }

    public ClassLoaderResourceRegistry(ClassLoader classLoader) {
        this(defaultQualifier, classLoader);
    }

    public ClassLoaderResourceRegistry() {
        this(defaultQualifier, ClassLoaderResourceRegistry.class.getClassLoader());
    }


    @Override public String qualifier() {
        return qualifier;
    }


    @Override public SegmentsPath getResourceKey(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        return getPath(keyStr.getId());
    }

    @Override public ClassLoaderResource getResource(ResourceKey key) {
        if(!(key instanceof SegmentsPath)) {
            throw new ResourceRuntimeException("Cannot get class loader resource for key '" + key + "'; it is not of type SegmentsPath");
        }
        return getResource((SegmentsPath)key);
    }

    @Override public ClassLoaderResource getResource(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        return getResource(keyStr.getId());
    }


    @Override public @Nullable File toLocalFile(ResourceKey key) {
        if(!(key instanceof SegmentsPath)) {
            throw new ResourceRuntimeException("Cannot get local file for key '" + key + "'; it is not of type SegmentsPath");
        }
        return toLocalFile(getResource((SegmentsPath)key));
    }

    @Override public @Nullable File toLocalFile(Resource resource) {
        if(!(resource instanceof ClassLoaderResource)) {
            throw new ResourceRuntimeException("Cannot get local file for resource '" + resource + "'; it is not of type ClassLoaderResource");
        }
        return ((ClassLoaderResource)resource).asLocalFile();
    }


    public String getPathIdentifierForClass(Class<?> clazz) {
        return clazz.getCanonicalName().replace(".", SeparatorUtil.unixSeparator) + ".class";
    }


    public SegmentsPath getPath(String path) {
        return new SegmentsPath(qualifier, path);
    }

    public SegmentsPath getPath(Class<?> clazz) {
        return new SegmentsPath(qualifier, getPathIdentifierForClass(clazz));
    }


    public ClassLoaderResource getResource(String path) {
        return new ClassLoaderResource(classLoader, qualifier, path);
    }

    public ClassLoaderResource getResource(Class<?> clazz) {
        return new ClassLoaderResource(classLoader, qualifier, getPathIdentifierForClass(clazz));
    }

    public ClassLoaderResource getResource(SegmentsPath path) {
        return new ClassLoaderResource(classLoader, path);
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ClassLoaderResourceRegistry that = (ClassLoaderResourceRegistry)o;
        return qualifier.equals(that.qualifier) && classLoader.equals(that.classLoader);
    }

    @Override public int hashCode() {
        return Objects.hash(qualifier, classLoader);
    }

    @Override public String toString() {
        return "ClassLoaderResourceRegistry{" +
            "qualifier='" + qualifier + '\'' +
            ", classLoader=" + classLoader +
            '}';
    }
}
