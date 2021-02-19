package mb.resource.dagger;

import dagger.Component;
import mb.log.dagger.LoggerComponent;
import mb.resource.ResourceRegistry;
import mb.resource.ResourceService;

import java.util.Set;

@ResourceServiceScope
@Component(
    modules = {
        ResourceServiceModule.class
    },
    dependencies = {
        LoggerComponent.class
    }
)
public interface ResourceServiceComponent extends AutoCloseable {
    ResourceService getResourceService();


    default ResourceServiceModule createChildModule() {
        return new ResourceServiceModule(getResourceService());
    }

    default ResourceServiceModule createChildModule(Set<ResourceRegistry> registries) {
        return new ResourceServiceModule(getResourceService(), registries);
    }

    default ResourceServiceModule createChildModule(ResourceRegistry... registries) {
        return new ResourceServiceModule(getResourceService(), registries);
    }

    default ResourceServiceModule createChildModuleWithDefault(ResourceRegistry defaultRegistry, Set<ResourceRegistry> registries) {
        return new ResourceServiceModule(getResourceService(), defaultRegistry, registries);
    }

    default ResourceServiceModule createChildModuleWithDefault(ResourceRegistry defaultRegistry, ResourceRegistry... registries) {
        return new ResourceServiceModule(getResourceService(), defaultRegistry, registries);
    }


    @Override default void close() throws Exception {
        // For now, nothing to close. ResourceService will implement AutoCloseable in the future.
    }
}
