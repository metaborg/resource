package mb.resource.fs;

import mb.resource.Resource;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceRuntimeException;

import java.io.Serializable;

public class FSResourceRegistry implements ResourceRegistry {
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


    @Override public FSPath getResourceKey(String idStr) {
        return new FSPath(idStr);
    }

    @Override public Resource getResource(String idStr) {
        return new FSResource(idStr);
    }

//    @Override
//    public Resource createResource(String idStr) {
//        return null;
//    }

    @Override public String toStringRepresentation(Serializable id) {
        if(!(id instanceof FSPath)) {
            throw new ResourceRuntimeException(
                "Cannot convert identifier '" + id + "' to its string representation; it is not of type FSPath");
        }
        final FSPath path = (FSPath) id;
        return path.getStringRepresentation();
    }
}
