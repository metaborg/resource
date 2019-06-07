package mb.resource;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;

public interface ResourceKey extends Serializable {
    /**
     * Gets the qualifier of the key.
     *
     * @return Qualifier of the key.
     */
    String getQualifier();

    /**
     * Gets the identifier of the key.
     *
     * @return Identifier of the key.
     */
    Serializable getId();

    /**
     * Gets the string representation of the identifier of the key.
     *
     * @return Identifier of the key in String format.
     * @implNote The string representation may not contain the reserved character sequence '##'.
     */
    String getIdStringRepresentation();


    @Override boolean equals(@Nullable Object other);

    @Override int hashCode();

    /**
     * Gets the string representation of this key.
     *
     * @return String representation of this key.
     * @implNote Implementors must return the result of {@link #toStringRepresentation(ResourceKey)} as the
     * implementation of this method.
     */
    @Override String toString();


    /**
     * Gets the string representation of this key.
     *
     * @return String representation of this key.
     * @apiNote Should only be used by implementors.
     * @implNote Implementors of this class must use this method in their {@link #toString()} implementation.
     */
    static String toStringRepresentation(ResourceKey key) {
        return key.getQualifier() + "##" + key.getIdStringRepresentation();
    }
}
