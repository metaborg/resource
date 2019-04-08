package mb.resource.fs;

import mb.resource.ResourceKey;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;

public class FSRegistry implements ResourceRegistry {
    @Override public FSResource getResource(ResourceKey key) {
        if(!(key instanceof FSPath)) {
            throw new ResourceRuntimeException("Cannot get resource for key '" + key + "', it is not of type FSPath");
        }
        final FSPath path = (FSPath) key;
        return new FSResource(path);
    }
}
