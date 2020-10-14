package mb.resource.util;

import java.io.File;

public class SeparatorUtil {
    public static final String unixSeparator = "/";
    public static final char unixSeparatorChar = '/';


    public static boolean startsWithSeparator(String path) {
        return path.startsWith(File.separator) || path.startsWith(unixSeparator);
    }

    public static boolean endsWithSeparator(String path) {
        return path.endsWith(File.separator) || path.endsWith(unixSeparator);
    }


    public static boolean startsWithUnixSeparator(String path) {
        return path.startsWith(unixSeparator);
    }

    public static boolean endsWithUnixSeparator(String path) {
        return path.endsWith(unixSeparator);
    }


    public static String[] splitWithUnixSeparator(String path) {
        return path.split(unixSeparator);
    }

    public static String joinWithUnixSeparator(CharSequence... segments) {
        return String.join(SeparatorUtil.unixSeparator, segments);
    }

    public static String joinWithUnixSeparator( Iterable<? extends CharSequence> segments) {
        return String.join(SeparatorUtil.unixSeparator, segments);
    }
}
