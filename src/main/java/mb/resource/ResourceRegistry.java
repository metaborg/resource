package mb.resource;

import java.io.Serializable;

public interface ResourceRegistry {
    /**
     * Gets the qualifier this resource registry handles.
     *
     * @return Qualifier this resource registry handles resources for.
     */
    String qualifier();

    /**
     * Gets resource for given identifier.
     *
     * @param id Identifier to get resource for.
     * @return The resource.
     * @throws ResourceRuntimeException when retrieving resource failed unexpectedly.
     */
    Resource getResource(Serializable id);

    /**
     * Gets resource key for given string representation of identifier.
     *
     * @param idStr String representation of the resource identifier.
     * @return The resource key.
     * @throws ResourceRuntimeException when building the resource key failed unexpectedly.
     */
    ResourceKey getResourceKey(String idStr);

    /**
     * Gets resource for given string representation of identifier.
     *
     * @param idStr String representation of the resource identifier.
     * @return The resource.
     * @throws ResourceRuntimeException when retrieving resource failed unexpectedly.
     */
    Resource getResource(String idStr);

    /**
     * Converts given identifier to its string representation.
     *
     * @param id Identifier.
     * @return String representation for given identifier.
     */
    String toStringRepresentation(Serializable id);

}
