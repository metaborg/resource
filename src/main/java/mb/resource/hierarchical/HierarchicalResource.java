package mb.resource.hierarchical;

import mb.resource.ResourceRuntimeException;
import mb.resource.WritableResource;
import mb.resource.hierarchical.match.ResourceMatcher;
import mb.resource.hierarchical.walk.ResourceWalker;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
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
    @Nullable String getLeaf();

    /**
     * Gets the file extension of the leaf segment of this resource, or {@code null} if it has no leaf segment (e.g., it
     * is empty) or no file extension.
     *
     * @return File extension of the leaf segment of this resource, or {@code null} it it has none.
     */
    @Nullable String getLeafExtension();


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


    HierarchicalResourceType getType() throws IOException;

    boolean isFile() throws IOException;

    boolean isDirectory() throws IOException;


    Stream<? extends HierarchicalResource> list() throws IOException;

    Stream<? extends HierarchicalResource> list(ResourceMatcher matcher) throws IOException;

    Stream<? extends HierarchicalResource> walk() throws IOException;

    Stream<? extends HierarchicalResource> walk(ResourceWalker walker, ResourceMatcher matcher) throws IOException;

    Stream<? extends HierarchicalResource> walk(ResourceWalker walker, ResourceMatcher matcher, @Nullable HierarchicalResourceAccess access) throws IOException;


    /**
     * @throws ResourceRuntimeException when {@code other}'s (sub)type is not the same as this resource's type.
     */
    void copyTo(HierarchicalResource other) throws IOException;

    /**
     * @throws ResourceRuntimeException when {@code other}'s (sub)type is not the same as this resource's type.
     */
    void moveTo(HierarchicalResource other) throws IOException;


    void createFile(boolean createParents) throws IOException;

    default void createFile() throws IOException {
        createFile(false);
    }

    void createDirectory(boolean createParents) throws IOException;

    default void createDirectory() throws IOException {
        createDirectory(false);
    }

    void createParents() throws IOException;

    void delete(boolean deleteContents) throws IOException;

    default void delete() throws IOException {
        delete(false);
    }
}
