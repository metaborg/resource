package mb.resource.string;

import mb.resource.HashMapResourceRegistry;

import java.io.Serializable;

public class StringResourceRegistry extends HashMapResourceRegistry {
    public static final String qualifier = "string";

    public StringResourceRegistry() {
        super(qualifier);
    }


    @Override public String toStringRepresentation(Serializable id) {
        return (String) id; // Assuming String id's from DefaultResourceKey
    }

    @Override protected Serializable toId(String idStr) {
        return idStr;
    }


    public StringResource createResource(String str, String id) {
        final StringResource resource = new StringResource(str, id);
        addResource(resource);
        return resource;
    }

    public void removeResource(String id) {
        super.removeResource(id);
    }
}
