package mb.resource.hierarchical;

import mb.resource.ResourceRuntimeException;
import mb.resource.WritableResource;
import mb.resource.hierarchical.match.ResourceMatcher;
import mb.resource.hierarchical.walk.ResourceWalker;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface HierarchicalResource extends WritableResource {
    /**
     * Gets the path of this resource.
     *
     * @return Path of this resource.
     */
    default ResourcePath getPath() {
        return getKey();
    }

    /**
     * Gets the key of this resource, which is a {@link ResourcePath path}.
     *
     * @return Key of this resource.
     */
    @Override ResourcePath getKey();


    /**
     * Tests if the path of this resource starts with the specified prefix.
     *
     * @param prefix Path prefix to look for
     * @return {@code true} if the path of this resource starts with {@code prefix}, {@code false} otherwise.
     * @throws ResourceRuntimeException when {@code prefix}'s (sub)type is not the same as this resource's path type.
     * @throws ResourceRuntimeException when {@code prefix}'s qualifier is not the same as this resource's qualifier.
     */
    boolean startsWith(ResourcePath prefix);

    /**
     * Tests if the path of this resource starts with the specified prefix.
     *
     * @param prefix Path prefix of the resource to look for
     * @return {@code true} if the path of this resource starts with {@code prefix}, {@code false} otherwise.
     * @throws ResourceRuntimeException when {@code prefix}'s (sub)type is not the same as this resource's type.
     * @throws ResourceRuntimeException when {@code prefix}'s qualifier is not the same as this resource's qualifier.
     */
    boolean startsWith(HierarchicalResource prefix);


    /**
     * Gets the parent of this resource, or {@code null} if it has no parent (e.g., it is a root or empty).
     *
     * @return Parent of this resource, or {@code null} if it has none.
     */
    @Nullable HierarchicalResource getParent();

    /**
     * Gets the root of this resource, or {@code null} if it has no root (e.g., it is empty).
     *
     * @return Root of this resource, or {@code null} if it has none.
     */
    @Nullable HierarchicalResource getRoot();

    /**
     * Gets the leaf segment of this resource, or {@code null} if it has no leaf segment (e.g., it is empty).
     *
     * @return Leaf segment of this resource, or {@code null} it it has none.
     */
    default @Nullable String getLeaf() {
        return getPath().getLeaf();
    }

    /**
     * Gets the file extension of the leaf segment of this resource, or {@code null} if it has no leaf segment (e.g., it
     * is empty) or no file extension.
     *
     * @return File extension of the leaf segment of this resource, or {@code null} it it has none.
     */
    default @Nullable String getLeafExtension() {
        return getPath().getLeafExtension();
    }

    /**
     * Returns a resource with a normalized path. Path normalization is implementation-defined.
     *
     * @throws ResourceRuntimeException when normalization fails.
     */
    HierarchicalResource getNormalized();


    /**
     * Returns a resource where {@code segment} is appended to the current resource. A segment should be a single
     * segment and should not contain path characters.
     *
     * @param segment Segment to append.
     * @return Appended resource.
     */
    HierarchicalResource appendSegment(String segment);

    /**
     * Returns a resource where {@code segments} are appended to the current resource in order. A segment should be a
     * single segment and should not contain path characters.
     *
     * @param segments Segments to append.
     * @return Appended resource.
     */
    HierarchicalResource appendSegments(Iterable<String> segments);

    /**
     * Returns a resource where {@code segments} are appended to the current resource in order. A segment should be a
     * single segment and should not contain path characters.
     *
     * @param segments Segments to append.
     * @return Appended resource.
     */
    HierarchicalResource appendSegments(Collection<String> segments);

    /**
     * Returns a resource where {@code segments} are appended to the current resource in order. A segment should be a
     * single segment and should not contain path characters.
     *
     * @param segments Segments to append.
     * @return Appended resource.
     */
    HierarchicalResource appendSegments(List<String> segments);

    /**
     * Returns a resource where {@code segments} are appended to the current resource in order. A segment should be a
     * single segment and should not contain path characters.
     *
     * @param segments Segments to append.
     * @return Appended resource.
     */
    HierarchicalResource appendSegments(String... segments);


    /**
     * Returns a resource where {@code path} is appended to the current resource. If {@code path} is an absolute path,
     * it is appended to the current resource as if it was a relative path.
     *
     * @param path Relative path to append from.
     * @return Appended resource.
     */
    HierarchicalResource appendAsRelativePath(String path);

    /**
     * Returns a resource where {@code relativePath} is appended to the current resource. Throws if {@code relativePath}
     * is an absolute path.
     *
     * @param relativePath Relative path to append from.
     * @return Appended resource.
     * @throws ResourceRuntimeException when {@code relativePath} is not a relative path (but instead an absolute one).
     */
    HierarchicalResource appendRelativePath(String relativePath);

    /**
     * Returns a resource where {@code other} is appended to the current resource if {@code other} is a relative path,
     * or returns a resource for {@code other} if it is an absolute path.
     *
     * @param other Relative path to append, or absolute path to replace.
     * @return Appended or replaced resource.
     */
    HierarchicalResource appendOrReplaceWithPath(String other);

    /**
     * Returns a resource where {@code other} is appended to the current resource using string concatenation.
     *
     * @param other String to append.
     * @return Appended resource.
     * @throws ResourceRuntimeException when {@code other} is not a valid string that can be appended to this resource.
     */
    HierarchicalResource appendString(String other);

    /**
     * Returns a resource where {@code relativePath} is appended to the current resource. Throws if {@code relativePath}
     * is an absolute path.
     *
     * @param relativePath Relative path to append from.
     * @return Appended resource.
     * @throws ResourceRuntimeException when {@code relativePath} is not a relative path (but instead an absolute one).
     * @throws ResourceRuntimeException when {@code relativePath}'s (sub)type is not the same as this resource's path
     *                                  type.
     */
    HierarchicalResource appendRelativePath(ResourcePath relativePath);

    /**
     * Returns a resource where {@code other} is appended to the current resource if {@code other} is a relative path,
     * or returns a resource for {@code other} if it is an absolute path.
     *
     * @param other Relative path to append, or absolute path to replace.
     * @return Appended or replaced resource.
     * @throws ResourceRuntimeException when {@code relativePath}'s (sub)type is not the same as this key's type.
     */
    HierarchicalResource appendOrReplaceWithPath(ResourcePath other);


    /**
     * Returns a resource where the leaf segment of the current resource is replaced by {@code segment}. If the current
     * resource has no leaf, it is returned unchanged.
     *
     * @param segment Segment to replace.
     * @return Resource with leaf segment replaced.
     */
    HierarchicalResource replaceLeaf(String segment);

    /**
     * Returns a resource where the leaf segment of the current resource is appended with {@code segment}. If the
     * current path has no leaf, it is returned unchanged.
     *
     * @param segment Segment to append.
     * @return Resource with leaf segment appended.
     */
    HierarchicalResource appendToLeaf(String segment);

    /**
     * Returns a resource where the leaf segment of the current resource is replaced by applying {@code func} to it. If
     * the current resource has no leaf, it is returned unchanged.
     *
     * @param func Function to apply to the leaf segment.
     * @return Resource with leaf segment replaced.
     */
    HierarchicalResource applyToLeaf(Function<String, String> func);

    /**
     * Returns a resource where the file extension of the leaf segment of the current resource is replaced by {@code
     * extension}. If the current resource has no leaf or file extension, it is returned unchanged.
     *
     * @param extension File extension to replace.
     * @return Resource with file extension replaced.
     */
    HierarchicalResource replaceLeafExtension(String extension);

    /**
     * Returns a resource where the file extension of the current resource is ensured to be {@code extension}. That is,
     * it returns a resource where the file extension of the leaf segment of the current resource is replaced by {@code
     * extension} if it has a file extension, or it returns a resource with the file extension appended if the current
     * resource has none. If the current resource has no leaf segment, it is returned unchanged.
     *
     * @param extension File extension to ensure.
     * @return Resource with file extension ensured.
     */
    HierarchicalResource ensureLeafExtension(String extension);

    /**
     * Returns a resource where the leaf segment of the current resource is appended with a '.' and {@code extension}.
     * If the current resource has no leaf, it is returned unchanged.
     *
     * @param extension File extension to append.
     * @return Resource with file extension appended.
     */
    HierarchicalResource appendExtensionToLeaf(String extension);

    /**
     * Returns a resource where the file extension of the leaf segment of the current resource is replaced by applying
     * {@code func} to it. If the current resource has no leaf or file extension, it is returned unchanged.
     *
     * @param func Function to apply to the file extension.
     * @return Resource with file extension replaced.
     */
    HierarchicalResource applyToLeafExtension(Function<String, String> func);


    /**
     * @throws UnsupportedOperationException The operation is not supported.
     */
    HierarchicalResourceType getType() throws IOException;

    /**
     * @throws UnsupportedOperationException The operation is not supported.
     */
    default boolean isFile() throws IOException {
        return getType() == HierarchicalResourceType.File;
    }

    /**
     * @throws UnsupportedOperationException The operation is not supported.
     */
    default boolean isDirectory() throws IOException {
        return getType() == HierarchicalResourceType.Directory;
    }


    /**
     * @throws UnsupportedOperationException The operation is not supported.
     */
    Stream<? extends HierarchicalResource> list() throws IOException;

    /**
     * @param matcher
     * @throws UnsupportedOperationException The operation is not supported.
     */
    Stream<? extends HierarchicalResource> list(ResourceMatcher matcher) throws IOException;

    /**
     * @throws UnsupportedOperationException The operation is not supported.
     */
    Stream<? extends HierarchicalResource> walk() throws IOException;

    /**
     * @param walker
     * @param matcher
     * @throws UnsupportedOperationException The operation is not supported.
     */
    Stream<? extends HierarchicalResource> walk(ResourceWalker walker, ResourceMatcher matcher) throws IOException;

    /**
     * @param walker
     * @param matcher
     * @param access
     * @throws UnsupportedOperationException The operation is not supported.
     */
    Stream<? extends HierarchicalResource> walk(ResourceWalker walker, ResourceMatcher matcher, @Nullable HierarchicalResourceAccess access) throws IOException;


    /**
     * @throws ResourceRuntimeException      When {@code other}'s (sub)type is not the same as this resource's type.
     * @throws UnsupportedOperationException The operation is not supported.
     */
    void copyTo(HierarchicalResource other) throws IOException;

    /**
     * @throws ResourceRuntimeException      When {@code other}'s (sub)type is not the same as this resource's type.
     * @throws UnsupportedOperationException The operation is not supported.
     */
    void copyRecursivelyTo(HierarchicalResource other) throws IOException;

    /**
     * @throws ResourceRuntimeException      When {@code other}'s (sub)type is not the same as this resource's type.
     * @throws UnsupportedOperationException The operation is not supported.
     */
    void moveTo(HierarchicalResource other) throws IOException;


    @Override default OutputStream openWrite() throws IOException {
        ensureFileExists();
        return openWriteExisting();
    }

    @Override default OutputStream openWriteNew() throws IOException {
        createFile();
        return openWriteExisting();
    }


    /**
     * Creates this file resource.
     *
     * @param createParents Whether to create the parent directories.
     * @return This object for chaining.
     * @throws FileAlreadyExistsException      The file already exists.
     * @throws DirectoryAlreadyExistsException A directory with the same name already exists.
     * @throws IOException                     The parent directory does not exist, or an I/O exception occurred.
     * @throws UnsupportedOperationException   The operation is not supported.
     */
    HierarchicalResource createFile(boolean createParents) throws IOException;

    /**
     * Creates this file resource.
     * <p>
     * This method does not create the parent directories if they do not exist.
     *
     * @return This object for chaining.
     * @throws FileAlreadyExistsException      The file already exists.
     * @throws DirectoryAlreadyExistsException A directory with the same name already exists.
     * @throws IOException                     The parent directory does not exist, or an I/O exception occurred.
     * @throws UnsupportedOperationException   The operation is not supported.
     */
    HierarchicalResource createFile() throws IOException;

    /**
     * Creates this file resource if it does not already exist.
     * <p>
     * This method creates parent directories if necessary.
     * <p>
     * It is possible for the resource to be deleted between a call to this method and a following call to a method such
     * as {@link #openWriteExisting()}.
     *
     * @return This object for chaining.
     * @throws DirectoryAlreadyExistsException A directory with the same name already exists.
     * @throws IOException                     An I/O exception occurred.
     * @throws UnsupportedOperationException   The operation is not supported.
     */
    HierarchicalResource ensureFileExists() throws IOException;


    /**
     * Creates this directory resource.
     *
     * @param createParents Whether to create the parent directories.
     * @return This object for chaining.
     * @throws DirectoryAlreadyExistsException The directory already exists.
     * @throws FileAlreadyExistsException      A file with the same name already exists.
     * @throws IOException                     The parent directory does not exist, or an I/O exception occurred.
     * @throws UnsupportedOperationException   The operation is not supported.
     */
    HierarchicalResource createDirectory(boolean createParents) throws IOException;

    /**
     * Creates this directory resource.
     * <p>
     * This method does not create the parent directories if they do not exist.
     *
     * @return This object for chaining.
     * @throws DirectoryAlreadyExistsException The directory already exists.
     * @throws FileAlreadyExistsException      A file with the same name already exists.
     * @throws IOException                     The parent directory does not exist, or an I/O exception occurred.
     * @throws UnsupportedOperationException   The operation is not supported.
     */
    HierarchicalResource createDirectory() throws IOException;

    /**
     * Creates this resource if it does not already exist.
     * <p>
     * This method creates parent directories if necessary.
     * <p>
     * It is possible for the resource to be deleted between a call to this method and a following call to a method.
     *
     * @return This object for chaining.
     * @throws FileAlreadyExistsException    A file with the same name already exists.
     * @throws IOException                   An I/O exception occurred.
     * @throws UnsupportedOperationException The operation is not supported.
     */
    HierarchicalResource ensureDirectoryExists() throws IOException;

    /**
     * Creates the parent directories of this resource.
     *
     * @return This object for chaining.
     * @throws IOException                   An I/O exception occurred.
     * @throws UnsupportedOperationException The operation is not supported.
     */
    HierarchicalResource createParents() throws IOException;


    /**
     * Deletes the resource.
     *
     * @param deleteRecursively Whether to delete the children of the resource recursively.
     * @throws DirectoryNotEmptyException    The directory resource is not empty.
     * @throws IOException                   An I/O exception occurred.
     * @throws UnsupportedOperationException The operation is not supported.
     */
    void delete(boolean deleteRecursively) throws IOException;

    /**
     * Deletes the resource.
     * <p>
     * This method does not recursively delete the children of the resource, if any.
     *
     * @throws DirectoryNotEmptyException    The directory resource is not empty.
     * @throws IOException                   An I/O exception occurred.
     * @throws UnsupportedOperationException The operation is not supported.
     */
    default void delete() throws IOException {
        delete(false);
    }
}
