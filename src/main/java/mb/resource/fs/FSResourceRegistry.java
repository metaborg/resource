package mb.resource.fs;

import mb.resource.QualifiedResourceKeyString;
import mb.resource.Resource;
import mb.resource.ResourceKeyString;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.Serializable;

public class FSResourceRegistry implements ResourceRegistry {
    static final String qualifier = "java";

    @Override public String qualifier() {
        return qualifier;
    }


    @Override public FSResource getResource(Serializable id) {
        if(!(id instanceof FSPath)) {
            throw new ResourceRuntimeException(
                "Cannot get FSResource for identifier '" + id + "'; it is not of type FSPath");
        }
        final FSPath path = (FSPath)id;
        return new FSResource(path);
    }


    @Override public FSPath getResourceKey(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatches(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        return new FSPath(keyStr.getId());
    }

    @Override public Resource getResource(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatches(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        return new FSResource(keyStr.getId());
    }

    @Override public QualifiedResourceKeyString toStringRepresentation(Serializable id) {
        if(!(id instanceof FSPath)) {
            throw new ResourceRuntimeException(
                "Cannot convert identifier '" + id + "' to its string representation; it is not of type FSPath");
        }
        final FSPath path = (FSPath)id;
        return QualifiedResourceKeyString.of(qualifier, path.getIdStringRepresentation());
    }

    @Override public @Nullable File toLocalFile(Serializable id) {
        if(!(id instanceof FSPath)) {
            throw new ResourceRuntimeException(
                "Cannot attempt to convert identifier '" + id + "' to a local file; the ID is not of type FSPath");
        }
        final FSPath path = (FSPath)id;
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
