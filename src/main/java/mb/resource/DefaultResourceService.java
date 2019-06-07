package mb.resource;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.ResourcePath;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.HashMap;

public class DefaultResourceService implements ResourceService {
    private final HashMap<Serializable, ResourceRegistry> registries;

    public DefaultResourceService() {
        this.registries = new HashMap<>();
    }

    public DefaultResourceService(Iterable<ResourceRegistry> registries) {
        this.registries = new HashMap<>();
        for(ResourceRegistry registry : registries) {
            this.registries.put(registry.qualifier(), registry);
        }
    }

    public DefaultResourceService(ResourceRegistry... registries) {
        this.registries = new HashMap<>();
        for(ResourceRegistry registry : registries) {
            this.registries.put(registry.qualifier(), registry);
        }
    }

    public DefaultResourceService(HashMap<Serializable, ResourceRegistry> registries) {
        this.registries = registries;
    }

    @Override public Resource getResource(ResourceKey key) {
        final Serializable qualifier = key.getQualifier();
        final @Nullable ResourceRegistry registry = registries.get(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException("No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.getResource(key);
    }

    @Override public ReadableResource getReadableResource(ResourceKey key) {
        final Resource resource = getResource(key);
        if(!(resource instanceof ReadableResource)) {
            throw new ResourceRuntimeException(
                "Found resource '" + resource + "' for key '" + key + "', but it does not implement ReadableResource");
        }
        return (ReadableResource) resource;
    }

    @Override public WritableResource getWritableResource(ResourceKey key) {
        final Resource resource = getResource(key);
        if(!(resource instanceof WritableResource)) {
            throw new ResourceRuntimeException(
                "Found resource '" + resource + "' for key '" + key + "', but it does not implement WritableResource");
        }
        return (WritableResource) resource;
    }

    @Override public HierarchicalResource getHierarchicalResource(ResourcePath path) {
        final Resource resource = getResource(path);
        if(!(resource instanceof HierarchicalResource)) {
            throw new ResourceRuntimeException(
                "Found resource '" + resource + "' for path '" + path + "', but it does not implement HierarchicalResource");
        }
        return (HierarchicalResource) resource;
    }

    public void addRegistry(ResourceRegistry registry) {
        registries.put(registry.qualifier(), registry);
    }

    public void removeRegistry(ResourceRegistry registry) {
        registries.remove(registry.qualifier());
    }

    public void removeRegistry(Serializable qualifier) {
        registries.remove(qualifier);
    }
}
