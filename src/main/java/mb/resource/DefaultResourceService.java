package mb.resource;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.ResourcePath;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;

public class DefaultResourceService implements ResourceService {
    private final ResourceRegistry defaultRegistry;
    private final HashMap<String, ResourceRegistry> registries;


    public DefaultResourceService(ResourceRegistry defaultRegistry, Iterable<ResourceRegistry> registries) {
        this.defaultRegistry = defaultRegistry;
        this.registries = new HashMap<>();
        this.registries.put(defaultRegistry.qualifier(), defaultRegistry);
        for(ResourceRegistry registry : registries) {
            this.registries.put(registry.qualifier(), registry);
        }
    }

    public DefaultResourceService(ResourceRegistry defaultRegistry, ResourceRegistry... registries) {
        this.defaultRegistry = defaultRegistry;
        this.registries = new HashMap<>();
        this.registries.put(defaultRegistry.qualifier(), defaultRegistry);
        for(ResourceRegistry registry : registries) {
            this.registries.put(registry.qualifier(), registry);
        }
    }

    public DefaultResourceService(ResourceRegistry defaultRegistry, HashMap<String, ResourceRegistry> registries) {
        this.defaultRegistry = defaultRegistry;
        this.registries = new HashMap<>(registries);
        this.registries.put(defaultRegistry.qualifier(), defaultRegistry);
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


    @Override public ResourceKey getResourceKey(String keyOrIdStr) {
        final ResourceKeyConverter.@Nullable Parsed parsedResourceKey = ResourceKeyConverter.parse(keyOrIdStr);
        if(parsedResourceKey == null) {
            try {
                return defaultRegistry.getResourceKey(keyOrIdStr);
            } catch(ResourceRuntimeException e) {
                throw new ResourceRuntimeException(
                    "No qualifier was found in string representation of resource key '" + keyOrIdStr + "', nor could a resource key be created by the default resource registry '" + defaultRegistry + "'", e);
            }
        }
        final String qualifier = parsedResourceKey.qualifier;
        final @Nullable ResourceRegistry registry = registries.get(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException(
                "No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.getResourceKey(parsedResourceKey.idStr);
    }

    @Override public ResourcePath getResourcePath(String pathOrIdStr) {
        final ResourceKey key = getResourceKey(pathOrIdStr);
        if(!(key instanceof ResourcePath)) {
            throw new ResourceRuntimeException("Resource key '" + key + "' is not a path");
        }
        return (ResourcePath) key;
    }


    @Override public Resource getResource(String keyOrIdStr) {
        final ResourceKeyConverter.@Nullable Parsed parsedResourceKey = ResourceKeyConverter.parse(keyOrIdStr);
        if(parsedResourceKey == null) {
            try {
                return defaultRegistry.getResource(keyOrIdStr);
            } catch(ResourceRuntimeException e) {
                throw new ResourceRuntimeException(
                    "No qualifier was found in string representation of resource key '" + keyOrIdStr + "', nor could a resource be created by the default resource registry '" + defaultRegistry + "'", e);
            }
        }
        final String qualifier = parsedResourceKey.qualifier;
        final @Nullable ResourceRegistry registry = registries.get(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException(
                "No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.getResource(parsedResourceKey.idStr);
    }

    @Override public ReadableResource getReadableResource(String keyOrIdStr) {
        final Resource resource = getResource(keyOrIdStr);
        if(!(resource instanceof ReadableResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a readable resource");
        }
        return (ReadableResource) resource;
    }

    @Override public WritableResource getWritableResource(String keyOrIdStr) {
        final Resource resource = getResource(keyOrIdStr);
        if(!(resource instanceof WritableResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a writable resource");
        }
        return (WritableResource) resource;
    }

    @Override public HierarchicalResource getHierarchicalResource(String pathOrIdStr) {
        final Resource resource = getResource(pathOrIdStr);
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


    @Override public ResourceRegistry getDefaultResourceRegistry() {
        return defaultRegistry;
    }
}
