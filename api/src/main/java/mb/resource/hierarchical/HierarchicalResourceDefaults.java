package mb.resource.hierarchical;

import mb.resource.hierarchical.match.ResourceMatcher;
import mb.resource.hierarchical.match.TrueResourceMatcher;
import mb.resource.hierarchical.walk.ResourceWalker;
import mb.resource.hierarchical.walk.TrueResourceWalker;
import mb.resource.util.SeparatorUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class HierarchicalResourceDefaults<SELF extends HierarchicalResourceDefaults<SELF>> implements HierarchicalResource {
    /**
     * Returns {@code this}. Required because {@code this} is not of type {@code SELF}.
     */
    protected abstract SELF self();


    @Override public boolean startsWith(ResourcePath prefix) {
        return getPath().startsWith(prefix);
    }

    @Override public boolean startsWith(HierarchicalResource prefix) {
        return getPath().startsWith(prefix.getPath());
    }


    @Override public abstract SELF appendSegments(Iterable<String> segments);

    @Override public SELF appendSegments(Collection<String> segments) {
        return appendSegments((Iterable<String>)segments);
    }

    @Override public SELF appendSegments(List<String> segments) {
        return appendSegments((Collection<String>)segments);
    }

    @Override public SELF appendSegments(String... segments) {
        return appendSegments(Arrays.asList(segments));
    }


    @Override public SELF appendAsRelativePath(String path) {
        if(SeparatorUtil.startsWithSeparator(path)) {
            return appendRelativePath(path.substring(1));
        } else {
            return appendRelativePath(path);
        }
    }

    @Override public abstract SELF appendRelativePath(String relativePath);

    @Override public abstract SELF appendString(String other);

    @Override public abstract SELF appendRelativePath(ResourcePath relativePath);

    @Override public SELF appendOrReplaceWithPath(ResourcePath other) {
        if(other.isAbsolute()) {
            @SuppressWarnings("unchecked") final SELF otherSelf = (SELF)other;
            return otherSelf;
        }
        return appendRelativePath(other);
    }


    @Override public abstract SELF replaceLeaf(String segment);

    @Override public SELF appendToLeaf(String segment) {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return self();
        }
        return replaceLeaf(leaf + segment);
    }

    @Override public SELF applyToLeaf(Function<String, String> func) {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return self();
        }
        return replaceLeaf(func.apply(leaf));
    }

    @Override public SELF replaceLeafExtension(String extension) {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return self();
        }
        return replaceLeaf(FilenameExtensionUtil.replaceExtension(leaf, extension));
    }

    @Override public SELF ensureLeafExtension(String extension) {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return self();
        }
        return replaceLeaf(FilenameExtensionUtil.ensureExtension(leaf, extension));
    }

    @Override public SELF appendExtensionToLeaf(String extension) {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return self();
        }
        return replaceLeaf(FilenameExtensionUtil.appendExtension(leaf, extension));
    }

    @Override public SELF applyToLeafExtension(Function<String, String> func) {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return self();
        }
        return replaceLeaf(FilenameExtensionUtil.applyToExtension(leaf, func));
    }


    @Override
    public Stream<SELF> list() throws IOException {
        return list(new TrueResourceMatcher());
    }

    @Override
    public abstract Stream<SELF> list(ResourceMatcher matcher) throws IOException;


    @Override
    public Stream<SELF> walk() throws IOException {
        return walk(new TrueResourceWalker(), new TrueResourceMatcher());
    }

    @Override
    public Stream<SELF> walk(ResourceMatcher matcher) throws IOException {
        return walk(new TrueResourceWalker(), matcher);
    }

    @Override
    public abstract Stream<SELF> walk(ResourceWalker walker, ResourceMatcher matcher) throws IOException;


    @Override public abstract SELF createFile(boolean createParents) throws IOException;

    @Override public SELF createFile() throws IOException {
        return createFile(false);
    }

    @Override public SELF ensureFileExists() throws IOException {
        try {
            return createFile(true);
        } catch(FileAlreadyExistsException ex) {
            // Ignored
        }
        return self();
    }


    @Override public abstract SELF createDirectory(boolean createParents) throws IOException;

    @Override public SELF createDirectory() throws IOException {
        return createDirectory(false);
    }

    @Override public SELF ensureDirectoryExists() throws IOException {
        try {
            return createDirectory(true);
        } catch(DirectoryAlreadyExistsException ex) {
            // Ignored
        }
        return self();
    }


    @Override public abstract boolean equals(@Nullable Object other);

    @Override public int hashCode() { return getPath().hashCode(); }

    @Override public String toString() {
        return getPath().toString();
    }
}
