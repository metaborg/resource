package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.HashMap;


/**
 * Base class for in-memory resource registries.
 */
public abstract class InMemoryResourceRegistry implements ResourceRegistry {

    private final String qualifier;
    private final HashMap<Serializable, Resource> resources = new HashMap<>();

    /**
     * Initializes a new instance of the {@link InMemoryResourceRegistry} class.
     *
     * @param qualifier The qualifier of the resources.
     */
    protected InMemoryResourceRegistry(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public String qualifier() {
        return this.qualifier;
    }


    @Override
    public Resource getResource(Serializable id) {
        final @Nullable Resource resource = this.resources.get(id);
        if (resource == null) {
            throw new ResourceRuntimeException(
                    "Cannot get resource with identifier '" + id + "'; it was not found in this registry");
        }
        return resource;
    }


    @Override
    public Resource getResource(String idStr) {
        final Serializable id = toId(idStr);
        final @Nullable Resource resource = this.resources.get(id);
        if (resource == null) {
            throw new ResourceRuntimeException(
                    "Cannot get resource with identifier '" + id + "'; it was not found in this registry");
        }
        return resource;
    }


    /**
     * Converts given string representation of identifier to its canonical form.
     *
     * @param idStr String representation of identifier.
     * @return Identifier in its canonical form.
     */
    protected abstract Serializable toId(String idStr);

    /**
     * Adds the specified resource to the resources known by this registry.
     *
     * @param resource The resource to add.
     * @return {@code true} when a resource with the same key was already present and overwritten;
     * otherwise, {@code false}.
     */
    @SuppressWarnings("UnusedReturnValue")
    protected boolean addResource(Resource resource) {
        final ResourceKey key = resource.getKey();
        final Serializable qualifier = key.getQualifier();
        if (!this.qualifier.equals(qualifier)) {
            throw new ResourceRuntimeException(
                    "Cannot add resource '" + resource + "' to registry; its qualifier '" + qualifier + "' does not " +
                            "match qualifier '" + this.qualifier + "' of the registry");
        }
        @Nullable Resource oldResource = this.resources.put(key.getId(), resource);
        return oldResource != null;
    }

    /**
     * Removes the specified resource from the resources known by this registry.
     *
     * @param resource The resource to remove.
     * @return {@code true} when the resource was found and removed;
     * otherwise, {@code false}.
     */
    @SuppressWarnings("UnusedReturnValue")
    protected boolean removeResource(Resource resource) {
        final ResourceKey key = resource.getKey();
        final Serializable qualifier = key.getQualifier();
        if (!this.qualifier.equals(qualifier)) {
            throw new ResourceRuntimeException(
                    "Cannot remove resource '" + resource + "' from registry; its qualifier '" + qualifier + "' does " +
                            "not match qualifier '" + this.qualifier + "' of the registry");
        }
        return removeResource(key.getId());
    }

    /**
     * Removes the resource with the specified identifier from the resources known by this registry.
     *
     * @param id The identifier of the resource to remove.
     * @return {@code true} when the resource was found and removed;
     * otherwise, {@code false}.
     */
    @SuppressWarnings("UnusedReturnValue")
    protected boolean removeResource(Serializable id) {
        @Nullable Resource oldResource = this.resources.remove(id);
        return oldResource != null;
    }

}
