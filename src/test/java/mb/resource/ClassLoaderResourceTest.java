package mb.resource;

import mb.resource.classloader.ClassLoaderResource;
import mb.resource.classloader.ClassLoaderResourceLocations;
import mb.resource.classloader.ClassLoaderResourceRegistry;
import mb.resource.classloader.JarFileWithPath;
import mb.resource.fs.FSResource;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassLoaderResourceTest {
    final ClassLoaderResourceRegistry registry = new ClassLoaderResourceRegistry(ClassLoaderResourceTest.class.getClassLoader());
    final String file1Name = "test_file_1_for_class_loader_resource.txt";
    final String file2Name = "test_file_2_for_class_loader_resource.txt";

    @Test void testReadFile() throws Exception {
        final ClassLoaderResource mbResource = registry.getResource("mb/resource");
        final ClassLoaderResource file1 = mbResource.appendRelativePath(file1Name);
        assertTrue(file1.exists());
        assertTrue(file1.readString().contains(file1Name));
        final ClassLoaderResource file2 = mbResource.appendAsRelativePath("classloader").appendRelativePath(file2Name);
        assertTrue(file2.exists());
        assertTrue(file2.readString().contains(file2Name));
    }

    @Test void testDirectoryLocations() throws Exception {
        final String path = "mb/resource";
        final ClassLoaderResourceLocations locations = registry.getResource(path).getLocations();
        assertTrue(locations.directories.size() > 0);
        for(FSResource directory : locations.directories) {
            assertTrue(directory.exists());
            assertTrue(directory.toString().contains(path));
        }
    }

    @Test void testJarLocations() throws Exception {
        final String path = "org/junit/jupiter/api";
        final ClassLoaderResourceLocations locations = registry.getResource(path).getLocations();
        assertTrue(locations.jarFiles.size() > 0);
        for(JarFileWithPath jarFileWithPath : locations.jarFiles) {
            assertTrue(jarFileWithPath.file.exists());
            assertTrue(jarFileWithPath.file.isFile());
            final @Nullable String leaf = jarFileWithPath.file.getLeaf();
            assertNotNull(leaf);
            assertTrue(leaf.contains("junit-jupiter-api"));
            assertTrue(leaf.contains(".jar"));
            assertTrue(jarFileWithPath.path.contains(path));
        }
    }
}
