package mb.resource.url;

import mb.resource.QualifiedResourceKeyString;
import mb.resource.ResourceKeyString;
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
        final URLPath urlPath = (URLPath)id;
        return new URLResource(urlPath);
    }


    @Override public URLPath getResourceKey(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        try {
            return new URLPath(keyStr.getId());
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException(
                "Cannot get URL path with identifier string representation '" + keyStr + "'; the string representation cannot be parsed into an URI",
                e);
        }
    }

    @Override public URLResource getResource(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatchesOrMissing(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        try {
            final URLPath urlPath = new URLPath(keyStr.getId());
            return new URLResource(urlPath);
        } catch(URISyntaxException e) {
            throw new ResourceRuntimeException(
                "Cannot get URL resource with identifier string representation '" + keyStr + "'; the string representation cannot be parsed into an URI",
                e);
        }
    }

    @Override public QualifiedResourceKeyString toResourceKeyString(Serializable id) {
        if(!(id instanceof URLPath)) {
            throw new ResourceRuntimeException(
                "Cannot convert identifier '" + id + "' to its string representation; it is not of type URLPath");
        }
        final URLPath urlPath = (URLPath)id;
        return QualifiedResourceKeyString.of(qualifier(), urlPath.toString());
    }

    @Override public String toString(Serializable id) {
        if(!(id instanceof URLPath)) {
            throw new ResourceRuntimeException(
                "Cannot convert identifier '" + id + "' to its string representation; it is not of type URLPath");
        }
        final URLPath urlPath = (URLPath)id;
        return QualifiedResourceKeyString.toString(qualifier(), urlPath.toString());
    }
}
