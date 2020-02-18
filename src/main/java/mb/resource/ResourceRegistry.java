package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
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
     * @throws ResourceRuntimeException when {@code id} cannot be handled by this resource registry.
     */
    Resource getResource(Serializable id);

    /**
     * Gets resource key for given string representation of identifier.
     *
     * @param keyStr String representation of the resource identifier.
     * @return The resource key.
     * @throws ResourceRuntimeException when {@code keyStr} cannot be converted into an identifier.
     * @throws ResourceRuntimeException when {@code keyStr} has a qualifier which is not equal to {@link #qualifier()}.
     */
    ResourceKey getResourceKey(ResourceKeyString keyStr);

    /**
     * Gets resource for given string representation of identifier.
     *
     * @param keyStr String representation of the resource identifier.
     * @return The resource.
     * @throws ResourceRuntimeException when {@code idStr} cannot be converted into a resource.
     * @throws ResourceRuntimeException when {@code keyStr} has a qualifier which is not equal to {@link #qualifier()}.
     */
    Resource getResource(ResourceKeyString keyStr);

    /**
     * Converts given identifier to a {@link QualifiedResourceKeyString qualified resource key string}.
     *
     * @param id Identifier to convert.
     * @return {@link QualifiedResourceKeyString Qualified resource key string} for given identifier.
     * @throws ResourceRuntimeException when {@code id} cannot be handled by this resource registry.
     * @throws ResourceRuntimeException when {@code id} cannot be converted.
     */
    QualifiedResourceKeyString toResourceKeyString(Serializable id);

    /**
     * Converts given identifier to its string representation, which can be parsed back into a resource key with {@code
     * resourceRegistry.getResourceKey(QualifiedResourceKeyString.parse(str))}.
     *
     * @param id Identifier.
     * @return String representation for given identifier.
     * @throws ResourceRuntimeException when {@code id} cannot be handled by this resource registry.
     * @throws ResourceRuntimeException when {@code id} cannot be converted.
     */
    String toString(Serializable id);

    /**
     * Attempts to get a local file handle for given identifier.
     *
     * @param id Identifier to get a local file handle for.
     * @return Local file handle, or null if given identifier does not reside on the local file system.
     * @throws ResourceRuntimeException when {@code id} cannot be handled by this resource registry.
     */
    default @Nullable File toLocalFile(Serializable id) { return null; }

    /**
     * Attempts to get a local file handle for given resource.
     *
     * @param resource Resource to get a local file handle for.
     * @return Local file handle, or null if given resource does not reside on the local file system.
     * @throws ResourceRuntimeException when {@code resource} cannot be handled by this resource registry.
     */
    default @Nullable File toLocalFile(Resource resource) { return null; }
}
