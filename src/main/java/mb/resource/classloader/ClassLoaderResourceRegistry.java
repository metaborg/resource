package mb.resource.classloader;

import mb.resource.ResourceKey;
import mb.resource.ResourceKeyString;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;
import mb.resource.hierarchical.SegmentsPath;
import org.checkerframework.checker.nullness.qual.Nullable;

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
        return new SegmentsPath(qualifier, keyStr.getId());
    }

    @Override public ClassLoaderResource getResource(ResourceKey key) {
        if(!(key instanceof SegmentsPath)) {
            throw new ResourceRuntimeException(
                "Cannot get class loader resource for key '" + key + "'; it is not of type ClassLoaderResourcePath");
        }
        return new ClassLoaderResource(classLoader, (SegmentsPath)key);
    }

    @Override public ClassLoaderResource getResource(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        final SegmentsPath path = new SegmentsPath(qualifier, keyStr.getId());
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
