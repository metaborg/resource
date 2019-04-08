package mb.resource;

public interface ResourceRegistry {
    /**
     * Gets resource for given key.
     *
     * @param key Key to get resource for.
     * @return Resource for {@code key}.
     * @throws ResourceRuntimeException when given {@code key} cannot be handled by this resource registry.
     */
    Resource getResource(ResourceKey key);
}
