package mb.resource.fs;

import mb.resource.Resource;
import mb.resource.ResourceKey;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class FSRegistry implements ResourceRegistry {
    @Override public @Nullable Resource getResource(ResourceKey key) {
        if(!(key instanceof FSPath)) {
            throw new ResourceRuntimeException("Cannot get resource for key '" + key + "', it is not of type FSPath");
        }
        final FSPath path = (FSPath) key;
        return new FSResource(path);
    }
}
