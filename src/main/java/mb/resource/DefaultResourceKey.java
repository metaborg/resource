package mb.resource;

import java.io.Serializable;

public class DefaultResourceKey implements ResourceKey {
    private final Serializable qualifier;
    private final Serializable id;

    public DefaultResourceKey(Serializable qualifier, Serializable id) {
        this.qualifier = qualifier;
        this.id = id;
    }

    @Override public Serializable qualifier() {
        return qualifier;
    }

    @Override public Serializable id() {
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
        return "#" + qualifier + ":" + id;
    }
}
