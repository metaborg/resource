package mb.resource.fs;

import mb.resource.Resource;
import mb.resource.ResourceKey;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;

import java.io.Serializable;

public class FSRegistry implements ResourceRegistry {
    @Override public String qualifier() {
        return FSPath.qualifier;
    }

    @Override public FSResource getResource(Serializable id) {
        if(!(id instanceof FSPath)) {
            throw new ResourceRuntimeException(
                "Cannot get FSResource for identifier '" + id + "'; it is not of type FSPath");
        }
        final FSPath path = (FSPath) id;
        return new FSResource(path);
    }

    @Override public Resource getResource(String id) {
        return new FSResource(id);
    }

    @Override public Resource getResource(ResourceKey key) {
        if(!(key instanceof FSPath)) {
            throw new ResourceRuntimeException("Cannot get FSResource for key '" + key + "'; it is not of type FSPath");
        }
        final FSPath path = (FSPath) key;
        return new FSResource(path);
    }
}
