package mb.resource;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.ResourcePath;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

public class DefaultResourceService implements ResourceService {
    private final @Nullable ResourceService parent;
    private final ResourceRegistry defaultRegistry;
    private final HashMap<String, ResourceRegistry> registries;


    private DefaultResourceService(@Nullable ResourceService parent, ResourceRegistry defaultRegistry, HashMap<String, ResourceRegistry> registries) {
        this.parent = parent;
        this.defaultRegistry = defaultRegistry;
        this.registries = registries;
    }

    public DefaultResourceService(ResourceService parent, ResourceRegistry defaultRegistry, Iterable<ResourceRegistry> registries) {
        this(parent, defaultRegistry, toRegistriesHashMap(defaultRegistry, registries));
    }

    public DefaultResourceService(ResourceService parent, ResourceRegistry defaultRegistry, ResourceRegistry... registries) {
        this(parent, defaultRegistry, toRegistriesHashMap(defaultRegistry, Arrays.asList(registries)));
    }

    public DefaultResourceService(ResourceRegistry defaultRegistry, HashMap<String, ResourceRegistry> registries) {
        this(null, defaultRegistry, registries);
    }

    public DefaultResourceService(ResourceRegistry defaultRegistry, Iterable<ResourceRegistry> registries) {
        this(null, defaultRegistry, toRegistriesHashMap(defaultRegistry, registries));
    }

    public DefaultResourceService(ResourceRegistry defaultRegistry, ResourceRegistry... registries) {
        this(null, defaultRegistry, toRegistriesHashMap(defaultRegistry, Arrays.asList(registries)));
    }

    private static HashMap<String, ResourceRegistry> toRegistriesHashMap(ResourceRegistry defaultRegistry, Iterable<ResourceRegistry> registries) {
        final HashMap<String, ResourceRegistry> registriesMap = new HashMap<>();
        registriesMap.put(defaultRegistry.qualifier(), defaultRegistry);
        for(ResourceRegistry registry : registries) {
            registriesMap.put(registry.qualifier(), registry);
        }
        return registriesMap;
    }


    @Override public Resource getResource(ResourceKey key) {
        final String qualifier = key.getQualifier();
        final @Nullable ResourceRegistry registry = getResourceRegistry(qualifier);
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
        return (ReadableResource)resource;
    }

    @Override public WritableResource getWritableResource(ResourceKey key) {
        final Resource resource = getResource(key);
        if(!(resource instanceof WritableResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a writable resource");
        }
        return (WritableResource)resource;
    }

    @Override public HierarchicalResource getHierarchicalResource(ResourcePath path) {
        final Resource resource = getResource(path);
        if(!(resource instanceof HierarchicalResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a hierarchical resource");
        }
        return (HierarchicalResource)resource;
    }


    @Override public ResourceKey getResourceKey(ResourceKeyString keyStr) {
        final @Nullable String qualifier = keyStr.getQualifier();
        if(qualifier == null) {
            try {
                return defaultRegistry.getResourceKey(keyStr);
            } catch(ResourceRuntimeException e) {
                throw new ResourceRuntimeException(
                    "No qualifier was found in string representation of resource key '" + keyStr + "', nor could a resource key be created by the default resource registry '" + defaultRegistry + "'", e);
            }
        }
        final @Nullable ResourceRegistry registry = getResourceRegistry(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException(
                "No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.getResourceKey(keyStr);
    }

    @Override public ResourcePath getResourcePath(ResourceKeyString pathStr) {
        final ResourceKey key = getResourceKey(pathStr);
        if(!(key instanceof ResourcePath)) {
            throw new ResourceRuntimeException("Resource key '" + key + "' is not a path");
        }
        return (ResourcePath)key;
    }


    @Override public Resource getResource(ResourceKeyString keyStr) {
        final @Nullable String qualifier = keyStr.getQualifier();
        if(qualifier == null) {
            try {
                return defaultRegistry.getResource(keyStr);
            } catch(ResourceRuntimeException e) {
                throw new ResourceRuntimeException(
                    "No qualifier was found in string representation of resource key '" + keyStr + "', nor could a resource be created by the default resource registry '" + defaultRegistry + "'", e);
            }
        }
        final @Nullable ResourceRegistry registry = getResourceRegistry(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException(
                "No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.getResource(keyStr);
    }

    @Override public ReadableResource getReadableResource(ResourceKeyString keyStr) {
        final Resource resource = getResource(keyStr);
        if(!(resource instanceof ReadableResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a readable resource");
        }
        return (ReadableResource)resource;
    }

    @Override public WritableResource getWritableResource(ResourceKeyString keyStr) {
        final Resource resource = getResource(keyStr);
        if(!(resource instanceof WritableResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a writable resource");
        }
        return (WritableResource)resource;
    }

    @Override public HierarchicalResource getHierarchicalResource(ResourceKeyString pathStr) {
        final Resource resource = getResource(pathStr);
        if(!(resource instanceof HierarchicalResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a hierarchical resource");
        }
        return (HierarchicalResource)resource;
    }


    @Override public Resource appendOrReplaceWith(HierarchicalResource resource, String keyStrOrPath) {
        final ResourceKeyString parsedResourceKey = ResourceKeyString.parse(keyStrOrPath);
        final @Nullable String qualifier = parsedResourceKey.getQualifier();
        if(qualifier != null) {
            final @Nullable ResourceRegistry registry = getResourceRegistry(qualifier);
            if(registry == null) {
                throw new ResourceRuntimeException("No resource registry was found for qualifier '" + qualifier + "'");
            }
            return registry.getResource(parsedResourceKey);
        }
        return resource.appendOrReplaceWithPath(keyStrOrPath);
    }

    @Override
    public HierarchicalResource appendOrReplaceWithHierarchical(HierarchicalResource resource, String pathStrOrPath) {
        final Resource newResource = appendOrReplaceWith(resource, pathStrOrPath);
        if(!(newResource instanceof HierarchicalResource)) {
            throw new ResourceRuntimeException("Resource '" + resource + "' is not a hierarchical resource");
        }
        return (HierarchicalResource)newResource;
    }


    @Override public QualifiedResourceKeyString toResourceKeyString(ResourceKey key) {
        final String qualifier = key.getQualifier();
        final @Nullable ResourceRegistry registry = getResourceRegistry(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException("No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.toResourceKeyString(key.getId());
    }

    @Override public String toString(ResourceKey key) {
        final String qualifier = key.getQualifier();
        final @Nullable ResourceRegistry registry = getResourceRegistry(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException("No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.toString(key.getId());
    }


    @Override public @Nullable File toLocalFile(ResourceKey key) {
        final String qualifier = key.getQualifier();
        final @Nullable ResourceRegistry registry = getResourceRegistry(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException("No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.toLocalFile(key.getId());
    }

    @Override public @Nullable File toLocalFile(Resource resource) {
        final String qualifier = resource.getKey().getQualifier();
        final @Nullable ResourceRegistry registry = getResourceRegistry(qualifier);
        if(registry == null) {
            throw new ResourceRuntimeException("No resource registry was found for qualifier '" + qualifier + "'");
        }
        return registry.toLocalFile(resource);
    }


    @Override public ResourceRegistry getDefaultResourceRegistry() {
        return defaultRegistry;
    }

    @Override public @Nullable ResourceRegistry getResourceRegistry(String qualifier) {
        final @Nullable ResourceRegistry registry = registries.get(qualifier);
        if(registry != null) {
            return registry;
        }
        if(parent != null) {
            return parent.getResourceRegistry(qualifier);
        }
        return null;
    }


    @Override
    public DefaultResourceService createChild(ResourceRegistry defaultRegistry, Iterable<ResourceRegistry> registries) {
        return new DefaultResourceService(this, defaultRegistry, registries);
    }
}
