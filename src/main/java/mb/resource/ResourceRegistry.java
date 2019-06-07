package mb.resource;

import java.io.Serializable;

public interface ResourceRegistry {
    /**
     * Gets the qualifier (corresponding to {@link ResourceKey#getQualifier()}) this resource registry handles.
     *
     * @return Qualifier this resource registry handles resources for.
     */
    String qualifier();

    /**
     * Gets resource for given identifier (corresponding to {@link ResourceKey#getId()}).
     *
     * @param id Identifier to get resource for.
     * @return Resource for {@code id}.
     * @throws ResourceRuntimeException when retrieving resource failed unexpectedly.
     */
    Resource getResource(Serializable id);

    /**
     * Gets resource for given string representation of identifier (corresponding to {@link
     * ResourceKey#getIdStringRepresentation()}).
     *
     * @param id String representation of identifier to get resource for.
     * @return Resource for {@code id}.
     * @throws ResourceRuntimeException when retrieving resource failed unexpectedly.
     */
    Resource getResource(String id);

    /**
     * Gets resource for given {@link ResourceKey#getId()}.
     *
     * @param key Key to get resource for.
     * @return Resource for {@code key}.
     * @throws ResourceRuntimeException when given {@link ResourceKey#getQualifier()} does not equate {@link
     *                                  #qualifier()}.
     * @throws ResourceRuntimeException when retrieving resource failed unexpectedly.
     */
    default Resource getResource(ResourceKey key) {
        final String qualifier = key.getQualifier();
        final Serializable expectedQualifier = this.qualifier();
        if(!expectedQualifier.equals(qualifier)) {
            throw new ResourceRuntimeException(
                "Cannot get resource with key '" + key + "'; its qualifier '" + qualifier + "' is not '" + expectedQualifier + "'");
        }
        return getResource(key.getId());
    }
}
