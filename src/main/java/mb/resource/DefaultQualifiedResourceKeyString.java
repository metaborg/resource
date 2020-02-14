package mb.resource;

import java.util.Objects;

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

    @Override public boolean equals(Object o) {
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
