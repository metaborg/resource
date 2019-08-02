package mb.resource;

import mb.resource.hierarchical.HierarchicalResource;
import mb.resource.hierarchical.ResourcePath;

public interface ResourceService {
    /**
     * Gets {@link Resource resource} for given {@link ResourceKey resource key}.
     *
     * @param key {@link ResourceKey Key} to get resource for.
     * @return Resource.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when the {@link ResourceRegistry resource registry} unexpectedly fails to get
     *                                  the resource.
     */
    Resource getResource(ResourceKey key);

    /**
     * Gets {@link ReadableResource readable resource} for given {@link ResourceKey resource key}.
     *
     * @param key {@link ResourceKey Key} to get readable resource for.
     * @return Readable resource.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when the {@link ResourceRegistry resource registry} unexpectedly fails to get
     *                                  the resource.
     * @throws ResourceRuntimeException when a resource is found, but it is not a {@link ReadableResource readable
     *                                  resource}.
     */
    ReadableResource getReadableResource(ResourceKey key);

    /**
     * Gets {@link WritableResource writable resource} for given {@link ResourceKey resource key}.
     *
     * @param key {@link ResourceKey Key} to get writable resource for.
     * @return Writable resource.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when the {@link ResourceRegistry resource registry} unexpectedly fails to get
     *                                  the resource.
     * @throws ResourceRuntimeException when a resource is found, but it is not a {@link WritableResource writable
     *                                  resource}.
     */
    WritableResource getWritableResource(ResourceKey key);

    /**
     * Gets {@link HierarchicalResource hierarchical resource} for given {@link ResourcePath resource path}.
     *
     * @param path {@link ResourcePath Path} to get hierarchical resource for.
     * @return Hierarchical resource.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourcePath#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when the {@link ResourceRegistry resource registry} unexpectedly fails to get
     *                                  the resource.
     * @throws ResourceRuntimeException when a resource is found, but it is not a {@link HierarchicalResource
     *                                  hierarchical resource}.
     */
    HierarchicalResource getHierarchicalResource(ResourcePath path);


    /**
     * Gets {@link ResourceKey resource key} for given string representation of the resource key.
     *
     * @param keyStr String representation of the resource key.
     * @return Resource key.
     * @throws ResourceRuntimeException when {@code keyStr} is not a valid string representation of a {@link ResourceKey
     *                                  resource key}.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourceKey#getQualifier() qualifier}.
     */
    ResourceKey getResourceKey(String keyStr);

    /**
     * Gets {@link ResourcePath resource path} for given string representation of the resource path.
     *
     * @param pathStr String representation of the resource path.
     * @return Resource path.
     * @throws ResourceRuntimeException when {@code pathStr} is not a valid string representation of a {@link
     *                                  ResourcePath resource path}.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourcePath#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when a resource key could be created, but it is not a {@link ResourcePath
     *                                  resource path}.
     */
    ResourcePath getResourcePath(String pathStr);


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
     */
    Resource getResource(String keyStr);

    /**
     * Gets a {@link ReadableResource readable resource} for given string representation of a {@link ResourceKey
     * resource key}.
     *
     * @param keyStr String representation of a resource key.
     * @return Readable resource.
     * @throws ResourceRuntimeException when {@code keyStr} is not a valid string representation of a {@link ResourceKey
     *                                  resource key}.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when the {@link ResourceRegistry resource registry} unexpectedly fails to get
     *                                  the resource.
     * @throws ResourceRuntimeException when a resource is found, but it is not a {@link ReadableResource readable
     *                                  resource}.
     */
    ReadableResource getReadableResource(String keyStr);

    /**
     * Gets a {@link WritableResource writable resource} for given string representation of a {@link ResourceKey
     * resource key}.
     *
     * @param keyStr String representation of a resource key.
     * @return Writable resource.
     * @throws ResourceRuntimeException when {@code keyStr} is not a valid string representation of a {@link ResourceKey
     *                                  resource key}.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when the {@link ResourceRegistry resource registry} unexpectedly fails to get
     *                                  the resource.
     * @throws ResourceRuntimeException when a resource is found, but it is not a {@link WritableResource writable
     *                                  resource}.
     */
    WritableResource getWritableResource(String keyStr);

    /**
     * Gets a {@link HierarchicalResource hierarchical resource} for given string representation of a {@link
     * ResourcePath resource path}.
     *
     * @param pathStr String representation of a resource path
     * @return Hierarchical resource.
     * @throws ResourceRuntimeException when {@code pathStr} is not a valid string representation of a {@link
     *                                  ResourcePath resource path}.
     * @throws ResourceRuntimeException when no {@link ResourceRegistry resource registry} is found for {@link
     *                                  ResourcePath#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when the {@link ResourceRegistry resource registry} unexpectedly fails to get
     *                                  the resource.
     * @throws ResourceRuntimeException when a resource is found, but it is not a {@link HierarchicalResource
     *                                  hierarchical resource}.
     */
    HierarchicalResource getHierarchicalResource(String pathStr);


    /**
     * If {@code pathOrKey} is a string representation of a {@link ResourceKey resource key}, returns a (replaced)
     * {@link Resource resource} for that key. Otherwise, returns a resource where given {@code pathOrKey} is appended
     * (or replaced with) as a path to given {@code resource} using {@link HierarchicalResource#appendOrReplaceWithPath(String)}.
     *
     * @param resource     Resource to attempt to append {@code pathOrKey} to.
     * @param keyStrOrPath String representation of a {@link ResourceKey key}, or path which should be appended to
     *                     {@code resource}.
     * @return Resource.
     * @throws ResourceRuntimeException when {@code keyStrOrPath} is a key and no {@link ResourceRegistry resource
     *                                  registry} is found for {@link ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when {@code keyStrOrPath} is a key and the {@link ResourceRegistry resource
     *                                  registry} unexpectedly fails to get the resource.
     * @throws ResourceRuntimeException when {@code keyStrOrPath} is a path and appending the path to {@code resource}
     *                                  fails unexpectedly.
     */
    Resource appendOrReplaceWith(HierarchicalResource resource, String keyStrOrPath);

    /**
     * If {@code pathOrKey} is a string representation of a {@link ResourcePath resource path}, returns a (replaced)
     * {@link HierarchicalResource hierarchical resource} for that path. Otherwise, returns a resource where given
     * {@code pathOrKey} is appended (or replaced with) as a path to given {@code resource} using {@link
     * HierarchicalResource#appendOrReplaceWithPath(String)}.
     *
     * @param resource      Resource to attempt to append {@code pathOrKey} to.
     * @param pathStrOrPath String representation of a {@link ResourcePath resource path}, or path which should be
     *                      appended to {@code resource}.
     * @return Hierarchical resource.
     * @throws ResourceRuntimeException when {@code pathStrOrPath} is a resource path and no {@link ResourceRegistry
     *                                  resource registry} is found for {@link ResourceKey#getQualifier() qualifier}.
     * @throws ResourceRuntimeException when {@code keyStrOrPath} is a resource path and the {@link ResourceRegistry
     *                                  resource registry} unexpectedly fails to get the resource.
     * @throws ResourceRuntimeException when {@code keyStrOrPath} is a resource path and a resource is found, but it is
     *                                  not a {@link HierarchicalResource hierarchical resource}.
     * @throws ResourceRuntimeException when {@code keyStrOrPath} is a path and appending the path to {@code resource}
     *                                  fails unexpectedly.
     */
    HierarchicalResource appendOrReplaceWithHierarchical(HierarchicalResource resource, String pathStrOrPath);


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
