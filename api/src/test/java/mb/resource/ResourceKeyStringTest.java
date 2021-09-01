package mb.resource;

import com.google.common.jimfs.Jimfs;
import mb.resource.fs.FSResource;
import mb.resource.fs.FSResourceRegistry;
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
    void testResourceKeyStringUnexpectedString(boolean useJimFs, @TempDir Path tempDir) {
        final FSResource resource = getResource(useJimFs, tempDir);
        final String resourceAsString = resource.getJavaPath().toString();
        final ResourceKeyString parsedResourceKeyString = ResourceKeyString.parse(resourceAsString);
        final Resource parsedResource = resourceService.getResource(parsedResourceKeyString);
        assertNotEquals(resource, parsedResource);
    }
}
