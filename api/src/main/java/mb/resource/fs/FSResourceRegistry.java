package mb.resource.fs;

import mb.resource.Resource;
import mb.resource.ResourceKey;
import mb.resource.ResourceKeyString;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;
import mb.resource.util.SeparatorUtil;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class FSResourceRegistry implements ResourceRegistry {
    static final String qualifier = "java";

    @Override public String qualifier() {
        return qualifier;
    }


    @Override public FSPath getResourceKey(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        try {
            // Convert to UNIX separators (/) as URIs require them.
            return new FSPath(new URI(SeparatorUtil.convertCurrentToUnixSeparator(keyStr.getId())));
        } catch(URISyntaxException e) {
            return new FSPath(keyStr.getId()); // Try as local path
        }
    }

    @Override public FSResource getResource(ResourceKey key) {
        if(!(key instanceof FSPath)) {
            throw new ResourceRuntimeException(
                "Cannot get FSResource for key '" + key + "'; it is not of type FSPath");
        }
        return new FSResource((FSPath)key);
    }

    @Override public FSResource getResource(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        try {
            // Convert to UNIX separators (/) as URIs require them.
            return new FSResource(new URI(SeparatorUtil.convertCurrentToUnixSeparator(keyStr.getId())));
        } catch(URISyntaxException e) {
            return new FSResource(keyStr.getId()); // Try as local path
        }
    }


    @Override public @Nullable File toLocalFile(ResourceKey key) {
        if(!(key instanceof FSPath)) {
            throw new ResourceRuntimeException(
                "Cannot attempt to convert key '" + key + "' to a local file; the key is not of type FSPath");
        }
        final FSPath path = (FSPath)key;
        try {
            return path.javaPath.toFile();
        } catch(UnsupportedOperationException e) {
            return null;
        }
    }

    @Override public @Nullable File toLocalFile(Resource resource) {
        if(!(resource instanceof FSResource)) {
            throw new ResourceRuntimeException(
                "Cannot attempt to convert resource '" + resource + "' to a local file; the resource is not of type FSResource");
        }
        final FSResource fsResource = (FSResource)resource;
        try {
            return fsResource.path.javaPath.toFile();
        } catch(UnsupportedOperationException e) {
            return null;
        }
    }
}
