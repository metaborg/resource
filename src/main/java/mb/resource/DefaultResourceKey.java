package mb.resource;

import java.io.Serializable;

public class DefaultResourceKey implements ResourceKey {
    private final String qualifier;
    private final String id;


    public DefaultResourceKey(String qualifier, String id) {
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
        final DefaultResourceKey that = (DefaultResourceKey) o;
        if(!qualifier.equals(that.qualifier)) return false;
        return id.equals(that.id);
    }

    @Override public int hashCode() {
        int result = qualifier.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override public String toString() {
        return ResourceKeyConverter.toString(qualifier, id);
    }
}
