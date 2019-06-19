package mb.resource;

import mb.resource.string.StringResource;
import mb.resource.string.StringResourceRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringResourceRegistryTest {
    @Test
    void addAndGetResource() {
        final StringResourceRegistry registry = new StringResourceRegistry();
        final StringResource resource = registry.createResource("Hello world!", "1");
        registry.addResource(resource);
        final StringResource getResource = (StringResource) registry.getResource("1");
        assertNotNull(getResource);
        assertEquals(resource, getResource);
        assertEquals(resource.getKey().getQualifier(), getResource.getKey().getQualifier());
        assertEquals(resource.getKey().getId(), getResource.getKey().getId());
        assertEquals(resource.str, getResource.str);
    }

    @Test
    void addAndGetMultipleResources() {
        final StringResourceRegistry registry = new StringResourceRegistry();
        final StringResource resource1 = registry.createResource("Hello world!", "1");
        registry.addResource(resource1);
        final Resource getResource1 = registry.getResource("1");
        final StringResource resource2 = registry.createResource("World hello!", "2");
        registry.addResource(resource2);
        final Resource getResource2 = registry.getResource("2");
        assertEquals(resource1, getResource1);
        assertEquals(resource2, getResource2);
        assertNotEquals(resource1, getResource2);
        assertNotEquals(resource2, getResource1);
    }

    @Test
    void overwriteResource() {
        final StringResourceRegistry registry = new StringResourceRegistry();
        final String id = "1";
        final StringResource resource1 = registry.createResource("Hello world!", id);
        registry.addResource(resource1);
        final StringResource getResource1 = (StringResource) registry.getResource(id);
        assertEquals(resource1, getResource1);
        final StringResource resource2 = registry.createResource("World hello!", id);
        registry.addResource(resource2);
        final StringResource getResource2 = (StringResource) registry.getResource(id);
        assertNotNull(getResource2);
        assertEquals(resource2, getResource2);
        assertNotEquals(resource1.str, getResource2.str);
    }

    @Test
    void removeResource() {
        final StringResourceRegistry registry = new StringResourceRegistry();
        final String id = "1";
        final StringResource resource = registry.createResource("Hello world!", id);
        registry.addResource(resource);
        assertNotNull(registry.getResource(id));
        registry.removeResource((Resource) resource);
        assertThrows(ResourceRuntimeException.class, () -> {
            registry.getResource(id);
        });
    }

    @Test
    void removeResourceByKey() {
        final StringResourceRegistry registry = new StringResourceRegistry();
        final StringResource resource = registry.createResource("Hello world!", "1");
        final String id = resource.key.getId();
        registry.addResource(resource);
        assertNotNull(registry.getResource(id));
        registry.removeResource(id);
        assertThrows(ResourceRuntimeException.class, () -> registry.getResource(id));
    }
}