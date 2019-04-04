package mb.resource.fs;

import mb.resource.TreeResourceAccess;
import mb.resource.match.ResourceMatcher;
import mb.resource.walk.ResourceWalker;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream.Builder;

class ResourceWalkerFileVisitor implements FileVisitor<Path> {
    private final ResourceMatcher matcher;
    private final ResourceWalker walker;
    private final FSResource rootDirectory;
    private final Builder<FSResource> streamBuilder;
    private final @Nullable TreeResourceAccess access;

    ResourceWalkerFileVisitor(ResourceWalker walker, ResourceMatcher matcher, FSResource rootDirectory, Builder<FSResource> streamBuilder,
        @Nullable TreeResourceAccess access) {
        this.matcher = matcher;
        this.walker = walker;
        this.rootDirectory = rootDirectory;
        this.streamBuilder = streamBuilder;
        this.access = access;
    }

    @Override
    public FileVisitResult preVisitDirectory(@NonNull Path dir, @NonNull BasicFileAttributes attrs) throws IOException {
        final FSResource resource = new FSResource(dir);
        if(access != null) {
            access.read(resource);
        }
        if(matcher.matches(resource, rootDirectory)) {
            streamBuilder.add(resource);
        }
        if(walker.traverse(resource, rootDirectory)) {
            return FileVisitResult.CONTINUE;
        }
        return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(@NonNull Path file, @NonNull BasicFileAttributes attrs) throws IOException {
        final FSResource resource = new FSResource(file);
        if(access != null) {
            access.read(resource);
        }
        if(matcher.matches(resource, rootDirectory)) {
            streamBuilder.add(resource);
        }
        return FileVisitResult.CONTINUE;
    }

    @Override public FileVisitResult visitFileFailed(@NonNull Path file, @NonNull IOException exc) {
        // TODO: handle visit file failed.
        return FileVisitResult.CONTINUE;
    }

    @Override public FileVisitResult postVisitDirectory(@NonNull Path dir, @Nullable IOException exc) {
        // TODO: handle visit directory failed (exc != null).
        return FileVisitResult.CONTINUE;
    }
}
