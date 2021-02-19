package mb.resource.dagger;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceService;
import org.checkerframework.checker.nullness.qual.Nullable;

import javax.inject.Named;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Module(includes = ResourceServiceProviderModule.class)
public class ResourceServiceModule {
    private final @Nullable ResourceService parentResourceService;
    private final @Nullable ResourceRegistry defaultRegistry;
    private final Set<ResourceRegistry> registries;

    public ResourceServiceModule(
        @Nullable ResourceService parentResourceService,
        @Nullable ResourceRegistry defaultRegistry,
        Set<ResourceRegistry> registries
    ) {
        this.parentResourceService = parentResourceService;
        this.defaultRegistry = defaultRegistry;
        this.registries = registries;
    }

    public ResourceServiceModule(
        @Nullable ResourceService parentResourceService,
        @Nullable ResourceRegistry defaultRegistry,
        ResourceRegistry... registries
    ) {
        this(parentResourceService, defaultRegistry, new HashSet<>(Arrays.asList(registries)));
    }

    public ResourceServiceModule(
        ResourceRegistry defaultRegistry,
        Set<ResourceRegistry> registries
    ) {
        this(null, defaultRegistry, registries);
    }

    public ResourceServiceModule(
        ResourceRegistry defaultRegistry,
        ResourceRegistry... registries
    ) {
        this(null, defaultRegistry, registries);
    }

    public ResourceServiceModule(
        ResourceService parentResourceService,
        Set<ResourceRegistry> registries
    ) {
        this(parentResourceService, null, registries);
    }

    public ResourceServiceModule(
        ResourceService parentResourceService,
        ResourceRegistry... registries
    ) {
        this(parentResourceService, null, registries);
    }

    public ResourceServiceModule(ResourceRegistry defaultRegistry) {
        this(null, defaultRegistry, new HashSet<>());
    }

    public ResourceServiceModule(ResourceService parentResourceService) {
        this(parentResourceService, null, new HashSet<>());
    }


    public ResourceServiceModule addRegistry(ResourceRegistry registry) {
        registries.add(registry);
        return this;
    }

    public ResourceServiceModule addRegistries(ResourceRegistry... registries) {
        this.registries.addAll(Arrays.asList(registries));
        return this;
    }

    public ResourceServiceModule addRegistries(Iterable<ResourceRegistry> registries) {
        for(ResourceRegistry registry : registries) {
            this.registries.add(registry);
        }
        return this;
    }

    public ResourceServiceModule addRegistriesFrom(ResourceRegistriesProvider resourceRegistriesProvider) {
        registries.addAll(resourceRegistriesProvider.getResourceRegistries());
        return this;
    }


    @Provides @ResourceServiceScope @ElementsIntoSet
    Set<ResourceRegistry> provideResourceRegistries() {
        return registries;
    }

    @Provides @Named("parent") @ResourceServiceScope
    Optional<ResourceService> provideParentResourceService() {
        return Optional.ofNullable(parentResourceService);
    }

    @Provides @Named("default") @ResourceServiceScope
    Optional<ResourceRegistry> provideDefaultResourceRegistry() {
        return Optional.ofNullable(defaultRegistry);
    }
}
