package mb.resource.text;

import mb.resource.DefaultResourceKey;
import mb.resource.HashMapResourceRegistry;
import mb.resource.ResourceRuntimeException;

import java.io.Serializable;
import java.util.UUID;

public class TextResourceRegistry extends HashMapResourceRegistry {
    public static final String qualifier = "text";

    public TextResourceRegistry() {
        super(qualifier);
    }


    @Override public DefaultResourceKey getResourceKey(String idStr) {
        return new DefaultResourceKey(TextResourceRegistry.qualifier, idStr);
    }

    @Override public String toStringRepresentation(Serializable id) {
        if(!(id instanceof String)) {
            throw new ResourceRuntimeException(
                "Cannot get text resource with ID '" + id + "'; the ID is not of type String");
        }
        return (String) id; // Assuming String id's from DefaultResourceKey
    }


    @Override protected Serializable toId(String idStr) {
        return idStr;
    }


    public TextResource createResource(String text, String id) {
        final TextResource resource = new TextResource(text, id);
        addResource(resource);
        return resource;
    }

    public TextResource createResourceWithRandomUUID(String text) {
        return createResource(text, UUID.randomUUID().toString());
    }

    public void removeResource(String id) {
        super.removeResource(id);
    }
}
