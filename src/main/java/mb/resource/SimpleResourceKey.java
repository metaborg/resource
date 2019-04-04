package mb.resource;

import java.io.Serializable;

public class SimpleResourceKey implements ResourceKey {
    private final Serializable qualifier;
    private final Serializable key;

    public SimpleResourceKey(Serializable qualifier, Serializable key) {
        this.qualifier = qualifier;
        this.key = key;
    }

    @Override public Serializable qualifier() {
        return qualifier;
    }

    @Override public Serializable id() {
        return key;
    }

    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final SimpleResourceKey that = (SimpleResourceKey) o;
        if(!qualifier.equals(that.qualifier)) return false;
        return key.equals(that.key);
    }

    @Override public int hashCode() {
        int result = qualifier.hashCode();
        result = 31 * result + key.hashCode();
        return result;
    }

    @Override public String toString() {
        return "#" + qualifier + ":" + key;
    }
}
