package mb.resource.hierarchical;

import mb.resource.util.SeparatorUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public abstract class ResourcePathDefaults<SELF extends ResourcePathDefaults<SELF>> implements ResourcePath {
    /**
     * Returns {@code this}. Required because {@code this} is not of type {@code SELF}.
     */
    protected abstract SELF self();


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

    @Override public SELF appendString(String other) {
        return appendAsRelativePath(other);
    }

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

    @Override public SELF removeLeafExtension() {
        final @Nullable String leaf = getLeaf();
        if(leaf == null) {
            return self();
        }
        return replaceLeaf(FilenameExtensionUtil.removeExtension(leaf));
    }

    @Override public SELF appendToLeafExtension(String extension) {
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


    @Override public abstract boolean equals(@Nullable Object other);

    @Override public abstract int hashCode();

    @Override public abstract String toString();
}
