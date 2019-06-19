package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

public class ResourceKeyConverter {
    public static final String separator = "##";

    public static class Parsed {
        public final String qualifier;
        public final String idStr;

        public Parsed(String qualifier, String idStr) {
            this.qualifier = qualifier;
            this.idStr = idStr;
        }

        @Override public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            final Parsed that = (Parsed) o;
            if(!qualifier.equals(that.qualifier)) return false;
            return idStr.equals(that.idStr);
        }

        @Override public int hashCode() {
            int result = qualifier.hashCode();
            result = 31 * result + idStr.hashCode();
            return result;
        }

        @Override public String toString() {
            return ResourceKeyConverter.toString(qualifier, idStr);
        }
    }

    public static @Nullable Parsed parse(String keyStr) {
        final String[] split = keyStr.split(separator, 2);
        if(split.length < 2) {
            return null;
        }
        return new Parsed(split[0], split[1]);
    }

    public static String toString(String qualifier, String idStr) {
        return qualifier + separator + idStr;
    }
}
