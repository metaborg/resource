package mb.resource;

import java.io.Serializable;

/**
 * Dummy implementation of {@link ResourceRegistry}, for testing purposes.
 */
public class DummyResourceRegistry implements ResourceRegistry {
    private final String qualifier;

    public DummyResourceRegistry(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override public String qualifier() {
        return qualifier;
    }

    @Override public Resource getResource(Serializable id) {
        return new DummyResource(new DefaultResourceKey(qualifier, (String)id));
    }

    @Override public ResourceKey getResourceKey(ResourceKeyString keyStr) {
        return new DefaultResourceKey(qualifier, keyStr.getId());
    }

    @Override public Resource getResource(ResourceKeyString keyStr) {
        return new DummyResource(new DefaultResourceKey(qualifier, keyStr.getId()));
    }

    @Override public QualifiedResourceKeyString toStringRepresentation(Serializable id) {
        return QualifiedResourceKeyString.of(qualifier, (String)id);
    }
}
