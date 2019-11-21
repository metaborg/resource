package mb.resource.url;

import mb.resource.ResourceKey;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;

import java.io.Serializable;
import java.net.URISyntaxException;

public class URLResourceRegistry implements ResourceRegistry {
    public static final String qualifier = "url";


    @Override public String qualifier() {
        return qualifier;
    }


    @Override public URLResource getResource(Serializable id) {
        if(!(id instanceof URLPath)) {
            throw new ResourceRuntimeException(
                "Cannot get URL resource with ID '" + id + "'; the ID is not of type URLPath");
        }
        final URLPath urlPath = (URLPath) id;
        return new URLResource(urlPath);
    }


    @Override public URLPath getResourceKey(String idStr) {
        try {
            return new URLPath(idStr);
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException(
                "Cannot get URL path with identifier string representation '" + idStr + "'; the string representation cannot be parsed into an URI",
                e);
        }
    }

    @Override public URLResource getResource(String idStr) {
        try {
            final URLPath urlPath = new URLPath(idStr);
            return new URLResource(urlPath);
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException(
                "Cannot get URL resource with identifier string representation '" + idStr + "'; the string representation cannot be parsed into an URI",
                e);
        }
    }

    @Override public String toStringRepresentation(Serializable id) {
        if(!(id instanceof URLPath)) {
            throw new ResourceRuntimeException(
                "Cannot convert identifier '" + id + "' to its string representation; it is not of type URLPath");
        }
        final URLPath urlPath = (URLPath) id;
        return urlPath.getIdStringRepresentation();
    }
}
