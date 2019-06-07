package mb.resource;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.ResourcePath;

public interface ResourceService {
    /**
     * Gets a resource for given {@code key}.
     *
     * @param key Key to get resource for.
     * @return Resource for {@code key}.
     * @throws ResourceRuntimeException when given {@code key} cannot be handled by this resource service, for example
     *                                  when qualifier of given {@code key} does not correspond to a {@link
     *                                  ResourceRegistry} in this resource service.
     */
    Resource getResource(ResourceKey key);

    /**
     * Gets a readable resource for given {@code key}.
     *
     * @param key Key to get resource for.
     * @return Readable resource for {@code key}.
     * @throws ResourceRuntimeException when given {@code key} cannot be handled by this resource service, for example
     *                                  when qualifier of given {@code key} does not correspond to a {@link
     *                                  ResourceRegistry} in this resource service.
     * @throws ResourceRuntimeException when resource for given {@code key} does not implement {@link
     *                                  ReadableResource}.
     */
    ReadableResource getReadableResource(ResourceKey key);

    /**
     * Gets a writable resource for given {@code key}.
     *
     * @param key Key to get writable resource for.
     * @return Writable resource for {@code key}.
     * @throws ResourceRuntimeException when given {@code key} cannot be handled by this resource service, for example
     *                                  when qualifier of given {@code key} does not correspond to a {@link
     *                                  ResourceRegistry} in this resource service.
     * @throws ResourceRuntimeException when resource for given {@code key} does not implement {@link
     *                                  WritableResource}.
     */
    WritableResource getWritableResource(ResourceKey key);

    /**
     * Gets a hierarchical resource for given {@code path}.
     *
     * @param path Path to get hierarchical resource for.
     * @return Hierarchical resource for {@code path}.
     * @throws ResourceRuntimeException when given {@code path} cannot be handled by this resource service, for example
     *                                  when qualifier of given {@code path} does not correspond to a {@link
     *                                  ResourceRegistry} in this resource service.
     * @throws ResourceRuntimeException when resource for given {@code path} does not implement {@link
     *                                  HierarchicalResource}.
     */
    HierarchicalResource getHierarchicalResource(ResourcePath path);
}
