package mb.resource;

import mb.resource.string.StringResource;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

class DefaultResourceRegistryTest {
    private final String qualifier = StringResource.qualifier;

    @Test
    void addAndGetResource() {
        final DefaultResourceRegistry registry = new DefaultResourceRegistry(qualifier);
        final StringResource resource = new StringResource("Hello world!", 1);
        registry.addResource(resource);
        final StringResource getResource = (StringResource) registry.getResource(1);
        assertNotNull(getResource);
        assertEquals(resource, getResource);
        assertEquals(resource.getKey().getQualifier(), getResource.getKey().getQualifier());
        assertEquals(resource.getKey().getId(), getResource.getKey().getId());
        assertEquals(resource.str, getResource.str);
    }

    @Test
    void addAndGetMultipleResources() {
        final DefaultResourceRegistry registry = new DefaultResourceRegistry(qualifier);
        final StringResource resource1 = new StringResource("Hello world!", 1);
        registry.addResource(resource1);
        final Resource getResource1 = registry.getResource(1);
        final StringResource resource2 = new StringResource("World hello!", 2);
        registry.addResource(resource2);
        final Resource getResource2 = registry.getResource(2);
        assertEquals(resource1, getResource1);
        assertEquals(resource2, getResource2);
        assertNotEquals(resource1, getResource2);
        assertNotEquals(resource2, getResource1);
    }

    @Test
    void overwriteResource() {
        final DefaultResourceRegistry registry = new DefaultResourceRegistry(qualifier);
        final int id = 1;
        final StringResource resource1 = new StringResource("Hello world!", id);
        registry.addResource(resource1);
        final StringResource getResource1 = (StringResource) registry.getResource(id);
        assertEquals(resource1, getResource1);
        final StringResource resource2 = new StringResource("World hello!", id);
        registry.addResource(resource2);
        final StringResource getResource2 = (StringResource) registry.getResource(id);
        assertNotNull(getResource2);
        assertEquals(resource2, getResource2);
        assertNotEquals(resource1.str, getResource2.str);
    }

    @Test
    void removeResource() {
        final DefaultResourceRegistry registry = new DefaultResourceRegistry(qualifier);
        final Serializable id = 1;
        final StringResource resource = new StringResource("Hello world!", id);
        registry.addResource(resource);
        assertNotNull(registry.getResource(id));
        registry.removeResource((Resource) resource);
        assertThrows(ResourceRuntimeException.class, () -> {
            registry.getResource(id);
        });
    }

    @Test
    void removeResourceByKey() {
        final DefaultResourceRegistry registry = new DefaultResourceRegistry(qualifier);
        final StringResource resource = new StringResource("Hello world!", 1);
        final ResourceKey key = resource.key;
        registry.addResource(resource);
        assertNotNull(registry.getResource(key));
        registry.removeResource(key);
        assertThrows(ResourceRuntimeException.class, () -> {
            registry.getResource(key);
        });
    }
}