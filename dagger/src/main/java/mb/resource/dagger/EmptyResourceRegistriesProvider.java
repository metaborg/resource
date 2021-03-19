package mb.resource.dagger;

import mb.resource.ResourceRegistry;

import java.util.Collections;
import java.util.Set;

public class EmptyResourceRegistriesProvider implements ResourceRegistriesProvider {
    @Override public Set<ResourceRegistry> getResourceRegistries() {
        return Collections.emptySet();
    }
}
