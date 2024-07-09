package mb.resource.url;

import mb.resource.Resource;
import mb.resource.ResourceKey;
import mb.resource.ResourceKeyString;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.net.URISyntaxException;

public class URLResourceRegistry implements ResourceRegistry {
    public static final String qualifier = "url";


    @Override public String qualifier() {
        return qualifier;
    }


    @Override public URLPath getResourceKey(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        try {
            return new URLPath(keyStr.getId());
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException("Cannot get URL path with key string representation '" + keyStr + "'; it cannot be parsed into an URI", e);
        }
    }

    @Override public URLResource getResource(ResourceKey key) {
        if(!(key instanceof URLPath)) {
            throw new ResourceRuntimeException("Cannot get URL resource with key '" + key + "'; it is not of type URLPath");
        }
        final URLPath urlPath = (URLPath)key;
        return new URLResource(urlPath);
    }

    @Override public URLResource getResource(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        try {
            final URLPath urlPath = new URLPath(keyStr.getId());
            return new URLResource(urlPath);
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException("Cannot get URL resource with key string representation '" + keyStr + "'; it cannot be parsed into an URI", e);
        }
    }


    @Override public @Nullable File toLocalFile(ResourceKey key) {
        if(!(key instanceof URLPath)) {
            throw new ResourceRuntimeException("Cannot attempt to convert key '" + key + "' to a local file; the key is not of type URLPath");
        }
        final URLPath path = (URLPath)key;
        try {
            return new File(path.getURI());
        } catch(IllegalArgumentException e) {
            return null;
        }
    }

    @Override public @Nullable File toLocalFile(Resource resource) {
        if(!(resource instanceof URLResource)) {
            throw new ResourceRuntimeException("Cannot attempt to convert resource '" + resource + "' to a local file; the resource is not of type URLResource");
        }
        final URLResource urlResource = (URLResource)resource;
        try {
            return new File(urlResource.getPath().getURI());
        } catch(IllegalArgumentException e) {
            return null;
        }
    }
}
