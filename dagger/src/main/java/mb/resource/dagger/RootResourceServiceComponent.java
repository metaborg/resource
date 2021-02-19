package mb.resource.dagger;

import dagger.Component;
import mb.log.dagger.LoggerComponent;
import mb.resource.fs.FSResourceRegistry;
import mb.resource.url.URLResourceRegistry;

@ResourceServiceScope
@Component(
    modules = {
        RootResourceServiceModule.class
    },
    dependencies = {
        LoggerComponent.class
    }
)
public interface RootResourceServiceComponent extends ResourceServiceComponent {
    FSResourceRegistry getFsResourceRegistry();

    URLResourceRegistry getUrlResourceRegistry();
}
