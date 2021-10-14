package mb.resource.classloader;

import mb.resource.hierarchical.HierarchicalResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URL;

public interface ClassLoaderToNativeResolver {
    @Nullable HierarchicalResource toNativeFile(URL url);

    @Nullable HierarchicalResource toNativeDirectory(URL url);
}
