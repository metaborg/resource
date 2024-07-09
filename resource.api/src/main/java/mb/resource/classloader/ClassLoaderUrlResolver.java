package mb.resource.classloader;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URL;

public interface ClassLoaderUrlResolver {
    /**
     * Attempt to resolve given {@code url} into one that {@link ClassLoaderResource#getLocations} understands. Returns
     * {@code null} if the {@code url} cannot be resolved. Note that any special characters in the returned URL must
     * be encoded using standard URL encoding (e.g. ' ' -> '%20') in order to properly support all paths.
     *
     * @param url {@link URL} to be resolved.
     * @return Resolved {@link URL} or {@code null}.
     */
    @Nullable URL resolve(URL url);
}
