package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.HashMap;

abstract public class HashMapResourceRegistry implements ResourceRegistry {
    private final String qualifier;
    private final HashMap<Serializable, Resource> resources = new HashMap<>();


    public HashMapResourceRegistry(String qualifier) {
        this.qualifier = qualifier;
    }


    @Override public String qualifier() {
        return qualifier;
    }


    @Override public Resource getResource(Serializable id) {
        final @Nullable Resource resource = resources.get(id);
        if(resource == null) {
            throw new ResourceRuntimeException(
                "Cannot get resource with identifier '" + id + "'; it was not found in this registry");
        }
        return resource;
    }


    @Override public Resource getResource(String idStr) {
        final Serializable id = toId(idStr);
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
    protected abstract Serializable toId(String idStr);


    protected void addResource(Resource resource) {
        final ResourceKey key = resource.getKey();
        final Serializable qualifier = key.getQualifier();
        if(!this.qualifier.equals(qualifier)) {
            throw new ResourceRuntimeException(
                "Cannot add resource '" + resource + "' to registry; its qualifier '" + qualifier + "' does not match qualifier '" + this.qualifier + "' of the registry");
        }
        resources.put(key.getId(), resource);
    }

    protected void removeResource(Resource resource) {
        final ResourceKey key = resource.getKey();
        final Serializable qualifier = key.getQualifier();
        if(!this.qualifier.equals(qualifier)) {
            throw new ResourceRuntimeException(
                "Cannot remove resource '" + resource + "' from registry; its qualifier '" + qualifier + "' does not match qualifier '" + this.qualifier + "' of the registry");
        }
        resources.remove(key.getId());
    }

    protected void removeResource(Serializable id) {
        resources.remove(id);
    }
}
