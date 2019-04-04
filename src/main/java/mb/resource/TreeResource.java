package mb.resource;

import mb.resource.match.ResourceMatcher;
import mb.resource.path.FilenameExtensionUtil;
import mb.resource.path.Path;
import mb.resource.walk.ResourceWalker;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public interface TreeResource extends Resource, IOWrite, IORead {
    Path getPath();

    @Override Path getKey();


    @Nullable TreeResource getParent();

    @Nullable TreeResource getRoot();

    @Nullable String getLeaf();

    default @Nullable String getLeafExtension() {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return null;
        }
        return FilenameExtensionUtil.extension(leaf);
    }


    TreeResource appendSegment(String segment);

    TreeResource appendSegments(Iterable<String> segments);

    TreeResource appendSegments(Collection<String> segments);

    default TreeResource appendSegments(List<String> segments) {
        return appendSegments((Collection<String>) segments);
    }

    default TreeResource appendSegments(String... segments) {
        return appendSegments((Collection<String>) Arrays.asList(segments));
    }

    /**
     * @throws ResourceRuntimeException when relativePath is not of the same runtime (super)type as the path from {@link #getPath}.
     * @throws ResourceRuntimeException when relativePath is not a relative path (but instead an absolute one).
     */
    TreeResource appendRelativePath(Path relativePath);


    TreeResource replaceLeaf(String segment);

    default TreeResource appendToLeaf(String str) {
        return replaceLeaf(getLeaf() + str);
    }

    default TreeResource applyToLeaf(Function<String, String> func) {
        return replaceLeaf(func.apply(getLeaf()));
    }

    TreeResource replaceLeafExtension(String extension);

    TreeResource appendExtensionToLeaf(String extension);

    TreeResource applyToLeafExtension(Function<String, String> func);

    TreeResourceType getType() throws IOException;

    boolean isFile() throws IOException;

    boolean isDirectory() throws IOException;


    Stream<? extends TreeResource> list() throws IOException;

    Stream<? extends TreeResource> list(ResourceMatcher matcher) throws IOException;


    Stream<? extends TreeResource> walk() throws IOException;

    Stream<? extends TreeResource> walk(ResourceWalker walker, ResourceMatcher matcher, @Nullable TreeResourceAccess access) throws IOException;

    default Stream<? extends TreeResource> walk(ResourceWalker walker, ResourceMatcher matcher) throws IOException {
        return walk(walker, matcher, null);
    }


    /**
     * @throws ResourceRuntimeException when {@code other} is not of the same runtime (super)type as this resource.
     */
    void copyTo(TreeResource other) throws IOException;

    /**
     * @throws ResourceRuntimeException when {@code other} is not of the same runtime (super)type as this resource.
     */
    void moveTo(TreeResource other) throws IOException;


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


    @Override boolean equals(@Nullable Object other);

    @Override int hashCode();

    @Override String toString();
}
