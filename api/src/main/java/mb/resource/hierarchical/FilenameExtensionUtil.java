package mb.resource.hierarchical;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Function;

public class FilenameExtensionUtil {
    public static @Nullable String getExtension(String filename) {
        final int i = filename.lastIndexOf('.');
        if(i > 0) {
            return filename.substring(i + 1);
        }
        return null;
    }

    public static boolean hasExtension(String filename, String extension) {
        final @Nullable String fileExtension = getExtension(filename);
        if(fileExtension == null) return false;
        return fileExtension.equals(extension);
    }

    public static String replaceExtension(String filename, String extension) {
        final int i = filename.lastIndexOf('.');
        if(i < 0) {
            return filename;
        }
        final String leafNoExtension = filename.substring(0, i);
        return leafNoExtension + "." + extension;
    }

    public static String ensureExtension(String filename, String extension) {
        final int i = filename.lastIndexOf('.');
        if(i < 0) {
            return filename + "." + extension;
        }
        final String leafNoExtension = filename.substring(0, i);
        return leafNoExtension + "." + extension;
    }

    public static String appendExtension(String filename, String extension) {
        return filename + "." + extension;
    }

    public static String removeExtension(String filename) {
        final int i = filename.lastIndexOf('.');
        if(i < 0) {
            return filename;
        }
        return filename.substring(0, i);
    }

    public static String applyToExtension(String filename, Function<String, String> func) {
        final int i = filename.lastIndexOf('.');
        if(i < 0) {
            return filename;
        }
        final String extension = filename.substring(i + 1);
        final String newExtension = func.apply(extension);
        final String leafNoExtension = filename.substring(0, i);
        return leafNoExtension + "." + newExtension;
    }
}
