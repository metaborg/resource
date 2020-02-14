package mb.resource;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * String representation of a {@link ResourceKey resource key}.
 */
public interface QualifiedResourceKeyString extends ResourceKeyString {
    String separator = "##";


    /**
     * Creates a qualified resource key string from given string representations of a qualifier and identifier.
     *
     * @param qualifier String representation for the qualifier of the key.
     * @param id        String representation for the identifier of the key.
     * @return Qualified resource key string.
     * @throws ResourceRuntimeException when qualifier is empty.
     */
    static QualifiedResourceKeyString of(String qualifier, String id) {
        if(qualifier.isEmpty()) {
            throw new ResourceRuntimeException("Cannot create a qualified resource key string, qualifier is empty");
        }
        return new DefaultQualifiedResourceKeyString(qualifier, id);
    }

    /**
     * Parses given {@link String} into a qualified resource key string. Intended to be used to turn the string
     * representation produced by {@link #toString()} back into this qualified resource key string.
     *
     * @param keyStr {@link String} representation of a qualified resource key string.
     * @return Qualified resource key string parsed from given string.
     * @throws ResourceRuntimeException when given string cannot be parsed into a qualified resource key string.
     */
    static QualifiedResourceKeyString parse(String keyStr) {
        final String[] split = keyStr.split(separator, 2);
        if(split.length < 2) {
            throw new ResourceRuntimeException("Cannot parse '" + keyStr + "' into a qualified resource key string, separator '" + separator + "' is missing");
        }
        final String qualifier = split[0];
        if(qualifier.isEmpty()) {
            throw new ResourceRuntimeException("Cannot parse '" + keyStr + "' into a qualified resource key string, qualifier is missing");
        }
        return new DefaultQualifiedResourceKeyString(qualifier, split[1]);
    }

    /**
     * Creates a qualified resource key string, as a {@link String}, from given string representations of a qualifier and identifier.
     *
     * @param qualifier String representation for the qualifier of the key.
     * @param id        String representation for the identifier of the key.
     * @return Qualified resource key string as a {@link String}.
     * @throws ResourceRuntimeException when qualifier is empty.
     */
    static String toString(String qualifier, String id) {
        if(qualifier.isEmpty()) {
            throw new ResourceRuntimeException("Cannot create a qualified resource key string, qualifier is empty");
        }
        return qualifier + separator + id;
    }


    /**
     * Gets the string representation for the qualifier of the key.
     *
     * @return String representation for the qualifier of the key.
     */
    @Override @NonNull String getQualifier();

    /**
     * Gets the string representation for the identifier of the key.
     *
     * @return String representation for the identifier of the key.
     */
    @Override String getId();


    /**
     * Creates a resource key string without qualifier from this qualified resource key string.
     *
     * @return Resource key string without qualifier.
     */
    default ResourceKeyString withoutQualifier() {
        return ResourceKeyString.of(getId());
    }


    /**
     * Creates a {@link String} representation of this qualified resource key string. This string representation can be
     * converted back into this object using {@link #parse(String)}.
     *
     * @return {@link String} representation of this qualified resource key string.
     */
    @Override String toString();
}
