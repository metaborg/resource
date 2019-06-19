package mb.resource;

import java.io.Serializable;

public interface ResourceRegistry {
    /**
     * Gets resource for given identifier..
     *
     * @param id Identifier to get resource for.
     * @return Resource for {@code id}.
     * @throws ResourceRuntimeException when retrieving resource failed unexpectedly.
     */
    Resource getResource(Serializable id);

    /**
     * Gets resource for given string representation of identifier.
     *
     * @param idStr String representation of identifier to get resource for.
     * @return Resource for {@code id}.
     * @throws ResourceRuntimeException when retrieving resource failed unexpectedly.
     */
    Resource getResource(String idStr);

    /**
     * Gets the qualifier this resource registry handles.
     *
     * @return Qualifier this resource registry handles resources for.
     */
    String qualifier();

    /**
     * Converts given identifier to its string representation.
     *
     * @param id Identifier.
     * @return String representation for given identifier.
     */
    String toStringRepresentation(Serializable id);
}
