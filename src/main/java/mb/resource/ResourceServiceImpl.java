package mb.resource;

import mb.resource.path.Path;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.HashMap;

public class ResourceServiceImpl implements ResourceService {
    private final HashMap<Serializable, ResourceRegistry> registries = new HashMap<>();

    @Override public @Nullable Resource getResource(ResourceKey key) {
        final Serializable qualifier = key.qualifier();
        final ResourceRegistry srv = registries.get(qualifier);
        if(srv == null) {
            throw new ResourceRuntimeException(
                "Cannot get resource for key '" + key + "', no registry found for qualifier '" + qualifier + "'");
        }
        return srv.getResource(key);
    }

    @Override public @Nullable TreeResource getResource(Path path) {
        final Serializable qualifier = path.qualifier();
        final ResourceRegistry srv = registries.get(qualifier);
        if(srv == null) {
            throw new ResourceRuntimeException(
                "Cannot get resource for path '" + path + "', no registry found for qualifier '" + qualifier + "'");
        }
        final @Nullable Resource resource = srv.getResource(path);
        if(resource != null && !(resource instanceof TreeResource)) {
            throw new ResourceRuntimeException(
                "Got resource '" + resource + "' for path '" + path + "', but resource does not implement ResourceTree");
        }
        return (TreeResource) resource;
    }

    public void addRegistry(Serializable qualifier, ResourceRegistry service) {
        registries.put(qualifier, service);
    }

    public void removeRegistry(Serializable qualifier) {
        registries.remove(qualifier);
    }
}
