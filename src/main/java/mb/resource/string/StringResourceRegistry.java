package mb.resource.string;

import mb.resource.DefaultResourceRegistry;

public class StringResourceRegistry extends DefaultResourceRegistry {
    public StringResourceRegistry() {
        super(StringResource.qualifier);
    }
}
