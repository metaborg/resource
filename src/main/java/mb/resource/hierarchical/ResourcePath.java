package mb.resource.hierarchical;

import mb.resource.ResourceKey;
import mb.resource.ResourceRuntimeException;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public interface ResourcePath extends ResourceKey {
    /**
     * Checks if this path is absolute.
     *
     * @return True if absolute, false if relative.
     */
    boolean isAbsolute();


    /**
     * Gets the number of segments in this path.
     *
     * @return Number of segments in this path.
     */
    int getSegmentCount();

    /**
     * Gets the segments of this path, that can be used for appending with {@link #appendSegment} and {@link
     * #appendSegments}.
     *
     * @return Segments of this path.
     */
    Iterable<String> getSegments();


    /**
     * Tests if this path starts with the specified prefix.
     *
     * @param prefix Path prefix to look for
     * @return {@code true} if this path starts with {@code prefix}, {@code false} otherwise.
     * @throws ResourceRuntimeException when {@code prefix}'s (sub)type is not the same as this path's type.
     * @throws ResourceRuntimeException when {@code prefix}'s qualifier is not the same as this path's qualifier.
     */
    boolean startsWith(ResourcePath prefix);


    /**
     * Gets the parent of this path, or {@code null} if it has no parent (e.g., it is a root or empty).
     *
     * @return Parent of this path, or {@code null} if it has none.
     */
    @Nullable ResourcePath getParent();

    /**
     * Gets the root of this path, or {@code null} if it has no root (e.g., it is empty).
     *
     * @return Root of this path, or {@code null} if it has none.
     */
    @Nullable ResourcePath getRoot();

    /**
     * Gets the leaf segment of this path, or {@code null} if it has no leaf segment (e.g., it is empty).
     *
     * @return Leaf segment of this path, or {@code null} it it has none.
     */
    @Nullable String getLeaf();

    /**
     * Gets the file extension of the leaf segment of this path, or {@code null} if it has no leaf segment (e.g., it is
     * empty) or no file extension.
     *
     * @return File extension of the leaf segment of this path, or {@code null} it it has none.
     */
    default @Nullable String getLeafExtension() {
        final @Nullable String leaf = getLeaf();
        if(leaf != null) {
            return FilenameExtensionUtil.getExtension(leaf);
        }
        return null;
    }

    /**
     * Returns a normalized path. Path normalization is implementation-defined.
     *
     * @throws ResourceRuntimeException when normalization fails.
     */
    ResourcePath getNormalized();

    /**
     * Returns a string that relativizes {@code other} to this path. This is the inverse of {@link
     * #appendRelativePath(String)}.
     *
     * @throws ResourceRuntimeException when {@code other}'s (sub)type is not the same as this path's type.
     * @throws ResourceRuntimeException when {@code other}'s qualifier is not the same as this path's qualifier.
     * @throws ResourceRuntimeException when {@code other}'s (sub)type is not the same as this path's type.
     */
    String relativize(ResourcePath other);


    /**
     * Returns a path where {@code segment} is appended to the current path. A segment should be a single segment and
     * should not contain path separator characters.
     *
     * @param segment Segment to append.
     * @return Appended path.
     */
    ResourcePath appendSegment(String segment);

    /**
     * Returns a path where {@code segments} are appended to the current path in order. A segment should be a single
     * segment and should not contain path separator characters.
     *
     * @param segments Segments to append.
     * @return Appended path.
     */
    ResourcePath appendSegments(Iterable<String> segments);

    /**
     * Returns a path where {@code segments} are appended to the current path in order. A segment should be a single
     * segment and should not contain path separator characters.
     *
     * @param segments Segments to append.
     * @return Appended path.
     */
    ResourcePath appendSegments(Collection<String> segments);

    /**
     * Returns a path where {@code segments} are appended to the current path in order. A segment should be a single
     * segment and should not contain path separator characters.
     *
     * @param segments Segments to append.
     * @return Appended path.
     */
    ResourcePath appendSegments(List<String> segments);

    /**
     * Returns a path where {@code segments} are appended to the current path in order. A segment should be a single
     * segment and should not contain path separator characters.
     *
     * @param segments Segments to append.
     * @return Appended path.
     */
    ResourcePath appendSegments(String... segments);


    /**
     * Returns a path where {@code path} is appended to the current path. If {@code path} is an absolute path, it is
     * appended to the current path as if it was a relative path.
     *
     * @param path Relative path to append from.
     * @return Appended path.
     */
    ResourcePath appendAsRelativePath(String path);

    /**
     * Returns a path where {@code relativePath} is appended to the current path. Throws if {@code relativePath} is an
     * absolute path.
     *
     * @param relativePath Relative path to append from.
     * @return Appended path.
     * @throws ResourceRuntimeException when {@code relativePath} is not a relative path (but instead an absolute one).
     */
    ResourcePath appendRelativePath(String relativePath);

    /**
     * Returns a path where {@code other} is appended to the current path if {@code other} is a relative path, or
     * returns {@code other} if it is an absolute ({@link #isAbsolute()}) path.
     *
     * @param other Relative path to append, or absolute path to replace.
     * @return Appended or replaced path.
     */
    ResourcePath appendOrReplaceWithPath(String other);

    /**
     * Returns a path where {@code other} is appended to the current path using string concatenation.
     *
     * @param other String to append.
     * @return Appended path.
     * @throws ResourceRuntimeException when {@code other} is not a valid string that can be appended to this path.
     */
    ResourcePath appendString(String other);

    /**
     * Returns a path where {@code relativePath} is appended to the current path. Throws if {@code relativePath} is an
     * absolute ({@link #isAbsolute()}) path.
     *
     * @param relativePath Relative path to append from.
     * @return Appended path.
     * @throws ResourceRuntimeException when {@code relativePath} is not a relative path (but instead an absolute one).
     * @throws ResourceRuntimeException when {@code relativePath}'s (sub)type is not the same as this key's type.
     */
    ResourcePath appendRelativePath(ResourcePath relativePath);

    /**
     * Returns a path where {@code other} is appended to the current path if {@code other} is a relative path, or
     * returns {@code other} if it is an absolute ({@link #isAbsolute()}) path.
     *
     * @param other Relative path to append, or absolute path to replace.
     * @return Appended or replaced path.
     * @throws ResourceRuntimeException when {@code relativePath}'s (sub)type is not the same as this key's type.
     */
    ResourcePath appendOrReplaceWithPath(ResourcePath other);


    /**
     * Returns a path where the leaf segment of the current path is replaced by {@code segment}. If the current path has
     * no leaf, it is returned unchanged.
     *
     * @param segment Segment to replace.
     * @return Path with leaf segment replaced.
     */
    ResourcePath replaceLeaf(String segment);

    /**
     * Returns a path where the leaf segment of the current path is appended with {@code segment}. If the current path
     * has no leaf, it is returned unchanged.
     *
     * @param segment Segment to append.
     * @return Path with leaf segment appended.
     */
    ResourcePath appendToLeaf(String segment);

    /**
     * Returns a path where the leaf segment of the current path is replaced by applying {@code func} to it. If the
     * current path has no leaf, it is returned unchanged.
     *
     * @param func Function to apply to the leaf segment.
     * @return Path with leaf segment replaced.
     */
    ResourcePath applyToLeaf(Function<String, String> func);

    /**
     * Returns a path where the file extension of the leaf segment of the current path is replaced by {@code extension}.
     * If the current path has no leaf or file extension, it is returned unchanged.
     *
     * @param extension File extension to replace.
     * @return Path with file extension replaced.
     */
    ResourcePath replaceLeafExtension(String extension);

    /**
     * Returns a path where the file extension of the current path is ensured to be {@code extension}. That is, it
     * returns a path where the file extension of the leaf segment of the current path is replaced by {@code extension}
     * if it has a file extension, or it returns a path with the file extension appended if the current path has none.
     * If the current path has no leaf segment, it is returned unchanged.
     *
     * @param extension File extension to ensure.
     * @return Path with file extension ensured.
     */
    ResourcePath ensureLeafExtension(String extension);

    /**
     * Returns a path where the file extension of the leaf segment of the current path is removed. If the current path
     * has no leaf or file extension, it is returned unchanged.
     *
     * @return Path with file extension removed.
     */
    ResourcePath removeLeafExtension();

    /**
     * Returns a path where the leaf segment of the current path is appended with a '.' and {@code extension}. If the
     * current path has no leaf, it is returned unchanged.
     *
     * @param extension File extension to append.
     * @return Path with file extension appended.
     */
    ResourcePath appendToLeafExtension(String extension);

    /**
     * Returns a path where the file extension of the leaf segment of the current path is replaced by applying {@code
     * func} to it. If the current path has no leaf or file extension, it is returned unchanged.
     *
     * @param func Function to apply to the file extension.
     * @return Path with file extension replaced.
     */
    ResourcePath applyToLeafExtension(Function<String, String> func);


    /**
     * Gets this path as a string, that can be used for appending with {@link #appendOrReplaceWithPath(String)}, {@link
     * #appendRelativePath(String)} (if this path is relative), and {@link #appendString(String)}.
     *
     * @return This path as a string.
     */
    @Override String toString();
}
