package mb.resource;

import java.io.Serializable;
import java.util.HashMap;

public class MultiResourceRegistry implements ResourceRegistry {
    private final HashMap<Serializable, ResourceRegistry> registries = new HashMap<>();

    @Override public Resource getResource(ResourceKey key) {
        final Serializable qualifier = key.qualifier();
        final ResourceRegistry srv = registries.get(qualifier);
        if(srv == null) {
            throw new ResourceRuntimeException(
                "Cannot get resource for key '" + key + "', no registry found for qualifier '" + qualifier + "'");
        }
        return srv.getResource(key);
    }

    public void addRegistry(Serializable qualifier, ResourceRegistry service) {
        registries.put(qualifier, service);
    }

    public void removeRegistry(Serializable qualifier) {
        registries.remove(qualifier);
    }
}
