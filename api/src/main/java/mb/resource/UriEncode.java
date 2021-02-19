package mb.resource;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * URI encoding utility. From http://stackoverflow.com/a/10032289/499240.
 */
final class UriEncode {
    private static final BitSet pathAllowed;

    static {
        pathAllowed = new BitSet(256);

        // a-z
        for(int i = 97; i <= 122; ++i) {
            pathAllowed.set(i);
        }
        // A-Z
        for(int i = 65; i <= 90; ++i) {
            pathAllowed.set(i);
        }
        // 0-9
        for(int i = 48; i <= 57; ++i) {
            pathAllowed.set(i);
        }

        // '()*
        for(int i = 39; i <= 42; ++i) {
            pathAllowed.set(i);
        }
        pathAllowed.set(33); // !
        pathAllowed.set(45); // -
        pathAllowed.set(46); // .
        pathAllowed.set(95); // _
        pathAllowed.set(126); // ~
    }


    /**
     * Escapes all characters except the following: alphabetic, decimal digits, - _ . ! ~ * ' ( )
     *
     * @param input A component of a URI
     * @return the escaped URI component
     */
    public static String encodePath(String input) {
        StringBuilder filtered = new StringBuilder(input.length());
        char c;
        for(int i = 0; i < input.length(); ++i) {
            c = input.charAt(i);
            if(pathAllowed.get(c)) {
                filtered.append(c);
            } else {
                final byte[] b = charToBytesUTF(c);
                for(final byte value : b) {
                    filtered.append('%');
                    filtered.append("0123456789ABCDEF".charAt(value >> 4 & 0xF));
                    filtered.append("0123456789ABCDEF".charAt(value & 0xF));
                }
            }
        }
        return filtered.toString();
    }

    private static byte[] charToBytesUTF(char c) {
        return new String(new char[]{c}).getBytes(StandardCharsets.UTF_8);
    }
}
