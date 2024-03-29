package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Comparator;
import java.util.Objects;

import static java.util.Comparator.comparing;

public class DefaultQualifiedResourceKeyString implements QualifiedResourceKeyString, ResourceKeyString {
    private final String qualifier;
    private final String id;

    DefaultQualifiedResourceKeyString(String qualifier, String id) {
        this.qualifier = qualifier;
        this.id = id;
    }


    @Override public String getQualifier() {
        return qualifier;
    }

    @Override public String getId() {
        return id;
    }


    private static final Comparator<QualifiedResourceKeyString> comparator =
        comparing(QualifiedResourceKeyString::getQualifier)
            .thenComparing(QualifiedResourceKeyString::getId);

    @Override public int compareTo(QualifiedResourceKeyString o) {
        return comparator.compare(this, o);
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final DefaultQualifiedResourceKeyString that = (DefaultQualifiedResourceKeyString)o;
        return qualifier.equals(that.qualifier) && id.equals(that.id);
    }

    @Override public int hashCode() {
        return Objects.hash(qualifier, id);
    }

    @Override public String toString() {
        return QualifiedResourceKeyString.toString(qualifier, id);
    }
}
