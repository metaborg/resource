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
    public static final String defaultQualifier = "classloader-resource";
    public static final ClassLoaderUrlResolver defaultUrlResolver = new NoopClassLoaderUrlResolver();
    public static final ClassLoaderToNativeResolver defaultToNativeResolver = new FSResourceClassLoaderToNativeResolver();

    private final String qualifier;
    private final ClassLoader classLoader;
    private final ClassLoaderUrlResolver urlResolver;
    private final ClassLoaderToNativeResolver toNativeResolver;


    public ClassLoaderResourceRegistry(
        String qualifier,
        ClassLoader classLoader,
        ClassLoaderUrlResolver urlResolver,
        ClassLoaderToNativeResolver toNativeResolver
    ) {
        this.qualifier = qualifier;
        this.classLoader = classLoader;
        this.urlResolver = urlResolver;
        this.toNativeResolver = toNativeResolver;
    }

    public ClassLoaderResourceRegistry(String qualifier, ClassLoader classLoader, ClassLoaderUrlResolver urlResolver) {
        this(qualifier, classLoader, urlResolver, defaultToNativeResolver);
    }

    public ClassLoaderResourceRegistry(String qualifier, ClassLoader classLoader, ClassLoaderToNativeResolver toNativeResolver) {
        this(qualifier, classLoader, defaultUrlResolver, toNativeResolver);
    }

    public ClassLoaderResourceRegistry(String qualifier, ClassLoader classLoader) {
        this(qualifier, classLoader, defaultUrlResolver, defaultToNativeResolver);
    }

    public ClassLoaderResourceRegistry(ClassLoader classLoader, ClassLoaderUrlResolver urlResolver) {
        this(defaultQualifier, classLoader, urlResolver, defaultToNativeResolver);
    }

    public ClassLoaderResourceRegistry(ClassLoader classLoader, ClassLoaderToNativeResolver toNativeResolver) {
        this(defaultQualifier, classLoader, defaultUrlResolver, toNativeResolver);
    }

    public ClassLoaderResourceRegistry(ClassLoader classLoader) {
        this(classLoader, defaultUrlResolver);
    }

    public ClassLoaderResourceRegistry() {
        this(ClassLoaderResourceRegistry.class.getClassLoader());
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
        return new ClassLoaderResource(classLoader, urlResolver, toNativeResolver, path, qualifier);
    }

    public ClassLoaderResource getResource(Class<?> clazz) {
        return new ClassLoaderResource(classLoader, urlResolver, toNativeResolver, getPathIdentifierForClass(clazz), qualifier);
    }

    public ClassLoaderResource getResource(SegmentsPath path) {
        return new ClassLoaderResource(classLoader, urlResolver, toNativeResolver, path);
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
