package mb.resource.fs;

import java.nio.file.Path;
import java.util.Iterator;

class PathIterator implements Iterator<String> {
    private final Iterator<Path> iterator;

    PathIterator(Iterator<Path> iterator) {
        this.iterator = iterator;
    }

    @Override public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override public String next() {
        return iterator.next().toString();
    }
}
