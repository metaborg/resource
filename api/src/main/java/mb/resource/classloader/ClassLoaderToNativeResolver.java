package mb.resource.classloader;

import mb.resource.ReadableResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URL;

public interface ClassLoaderToNativeResolver {
    @Nullable ReadableResource toNativeResource(URL url);
}
