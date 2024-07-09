package mb.resource.classloader;

import mb.resource.ResourceRuntimeException;
import mb.resource.fs.FSResource;
import mb.resource.hierarchical.HierarchicalResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URISyntaxException;
import java.net.URL;

public class FSResourceClassLoaderToNativeResolver implements ClassLoaderToNativeResolver {
    @Override public @Nullable HierarchicalResource toNativeFile(URL url) {
        return toNativeResource(url);
    }

    @Override public @Nullable HierarchicalResource toNativeDirectory(URL url) {
        return toNativeResource(url);
    }

    private @Nullable HierarchicalResource toNativeResource(URL url) {
        if("file".equals(url.getProtocol())) {
            try {
                return new FSResource(url.toURI());
            } catch(URISyntaxException e) {
                throw new ResourceRuntimeException("Could not get local filesystem URI (with 'file' protocol); conversion of URL '" + url + "' to an URI failed", e);
            }
        }
        return null;
    }
}
