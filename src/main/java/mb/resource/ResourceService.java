package mb.resource;

import mb.resource.hierarchical.HierarchicalResource;

public interface ResourceService {
    /**
     * Gets {@link Resource resource} for given {@link ResourceKey resource key}.
     *
     * @param key {@link ResourceKey Key} to get resource for.
     * @param <R> Type of resource to retrieve.
     * @return Resource.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when the {@link ResourceRegistry resource registry} unexpectedly fails to get
     *                                  the resource.
     * @throws ClassCastException       when the {@link Resource resource} cannot be casted to {@code R}.
     */
    <R extends Resource> R getResource(ResourceKey key);

    /**
     * Gets {@link Resource resource} for given string representation of a {@link ResourceKey resource key}.
     *
     * @param keyStr String representation of a {@link ResourceKey resource key}.
     * @return Resource.
     * @throws ResourceRuntimeException when {@code keyStr} is not a valid string representation of a {@link ResourceKey
     *                                  resource key}.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when the {@link ResourceRegistry resource registry} unexpectedly fails to get
     *                                  the resource.
     * @throws ClassCastException       when the {@link Resource resource} cannot be casted to {@code R}.
     */
    <R extends Resource> R getResource(String keyStr);

    /**
     * If {@code pathOrKey} is a string representation of a {@link ResourceKey resource key}, returns a (replaced)
     * {@link Resource resource} for that key. Otherwise, returns a resource where given {@code pathOrKey} is appended
     * (or replaced with) as a path to given {@code resource} using {@link HierarchicalResource#appendOrReplaceWithPath(String)}.
     *
     * @param resource     Resource to attempt to append {@code pathOrKey} to.
     * @param keyStrOrPath String representation of a {@link ResourceKey key}, or path which should be appended to
     *                     {@code resource}.
     * @return Resource result.
     * @throws ResourceRuntimeException when {@code keyStrOrPath} is a key and no {@link ResourceRegistry resource
     *                                  registry} is found for {@link ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when {@code keyStrOrPath} is a key and the {@link ResourceRegistry resource
     *                                  registry} unexpectedly fails to get the resource.
     * @throws ResourceRuntimeException when {@code keyStrOrPath} is a path and appending the path to {@code resource}
     *                                  fails unexpectedly.
     * @throws ClassCastException       when the {@link Resource resource} cannot be casted to {@code R}.
     */
    <R extends Resource> R appendOrReplaceWith(HierarchicalResource resource, String keyStrOrPath);

    /**
     * Converts given {@link ResourceKey resource key} into its string representation.
     *
     * @param key {@link ResourceKey Key} to convert to its string representation.
     * @return String representation of given {@link ResourceKey key}.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when the {@link ResourceRegistry resource registry} unexpectedly fails to
     *                                  convert the identifier of the key into its string representation.
     */
    String toStringRepresentation(ResourceKey key);
}
