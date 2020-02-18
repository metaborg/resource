package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * String representation of a {@link ResourceKey resource key}, where the qualifier may be missing, indicating that the
 * qualifier is irrelevant, can be derived from the context, or that a default should be used.
 */
public interface ResourceKeyString {
    /**
     * Creates a resource key string from given identifier string representation.
     *
     * @param id String representation for the identifier of the key.
     * @return Resource key string.
     */
    static ResourceKeyString of(String id) {
        return new DefaultResourceKeyString(id);
    }

    /**
     * Creates a resource key string from given string representations of a qualifier and identifier.
     *
     * @param qualifier String representation for the qualifier of the key, or {@code null}/empty if missing.
     * @param id        String representation for the identifier of the key.
     * @return Resource key string.
     */
    static ResourceKeyString of(@Nullable String qualifier, String id) {
        return new DefaultResourceKeyString(qualifier != null ? (qualifier.isEmpty() ? null : qualifier) : null, id);
    }

    /**
     * Parses given {@link String} into a resource key string. Intended to be used to turn the string representation
     * produced by {@link #toString()} back into this resource key string.
     *
     * @param keyStr {@link String} representation of a (partial) resource key string.
     * @return Resource key string parsed from given string.
     */
    public static ResourceKeyString parse(String keyStr) {
        final String[] split = keyStr.split(DefaultQualifiedResourceKeyString.separator, 2);
        if(split.length < 2) {
            return new DefaultResourceKeyString(keyStr);
        }
        final String qualifier = split[0];
        return new DefaultResourceKeyString(qualifier.isEmpty() ? null : qualifier, split[1]);
    }

    /**
     * Creates a resource key string, as a {@link String}, from given string representations of a qualifier and
     * identifier.
     *
     * @param qualifier String representation for the qualifier of the key, or {@code null}/empty if missing.
     * @param id        String representation for the identifier of the key.
     * @return Resource key string as a {@link String}.
     */
    static String toString(@Nullable String qualifier, String id) {
        return ((qualifier == null || qualifier.isEmpty()) ? "" : qualifier) + DefaultQualifiedResourceKeyString.separator + id;
    }


    /**
     * Gets the string representation for the qualifier of the key, or {@code null} when the qualifier is missing.
     *
     * @return String representation for the qualifier of the key, or {@code null} when missing.
     */
    @Nullable String getQualifier();

    /**
     * Checks whether the qualifier is set (not missing).
     *
     * @return True when qualifier is set (not missing), false otherwise.
     */
    default boolean hasQualifier() { return getQualifier() != null; }

    /**
     * Checks whether given qualifier matches the qualifier in this resource key string. Always returns false when this
     * resource key string's qualifier is missing.
     *
     * @return True when qualifier matches, false otherwise.
     */
    default boolean qualifierMatches(String qualifier) { return getQualifier() != null && qualifier.equals(getQualifier()); }

    /**
     * Checks whether given qualifier matches the qualifier in this resource key string, or whether it is missing.
     *
     * @return True when qualifier matches or is missing, false otherwise.
     */
    default boolean qualifierMatchesOrMissing(String qualifier) { return getQualifier() == null || qualifier.matches(getQualifier()); }

    /**
     * Gets the string representation for the identifier of the key.
     *
     * @return String representation for the identifier of the key.
     */
    String getId();


    /**
     * Creates a qualified resource key string from this resource key string.
     *
     * @param qualifier String representation for the qualifier of the key.
     * @return Resource key string without qualifier.
     * @throws ResourceRuntimeException when qualifier is empty.
     */
    default QualifiedResourceKeyString withQualifier(String qualifier) {
        return QualifiedResourceKeyString.of(qualifier, getId());
    }


    /**
     * Creates a {@link String} representation of this resource key string. This string representation can be converted
     * back into this object using {@link #parse(String)}.
     *
     * @return {@link String} representation of this resource key string.
     */
    @Override String toString();
}
