package mb.resource.path;

import java.util.ArrayList;

public class PathNormalizerUtil {
    public static ArrayList<String> normalize(Iterable<String> segments, int segmentsSize) throws PathNormalizationException {
        final ArrayList<String> newSegments = new ArrayList<>(segmentsSize);
        for(String segment : segments) {
            if(segment.equals("..")) {
                final int newSegmentsSize = newSegments.size();
                if(newSegmentsSize == 0) {
                    throw new PathNormalizationException();
                } else {
                    newSegments.remove(newSegmentsSize - 1);
                }
            } else if(!segment.equals(".")) {
                newSegments.add(segment);
            }
        }
        return newSegments;
    }
}
