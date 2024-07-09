package mb.resource.hierarchical;

import mb.resource.hierarchical.HierarchicalResource;

import java.io.IOException;

public interface HierarchicalResourceConsumer {
    void accept(HierarchicalResource resource) throws IOException;
}
