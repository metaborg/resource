package mb.resource.classloader;

import mb.resource.ReadableResource;
import mb.resource.ResourceRuntimeException;
import mb.resource.fs.FSResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URISyntaxException;
import java.net.URL;

public class FSResourceClassLoaderToNativeResolver implements ClassLoaderToNativeResolver {
    @Override public @Nullable ReadableResource toNativeResource(URL url) {
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
