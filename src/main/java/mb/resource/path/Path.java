package mb.resource.path;

import mb.resource.ResourceKey;
import mb.resource.ResourceRuntimeException;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public interface Path extends ResourceKey, Comparable<Path>, Serializable {
    /**
     * @return {@link ResourceKey} qualifier indicating to which file system/tree this path belongs.
     */
    @Override String qualifier();

    /**
     * @return this path as the {@link ResourceKey} identifier.
     */
    @Override default Path id() {
        return this;
    }


    boolean isAbsolute();


    int getSegmentCount();

    Iterable<String> getSegments();


    @Nullable Path getParent();

    @Nullable Path getRoot();

    @Nullable String getLeaf();

    default @Nullable String getLeafExtension() {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return null;
        }
        return FilenameExtensionUtil.extension(leaf);
    }

    Path getNormalized() throws PathNormalizationException;

    Path relativize(Path other);


    /**
     * Creates a new path where given {@code segment} is appended to this path. Handling of the segment string is implementation-dependent.
     *
     * @param segment segment to append.
     * @return new path with {@code segment} appended.
     */
    Path appendSegment(String segment);

    Path appendSegments(Iterable<String> segments);

    Path appendSegments(Collection<String> segments);

    default Path appendSegments(List<String> segments) {
        return appendSegments((Collection<String>) segments);
    }

    default Path appendSegments(String... segments) {
        return appendSegments((Collection<String>) Arrays.asList(segments));
    }

    /**
     * @throws ResourceRuntimeException when `relativePath` is not of the same runtime (super)type as this path.
     * @throws ResourceRuntimeException when `relativePath` is not a relative path (but instead an absolute one).
     */
    Path appendRelativePath(Path relativePath);


    Path replaceLeaf(String segment);

    default Path appendToLeaf(String str) {
        return replaceLeaf(getLeaf() + str);
    }

    default Path applyToLeaf(Function<String, String> func) {
        return replaceLeaf(func.apply(getLeaf()));
    }

    Path replaceLeafExtension(String extension);

    Path appendExtensionToLeaf(String extension);

    Path applyToLeafExtension(Function<String, String> func);


    /**
     * @throws ResourceRuntimeException when other is not of the same runtime (super)type as this path.
     */
    @Override int compareTo(Path other);


    @Override boolean equals(@Nullable Object other);

    @Override int hashCode();

    @Override String toString();
}
