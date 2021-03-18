package mb.resource.hierarchical.match;

import mb.resource.hierarchical.HierarchicalResource;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.util.Objects;

public class NotResourceMatcher implements ResourceMatcher {
    private final ResourceMatcher matcher;

    public NotResourceMatcher(ResourceMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(HierarchicalResource resource, HierarchicalResource rootDirectory) throws IOException {
        return !matcher.matches(resource, rootDirectory);
    }

    @Override public boolean equals(@Nullable Object obj) {
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        final NotResourceMatcher other = (NotResourceMatcher)obj;
        return matcher.equals(other.matcher);
    }

    @Override public int hashCode() {
        return Objects.hash(matcher);
    }

    @Override public String toString() {
        return "NotResourceMatcher(" + matcher.toString() + ")";
    }
}
