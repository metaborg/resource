package mb.resource;

/**
 * A simple resource key.
 */
public class SimpleResourceKey implements ResourceKey {
    private final String qualifier;
    private final String id;

    /**
     * Initializes a new instance of the {@link SimpleResourceKey} class.
     *
     * @param qualifier The resource qualifier.
     * @param id        The resource identifier.
     */
    public SimpleResourceKey(String qualifier, String id) {
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
        final SimpleResourceKey that = (SimpleResourceKey)o;
        if(!qualifier.equals(that.qualifier)) return false;
        return id.equals(that.id);
    }

    @Override public int hashCode() {
        int result = qualifier.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override public String toString() {
        return QualifiedResourceKeyString.toString(getQualifier(), getId());
    }
}
