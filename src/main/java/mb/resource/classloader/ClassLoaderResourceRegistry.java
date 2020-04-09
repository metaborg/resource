package mb.resource.classloader;

import mb.resource.QualifiedResourceKeyString;
import mb.resource.ResourceKeyString;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;

import java.io.Serializable;
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


    @Override public ClassLoaderResource getResource(Serializable id) {
        if(!(id instanceof ClassLoaderResourcePath)) {
            throw new ResourceRuntimeException(
                "Cannot get class loader resource with ID '" + id + "'; the ID is not of type ClassLoaderResourcePath");
        }
        final ClassLoaderResourcePath path = (ClassLoaderResourcePath)id;
        return new ClassLoaderResource(classLoader, path);
    }


    @Override public ClassLoaderResourcePath getResourceKey(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        return new ClassLoaderResourcePath(qualifier, keyStr.getId());
    }

    @Override public ClassLoaderResource getResource(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        final ClassLoaderResourcePath path = new ClassLoaderResourcePath(qualifier, keyStr.getId());
        return new ClassLoaderResource(classLoader, path);
    }


    @Override public QualifiedResourceKeyString toResourceKeyString(Serializable id) {
        if(!(id instanceof ClassLoaderResourcePath)) {
            throw new ResourceRuntimeException(
                "Cannot convert identifier '" + id + "' to its string representation; it is not of type ClassLoaderResourcePath");
        }
        final ClassLoaderResourcePath path = (ClassLoaderResourcePath)id;
        return QualifiedResourceKeyString.of(path.getQualifier(), path.asString());
    }

    @Override public String toString(Serializable id) {
        if(!(id instanceof ClassLoaderResourcePath)) {
            throw new ResourceRuntimeException(
                "Cannot convert identifier '" + id + "' to its string representation; it is not of type ClassLoaderResourcePath");
        }
        final ClassLoaderResourcePath path = (ClassLoaderResourcePath)id;
        return QualifiedResourceKeyString.toString(path.getQualifier(), path.asString());
    }


    @Override public boolean equals(Object o) {
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
