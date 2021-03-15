package mb.resource.classloader;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URL;

public class NoopClassLoaderUrlResolver implements ClassLoaderUrlResolver {
    @Override public @Nullable URL resolve(URL url) {
        return null;
    }
}
