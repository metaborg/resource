package mb.resource;

import com.google.common.jimfs.Jimfs;
import mb.resource.fs.FSResource;
import mb.resource.fs.FSResourceRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ResourceKeyStringTest {
    private final ResourceService resourceService = new DefaultResourceService(new FSResourceRegistry());

    private FSResource getResource(boolean useJimFs, Path tempDir) {
        if(useJimFs) return new FSResource(Jimfs.newFileSystem().getPath(""));
        return new FSResource(tempDir);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void testQualifiedResourceKeyStringRoundtrip(boolean useJimFs, @TempDir Path tempDir) {
        final FSResource resource = getResource(useJimFs, tempDir);
        final QualifiedResourceKeyString resourceKeyString = resource.getPath().asResourceKeyString();
        final String resourceKeyStringAsString = resourceKeyString.toString();
        final QualifiedResourceKeyString parsedResourceKeyString = QualifiedResourceKeyString.parse(resourceKeyStringAsString);
        final Resource parsedResource = resourceService.getResource(parsedResourceKeyString);
        assertEquals(resource, parsedResource);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void testResourceKeyStringRoundtrip(boolean useJimFs, @TempDir Path tempDir) {
        final FSResource resource = getResource(useJimFs, tempDir);
        final QualifiedResourceKeyString resourceKeyString = resource.getPath().asResourceKeyString();
        final String resourceKeyStringAsString = resourceKeyString.toString();
        final ResourceKeyString parsedResourceKeyString = ResourceKeyString.parse(resourceKeyStringAsString);
        final Resource parsedResource = resourceService.getResource(parsedResourceKeyString);
        assertEquals(resource, parsedResource);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisabledOnOs(value = OS.WINDOWS, disabledReason = "Windows test below, because it throws an exception")
    void testResourceKeyStringUnexpectedStringOnUnix(boolean useJimFs, @TempDir Path tempDir) {
        final FSResource resource = getResource(useJimFs, tempDir);
        final String resourceAsString = resource.getJavaPath().toString();
        final ResourceKeyString parsedResourceKeyString = ResourceKeyString.parse(resourceAsString);
        final Resource parsedResource = resourceService.getResource(parsedResourceKeyString);
        assertNotEquals(resource, parsedResource);
    }

    @Test
    @EnabledOnOs(value = OS.WINDOWS, disabledReason = "Unix tests above, because they do not throw an exception")
    void testResourceKeyStringUnexpectedStringOnWindows(@TempDir Path tempDir) {
        final FSResource resource = getResource(false, tempDir);
        final String resourceAsString = resource.getJavaPath().toString();
        assertThrows(ResourceRuntimeException.class, () -> {
            ResourceKeyString.parse(resourceAsString);
        });
    }
}
