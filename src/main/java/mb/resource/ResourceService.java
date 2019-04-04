package mb.resource;

import mb.resource.path.Path;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ResourceService {
    /**
     * Gets resource for given key.
     *
     * @param key Key to get resource for.
     * @return Resource for given key, or null if such a resource does not exist.
     * @throws ResourceRuntimeException when there is no {@link ResourceRegistry} for qualifier of given {@code key}.
     */
    @Nullable Resource getResource(ResourceKey key);

    /**
     * Gets resource for given path.
     *
     * @param path Path to get resource for.
     * @return Resource for given path, or null if such a resource does not exist.
     * @throws ResourceRuntimeException when there is no {@link ResourceRegistry} for qualifier of given {@code path}.
     * @throws ResourceRuntimeException when {@link ResourceRegistry} for qualifier of given {@code path} returns a resource that does not implement {@link TreeResource}.
     */
    @Nullable TreeResource getResource(Path path);
}
