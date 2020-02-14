package mb.resource.text;

import mb.resource.HashMapResourceRegistry;
import mb.resource.QualifiedResourceKeyString;
import mb.resource.ResourceKeyString;
import mb.resource.ResourceRuntimeException;
import mb.resource.DefaultResourceKey;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * An in-memory read-only text resource registry.
 */
public class TextResourceRegistry extends HashMapResourceRegistry {
    public static final String qualifier = "text";

    public TextResourceRegistry() {
        super(qualifier);
    }


    @Override public DefaultResourceKey getResourceKey(ResourceKeyString keyStr) {
        if(!keyStr.qualifierMatches(qualifier)) {
            throw new ResourceRuntimeException("Qualifier of '" + keyStr + "' does not match qualifier '" + qualifier + "' of this resource registry");
        }
        return new DefaultResourceKey(TextResourceRegistry.qualifier, keyStr.getId());
    }

    @Override public QualifiedResourceKeyString toStringRepresentation(Serializable id) {
        if(!(id instanceof String)) {
            throw new ResourceRuntimeException(
                "Cannot get text resource with ID '" + id + "'; the ID is not of type String");
        }
        return QualifiedResourceKeyString.of(qualifier, (String)id);
    }


    @Override protected Serializable toId(ResourceKeyString idStr) {
        return idStr.getId();
    }

    /**
     * Creates a new read-only in-memory text resource.
     *
     * @param text The content of the resource.
     * @param id   The unique identifier of the resource; or {@code null} to generate one.
     * @return The created resource.
     */
    public TextResource createResource(String text, @Nullable String id) {
        id = id != null ? id : UUID.randomUUID().toString();
        final TextResource resource = new TextResource(text, id);
        addResource(resource);
        return resource;
    }

    /**
     * Creates a new read-only in-memory text resource with a generated unique identifier.
     *
     * @param text The content of the resource.
     * @return The created resource.
     */
    public TextResource createResource(String text) {
        return createResource(text, null);
    }

}
