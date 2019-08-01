package mb.resource;

import mb.resource.hierarchical.HierarchicalResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;

public class DefaultResourceService implements ResourceService {
    private final HashMap<String, ResourceRegistry> registries;


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

    public DefaultResourceService(HashMap<String, ResourceRegistry> registries) {
        this.registries = registries;
    }


    @Override public Resource getResource(ResourceKey key) {
        final String qualifier = key.getQualifier();
        final @Nullable ResourceRegistry registry = registries.get(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException("No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.getResource(key.getId());
    }


    @Override public ResourceKey getResourceKey(String keyStr) {
        final ResourceKeyConverter.@Nullable Parsed parsedResourceKey = ResourceKeyConverter.parse(keyStr);
        if(parsedResourceKey == null) {
            throw new ResourceRuntimeException(
                "No qualifier was found in string representation of resource key '" + keyStr + "'");
        }
        final String qualifier = parsedResourceKey.qualifier;
        final @Nullable ResourceRegistry registry = registries.get(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException(
                "No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.getResourceKey(parsedResourceKey.idStr);
    }

    @Override public Resource getResource(String keyStr) {
        final ResourceKeyConverter.@Nullable Parsed parsedResourceKey = ResourceKeyConverter.parse(keyStr);
        if(parsedResourceKey == null) {
            throw new ResourceRuntimeException(
                "No qualifier was found in string representation of resource key '" + keyStr + "'");
        }
        final String qualifier = parsedResourceKey.qualifier;
        final @Nullable ResourceRegistry registry = registries.get(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException(
                "No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.getResource(parsedResourceKey.idStr);
    }

    @Override public Resource appendOrReplaceWith(HierarchicalResource resource, String keyStrOrPath) {
        final ResourceKeyConverter.@Nullable Parsed parsedResourceKey = ResourceKeyConverter.parse(keyStrOrPath);
        if(parsedResourceKey != null) {
            final String qualifier = parsedResourceKey.qualifier;
            final @Nullable ResourceRegistry registry = registries.get(qualifier);
            if(registry == null) {
                throw new ResourceRuntimeException("No resource registry was found for qualifier '" + qualifier + "'");
            }
            return registry.getResource(parsedResourceKey.idStr);
        }
        return resource.appendOrReplaceWithPath(keyStrOrPath);
    }

    @Override public String toStringRepresentation(ResourceKey key) {
        final String qualifier = key.getQualifier();
        final @Nullable ResourceRegistry registry = registries.get(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException("No resource registry was found for qualifier '" + qualifier + "'");
        }
        final String idStr = registry.toStringRepresentation(key.getId());
        return ResourceKeyConverter.toString(qualifier, idStr);
    }


    public void addRegistry(ResourceRegistry registry) {
        registries.put(registry.qualifier(), registry);
    }

    public void removeRegistry(ResourceRegistry registry) {
        registries.remove(registry.qualifier());
    }

    public void removeRegistry(String qualifier) {
        registries.remove(qualifier);
    }
}
