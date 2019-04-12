package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.HashMap;

public class DefaultResourceRegistry implements ResourceRegistry {
    private final Serializable qualifier;
    private final HashMap<Serializable, Resource> resources = new HashMap<>();

    public DefaultResourceRegistry(Serializable qualifier) {
        this.qualifier = qualifier;
    }

    @Override public Serializable qualifier() {
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

    @Override public Resource getResource(ResourceKey key) {
        final Serializable qualifier = key.qualifier();
        if(!this.qualifier.equals(qualifier)) {
            throw new ResourceRuntimeException(
                "Cannot get resource with key '" + key + "' from registry; its qualifier '" + qualifier + "' does not match qualifier '" + this.qualifier + "' of the registry");
        }
        final Serializable id = key.id();
        final @Nullable Resource resource = resources.get(id);
        if(resource == null) {
            throw new ResourceRuntimeException(
                "Cannot get resource with identifier '" + id + "'; it was not found in this registry");
        }
        return resource;
    }

    public void addResource(Resource resource) {
        final ResourceKey key = resource.getKey();
        final Serializable qualifier = key.qualifier();
        if(!this.qualifier.equals(qualifier)) {
            throw new ResourceRuntimeException(
                "Cannot add resource '" + resource + "' to registry; its qualifier '" + qualifier + "' does not match qualifier '" + this.qualifier + "' of the registry");
        }
        resources.put(key.id(), resource);
    }

    public void removeResource(Resource resource) {
        final ResourceKey key = resource.getKey();
        final Serializable qualifier = key.qualifier();
        if(!this.qualifier.equals(qualifier)) {
            throw new ResourceRuntimeException(
                "Cannot remove resource '" + resource + "' from registry; its qualifier '" + qualifier + "' does not match qualifier '" + this.qualifier + "' of the registry");
        }
        resources.remove(key.id());
    }

    public void removeResource(ResourceKey key) {
        final Serializable qualifier = key.qualifier();
        if(!this.qualifier.equals(qualifier)) {
            throw new ResourceRuntimeException(
                "Cannot remove resource with key '" + key + "' from registry; its qualifier '" + qualifier + "' does not match qualifier '" + this.qualifier + "' of the registry");
        }
        resources.remove(key.id());
    }

    public void removeResource(Serializable id) {
        resources.remove(id);
    }
}
