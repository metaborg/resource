package mb.resource;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.ResourcePath;
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

    @Override public ReadableResource getReadableResource(ResourceKey key) {
        final Resource resource = getResource(key);
        if(!(resource instanceof ReadableResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a readable resource");
        }
        return (ReadableResource) resource;
    }

    @Override public WritableResource getWritableResource(ResourceKey key) {
        final Resource resource = getResource(key);
        if(!(resource instanceof WritableResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a writable resource");
        }
        return (WritableResource) resource;
    }

    @Override public HierarchicalResource getHierarchicalResource(ResourcePath path) {
        final Resource resource = getResource(path);
        if(!(resource instanceof HierarchicalResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a hierarchical resource");
        }
        return (HierarchicalResource) resource;
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

    @Override public ResourcePath getResourcePath(String pathStr) {
        final ResourceKey key = getResourceKey(pathStr);
        if(!(key instanceof ResourcePath)) {
            throw new ResourceRuntimeException("Resource key '" + key + "' is not a path");
        }
        return (ResourcePath) key;
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

    @Override public ReadableResource getReadableResource(String keyStr) {
        final Resource resource = getResource(keyStr);
        if(!(resource instanceof ReadableResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a readable resource");
        }
        return (ReadableResource) resource;
    }

    @Override public WritableResource getWritableResource(String keyStr) {
        final Resource resource = getResource(keyStr);
        if(!(resource instanceof WritableResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a writable resource");
        }
        return (WritableResource) resource;
    }

    @Override public HierarchicalResource getHierarchicalResource(String pathStr) {
        final Resource resource = getResource(pathStr);
        if(!(resource instanceof HierarchicalResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a hierarchical resource");
        }
        return (HierarchicalResource) resource;
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

    @Override
    public HierarchicalResource appendOrReplaceWithHierarchical(HierarchicalResource resource, String pathStrOrPath) {
        final Resource newResource = appendOrReplaceWith(resource, pathStrOrPath);
        if(!(newResource instanceof HierarchicalResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a hierarchical resource");
        }
        return (HierarchicalResource) newResource;
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
