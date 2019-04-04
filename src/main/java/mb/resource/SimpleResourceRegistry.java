package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.HashMap;

public class SimpleResourceRegistry implements ResourceRegistry {
    private final HashMap<Serializable, Resource> resources = new HashMap<>();

    @Override public @Nullable Resource getResource(ResourceKey key) {
        return resources.get(key);
    }

    public void addResource(Resource resource) {
        resources.put(resource.getKey(), resource);
    }

    public void removeResource(Resource resource) {
        resources.remove(resource.getKey());
    }

    public void removeResource(ResourceKey key) {
        resources.remove(key);
    }
}
