package mb.resource;

import java.io.Serializable;

public interface ResourceRegistry {
    /**
     * Gets the qualifier (corresponding to {@link ResourceKey#qualifier()}) this resource registry handles.
     *
     * @return Qualifier this resource registry handles resources for.
     */
    Serializable qualifier();

    /**
     * Gets resource for given identifier (corresponding to {@link ResourceKey#id()}).
     *
     * @param id Identifier to get resource for.
     * @return Resource for {@code id}.
     * @throws ResourceRuntimeException when retrieving resource failed unexpectedly.
     */
    Resource getResource(Serializable id);

    /**
     * Gets resource for given {@link ResourceKey#id()}.
     *
     * @param key Key to get resource for.
     * @return Resource for {@code key}.
     * @throws ResourceRuntimeException when given {@link ResourceKey#qualifier()} does not equate {@link #qualifier()}.
     * @throws ResourceRuntimeException when retrieving resource failed unexpectedly.
     */
    Resource getResource(ResourceKey key);
}
