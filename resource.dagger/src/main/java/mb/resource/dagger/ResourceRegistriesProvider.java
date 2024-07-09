package mb.resource.dagger;

import mb.resource.ResourceRegistry;

import java.util.Set;

@FunctionalInterface
public interface ResourceRegistriesProvider {
    Set<ResourceRegistry> getResourceRegistries();

    default void addResourceRegistriesTo(ResourceServiceModule resourceServiceModule) {
        resourceServiceModule.addRegistriesFrom(this);
    }

    default void addResourceRegistriesTo(RootResourceServiceModule resourceServiceModule) {
        resourceServiceModule.addRegistriesFrom(this);
    }
}
