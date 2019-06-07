package mb.resource.url;

import mb.resource.ResourceKey;

import java.io.Serializable;
import java.net.URL;

public class URLResourceKey implements ResourceKey {
    private final URL url;


    public URLResourceKey(URL url) {
        this.url = url;
    }


    @Override public Serializable qualifier() {
        return "url";
    }

    @Override public Serializable id() {
        return url;
    }


    @Override public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final URLResourceKey that = (URLResourceKey) o;
        return url.equals(that.url);
    }

    @Override public int hashCode() {
        return url.hashCode();
    }

    @Override public String toString() {
        return url.toString();
    }
}
