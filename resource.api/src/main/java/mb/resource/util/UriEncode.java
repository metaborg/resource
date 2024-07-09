package mb.resource.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

/**
 * URI encoding utility. Adapted from http://stackoverflow.com/a/10032289/499240.
 */
public final class UriEncode {
    /**
     * Encodes a string for use as a {@link URI}. Encodes all characters except the following:
     * <ul>
     *     <li>alphabetic (a-z, A-Z)</li>
     *     <li>decimal digits (0-9)</li>
     *     <li>!</li>
     *     <li>'</li>
     *     <li>(</li>
     *     <li>)</li>
     *     <li>*</li>
     *     <li>-</li>
     *     <li>.</li>
     *     <li>/</li>
     *     <li>:</li>
     *     <li>\</li>
     *     <li>_</li>
     *     <li>~</li>
     * </ul>
     *
     * @param input A URI as a unencoded string.
     * @return Encoded URI string.
     */
    public static String encodeFull(String input) {
        return encode(input, doNotEncodeFull);
    }

    /**
     * Encodes a string with {@link #encodeFull} and returns it as a {@link URI}.
     */
    public static URI encodeToUri(String input) throws URISyntaxException {
        return new URI(encodeFull(input));
    }

    /**
     * Encodes a string for use as a component in a {@link URI}. Encodes all characters except the following:
     * <ul>
     *     <li>alphabetic (a-z, A-Z)</li>
     *     <li>decimal digits (0-9)</li>
     *     <li>!</li>
     *     <li>'</li>
     *     <li>(</li>
     *     <li>)</li>
     *     <li>*</li>
     *     <li>-</li>
     *     <li>.</li>
     *     <li>_</li>
     *     <li>~</li>
     * </ul>
     *
     * @param input A component of a URI as an unencoded string.
     * @return Encoded URI component string.
     */
    public static String encodeComponent(String input) {
        return encode(input, doNotEncodeComponent);
    }


    private static final BitSet doNotEncodeComponent;
    private static final BitSet doNotEncodeFull;

    static {
        doNotEncodeComponent = new BitSet(256);

        // a-z
        for(int i = 97; i <= 122; ++i) {
            doNotEncodeComponent.set(i);
        }
        // A-Z
        for(int i = 65; i <= 90; ++i) {
            doNotEncodeComponent.set(i);
        }
        // 0-9
        for(int i = 48; i <= 57; ++i) {
            doNotEncodeComponent.set(i);
        }

        doNotEncodeComponent.set(33); // !
        // '()*
        for(int i = 39; i <= 42; ++i) {
            doNotEncodeComponent.set(i);
        }
        doNotEncodeComponent.set(45); // -
        doNotEncodeComponent.set(46); // .
        doNotEncodeComponent.set(95); // _
        doNotEncodeComponent.set(126); // ~

        doNotEncodeFull = new BitSet(256);
        doNotEncodeFull.or(doNotEncodeComponent);
        doNotEncodeFull.set(47); // /
        doNotEncodeFull.set(58); // :
        doNotEncodeFull.set(92); // \
    }

    private static String encode(String input, BitSet doNotEncode) {
        final StringBuilder filtered = new StringBuilder(input.length());
        char c;
        for(int i = 0; i < input.length(); ++i) {
            c = input.charAt(i);
            if(doNotEncode.get(c)) {
                filtered.append(c);
            } else {
                final byte[] b = charToBytesUTF(c);
                for(byte value : b) {
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
