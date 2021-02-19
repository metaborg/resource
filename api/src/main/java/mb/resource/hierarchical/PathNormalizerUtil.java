package mb.resource.hierarchical;

import mb.resource.ResourceRuntimeException;
import mb.resource.util.SeparatorUtil;

import java.util.ArrayList;

public class PathNormalizerUtil {
    public static ArrayList<String> normalize(Iterable<String> segments, int segmentsSize) {
        final ArrayList<String> newSegments = new ArrayList<>(segmentsSize);
        for(String segment : segments) {
            if(segment.equals("..")) {
                final int newSegmentsSize = newSegments.size();
                if(newSegmentsSize == 0) {
                    throw new ResourceRuntimeException("Failed to normalize path segments '" + SeparatorUtil.joinWithUnixSeparator(segments) + "', a .. was found without a parent segment");
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
