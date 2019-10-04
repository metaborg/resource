package mb.resource;

import mb.resource.text.TextResource;
import mb.resource.text.TextResourceRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextResourceRegistryTest {
    @Test
    void addAndGetResource() {
        final TextResourceRegistry registry = new TextResourceRegistry();
        final TextResource resource = registry.createResource("Hello world!", "1");
        registry.addResource(resource);
        final TextResource getResource = (TextResource) registry.getResource("1");
        assertNotNull(getResource);
        assertEquals(resource, getResource);
        assertEquals(resource.getKey().getQualifier(), getResource.getKey().getQualifier());
        assertEquals(resource.getKey().getId(), getResource.getKey().getId());
        assertEquals(resource.text, getResource.text);
    }

    @Test
    void addAndGetMultipleResources() {
        final TextResourceRegistry registry = new TextResourceRegistry();
        final TextResource resource1 = registry.createResource("Hello world!", "1");
        registry.addResource(resource1);
        final Resource getResource1 = registry.getResource("1");
        final TextResource resource2 = registry.createResource("World hello!", "2");
        registry.addResource(resource2);
        final Resource getResource2 = registry.getResource("2");
        assertEquals(resource1, getResource1);
        assertEquals(resource2, getResource2);
        assertNotEquals(resource1, getResource2);
        assertNotEquals(resource2, getResource1);
    }

    @Test
    void overwriteResource() {
        final TextResourceRegistry registry = new TextResourceRegistry();
        final String id = "1";
        final TextResource resource1 = registry.createResource("Hello world!", id);
        registry.addResource(resource1);
        final TextResource getResource1 = (TextResource) registry.getResource(id);
        assertEquals(resource1, getResource1);
        final TextResource resource2 = registry.createResource("World hello!", id);
        registry.addResource(resource2);
        final TextResource getResource2 = (TextResource) registry.getResource(id);
        assertNotNull(getResource2);
        assertEquals(resource2, getResource2);
        assertNotEquals(resource1.text, getResource2.text);
    }

    @Test
    void removeResource() {
        final TextResourceRegistry registry = new TextResourceRegistry();
        final String id = "1";
        final TextResource resource = registry.createResource("Hello world!", id);
        registry.addResource(resource);
        assertNotNull(registry.getResource(id));
        registry.removeResource((Resource) resource);
        assertThrows(ResourceRuntimeException.class, () -> {
            registry.getResource(id);
        });
    }

    @Test
    void removeResourceByKey() {
        final TextResourceRegistry registry = new TextResourceRegistry();
        final TextResource resource = registry.createResource("Hello world!", "1");
        final String id = resource.key.getId();
        registry.addResource(resource);
        assertNotNull(registry.getResource(id));
        registry.deleteResource(id);
        assertThrows(ResourceRuntimeException.class, () -> registry.getResource(id));
    }
}