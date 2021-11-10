package mb.resource.hierarchical.match;

import mb.resource.hierarchical.HierarchicalResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AnyResourceMatcher implements ResourceMatcher {
    private final List<ResourceMatcher> matchers;

    public AnyResourceMatcher(ArrayList<ResourceMatcher> matchers) {
        this.matchers = matchers;
    }

    public AnyResourceMatcher(ResourceMatcher... matchers) {
        this.matchers = Arrays.asList(matchers);
    }

    @Override
    public boolean matches(HierarchicalResource resource, HierarchicalResource rootDirectory) throws IOException {
        for(ResourceMatcher matcher : matchers) {
            if(matcher.matches(resource, rootDirectory)) return true;
        }
        return false;
    }

    @Override public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        final AnyResourceMatcher other = (AnyResourceMatcher) obj;
        return matchers.equals(other.matchers);
    }

    @Override public int hashCode() {
        return Objects.hash(matchers);
    }

    @Override public String toString() {
        return "any(" + matchers + ")";
    }
}
