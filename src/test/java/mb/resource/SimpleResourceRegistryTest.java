package mb.resource;

import mb.resource.string.StringResource;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SimpleResourceRegistryTest {
    @Test
    void addAndGetResource() throws IOException {
        final SimpleResourceRegistry registry = new SimpleResourceRegistry();
        final SimpleResourceKey key = new SimpleResourceKey("String", 1);
        final StringResource resource = new StringResource("Hello world!", key);
        registry.addResource(resource);
        final StringResource getResource = (StringResource) registry.getResource(key);
        assertNotNull(getResource);
        assertEquals(resource, getResource);
        assertEquals(resource.getKey().qualifier(), getResource.getKey().qualifier());
        assertEquals(resource.getKey().id(), getResource.getKey().id());
        assertEquals(resource.str, getResource.str);
    }

    @Test
    void addAndGetMultipleResources() {
        final SimpleResourceRegistry registry = new SimpleResourceRegistry();
        final SimpleResourceKey key1 = new SimpleResourceKey("String", 1);
        final StringResource resource1 = new StringResource("Hello world!", key1);
        registry.addResource(resource1);
        final @Nullable Resource getResource1 = registry.getResource(key1);
        final SimpleResourceKey key2 = new SimpleResourceKey("String", 2);
        final StringResource resource2 = new StringResource("World hello!", key2);
        registry.addResource(resource2);
        final @Nullable Resource getResource2 = registry.getResource(key2);
        assertEquals(resource1, getResource1);
        assertEquals(resource2, getResource2);
        assertNotEquals(resource1, getResource2);
        assertNotEquals(resource2, getResource1);
    }

    @Test
    void overwriteResource() throws IOException {
        final SimpleResourceRegistry registry = new SimpleResourceRegistry();
        final SimpleResourceKey key = new SimpleResourceKey("String", 1);
        final StringResource resource1 = new StringResource("Hello world!", key);
        registry.addResource(resource1);
        final @Nullable StringResource getResource1 = (StringResource) registry.getResource(key);
        assertEquals(resource1, getResource1);
        final StringResource resource2 = new StringResource("World hello!", key);
        registry.addResource(resource2);
        final @Nullable StringResource getResource2 = (StringResource) registry.getResource(key);
        assertNotNull(getResource2);
        assertEquals(resource2, getResource2);
        assertNotEquals(resource1.str, getResource2.str);
    }

    @Test
    void removeResource() {
        final SimpleResourceRegistry registry = new SimpleResourceRegistry();
        final SimpleResourceKey key = new SimpleResourceKey("String", 1);
        final StringResource resource = new StringResource("Hello world!", key);
        registry.addResource(resource);
        assertNotNull(registry.getResource(key));
        registry.removeResource(resource);
        assertThrows(ResourceRuntimeException.class, () -> {
            registry.getResource(key);
        });
    }

    @Test
    void removeResourceByKey() {
        final SimpleResourceRegistry registry = new SimpleResourceRegistry();
        final SimpleResourceKey key = new SimpleResourceKey("String", 1);
        final StringResource resource = new StringResource("Hello world!", key);
        registry.addResource(resource);
        assertNotNull(registry.getResource(key));
        registry.removeResource(key);
        assertThrows(ResourceRuntimeException.class, () -> {
            registry.getResource(key);
        });
    }
}