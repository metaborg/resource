package mb.resource.dagger;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import dagger.multibindings.IntoSet;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceService;
import mb.resource.fs.FSResourceRegistry;
import mb.resource.url.URLResourceRegistry;

import javax.inject.Named;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Module(includes = ResourceServiceProviderModule.class)
public class RootResourceServiceModule {
    private final Set<ResourceRegistry> registries;


    public RootResourceServiceModule(Set<ResourceRegistry> registries) {
        this.registries = registries;
    }

    public RootResourceServiceModule(ResourceRegistry... registries) {
        this(new HashSet<>(Arrays.asList(registries)));
    }

    public RootResourceServiceModule() {
        this(new HashSet<>());
    }


    public RootResourceServiceModule addRegistry(ResourceRegistry registry) {
        registries.add(registry);
        return this;
    }

    public RootResourceServiceModule addRegistries(ResourceRegistry... registries) {
        this.registries.addAll(Arrays.asList(registries));
        return this;
    }

    public RootResourceServiceModule addRegistries(Iterable<ResourceRegistry> registries) {
        for(ResourceRegistry registry : registries) {
            this.registries.add(registry);
        }
        return this;
    }

    public RootResourceServiceModule addRegistriesFrom(ResourceRegistriesProvider resourceRegistriesProvider) {
        registries.addAll(resourceRegistriesProvider.getResourceRegistries());
        return this;
    }


    @Provides @ResourceServiceScope
    static FSResourceRegistry provideDefaultResourceRegistry() {
        return new FSResourceRegistry();
    }

    @Provides @Named("default") @ResourceServiceScope
    static Optional<ResourceRegistry> provideDefaultResourceRegistryAsOptional(FSResourceRegistry registry) {
        return Optional.of(registry);
    }

    @Provides @ResourceServiceScope @IntoSet
    static ResourceRegistry provideDefaultResourceRegistryIntoSet(FSResourceRegistry registry) {
        return registry;
    }


    @Provides @ResourceServiceScope
    static URLResourceRegistry provideUrlResourceRegistry() {
        return new URLResourceRegistry();
    }

    @Provides @ResourceServiceScope @IntoSet
    static ResourceRegistry provideUrlResourceRegistryIntoSet(URLResourceRegistry registry) {
        return registry;
    }


    @Provides @ResourceServiceScope @ElementsIntoSet
    Set<ResourceRegistry> provideResourceRegistries() {
        return registries;
    }


    @Provides @Named("parent") @ResourceServiceScope
    static Optional<ResourceService> provideParentResourceService() {
        return Optional.empty();
    }
}
