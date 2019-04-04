package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

public interface ResourceRegistry {
    /**
     * Gets resource for given key.
     *
     * @param key Key to get resource for.
     * @return Resource for {@code key}, or null if such a resource does not exist.
     * @throws ResourceRuntimeException when given {@code key} cannot be handled by this resource registry.
     */
    @Nullable Resource getResource(ResourceKey key);
}
