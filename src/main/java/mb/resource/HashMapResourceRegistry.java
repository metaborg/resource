package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Base class for in-memory resource registries.
 */
abstract public class HashMapResourceRegistry implements ResourceRegistry {
    private final String qualifier;
    private final HashMap<Serializable, Resource> resources = new HashMap<>();

    /**
     * Initializes a new instance of the {@link HashMapResourceRegistry} class.
     *
     * @param qualifier The qualifier of the resources.
     */
    public HashMapResourceRegistry(String qualifier) {
        this.qualifier = qualifier;
    }


    @Override public String qualifier() {
        return qualifier;
    }

    @Override public Resource getResource(ResourceKey key) {
        final @Nullable Resource resource = resources.get(key.getId());
        if(resource == null) {
            throw new ResourceRuntimeException(
                "Cannot get resource with key '" + key + "'; it was not found in this registry");
        }
        return resource;
    }

    @Override public Resource getResource(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        final Serializable id = toId(keyStr);
        final @Nullable Resource resource = resources.get(id);
        if(resource == null) {
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
    protected abstract Serializable toId(ResourceKeyString idStr);


    /**
     * Adds the specified resource to the resources known by this registry.
     *
     * @param resource The resource to add.
     * @return {@code true} when a resource with the same key was already present and overwritten; otherwise, {@code
     * false}.
     */
    protected boolean addResource(Resource resource) {
        final ResourceKey key = resource.getKey();
        final Serializable qualifier = key.getQualifier();
        if(!this.qualifier.equals(qualifier)) {
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
     * @return {@code true} when the resource was found and removed; otherwise, {@code false}.
     */
    protected boolean removeResource(Resource resource) {
        final ResourceKey key = resource.getKey();
        final Serializable qualifier = key.getQualifier();
        if(!this.qualifier.equals(qualifier)) {
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
     * @return {@code true} when the resource was found and removed; otherwise, {@code false}.
     */
    protected boolean removeResource(Serializable id) {
        @Nullable Resource oldResource = this.resources.remove(id);
        return oldResource != null;
    }
}
