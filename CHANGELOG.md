# Changelog
All notable changes to this project are documented in this file, based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).


## [Unreleased]


## [0.14.1] - 2022-05-11
### Added
- `FileExtensionUtil.hasExtension`.


## [0.14.0] - 2022-03-25
### Removed
- `HierarchicalResource#openWrite` and `HierarchicalResource#openWriteNew`, as their implementations were dubious.


## [0.13.2] - 2021-11-10
### Changed
- All `PathMatcher`s to work with UNIX path segment separators `/`.

### Added
- `AllResourceWalker`, `AnyResourceWalker`, `NotResourceWalker`.
- `EndsWithPathMatcher`, `StartsWithPatchMatcher`, `LeafPathMatcher`.


## [0.13.1] - 2021-11-09
### Fixed
- Fix `ClassLoaderResource`s `getLastModifiedTime` and `getSize` methods closing the backing JAR file, which could still be used concurrently by different threads, thereby fixing stream closed exceptions.


## [0.13.0] - 2021-10-18
### Added
- `ClassLoaderResource#getLocationsTryAsNative` to get class loader resource locations as native resources.

### Changed
- `ClassLoaderResource#tryAsNativeResource` to `tryAsNativeFile`.
- `ClassLoaderResource#asNativeResource` to `asNativeFile`.
- `ClassLoaderResourceLocations` and `JarFileWithPath` to take a type argument with the kind of resources they hold.
- `ClassLoaderToNativeResolver` to have separate methods for resolving files and directories.


## [0.12.0] - 2021-10-11
### Added
- `ClassLoaderToNativeResolver` to allow class loader resources to be resolved to resources native to the application, such as Eclipse resources. `FSResourceClassLoaderToNativeResolver` is the default implementation that resolves to local filesystem resources.
- `tryAsNativeResource` and `asNativeResource` to `ClassLoaderResource` that use the above resolver.


## [0.11.5] - 2021-09-01
### Added
- Tests for `(Qualified)ResourceKeyString` roundtrips.

### Changed
- `FSResourceRegistry` accepts local filesystem paths in `getResourceKey` and `getResource`.


[Unreleased]: https://github.com/metaborg/resource/compare/release-0.14.1...HEAD
[0.14.1]: https://github.com/metaborg/resource/compare/release-0.14.0...release-0.14.1
[0.14.0]: https://github.com/metaborg/resource/compare/release-0.13.2...release-0.14.0
[0.13.2]: https://github.com/metaborg/resource/compare/release-0.13.1...release-0.13.2
[0.13.1]: https://github.com/metaborg/resource/compare/release-0.13.0...release-0.13.1
[0.13.0]: https://github.com/metaborg/resource/compare/release-0.12.0...release-0.13.0
[0.12.0]: https://github.com/metaborg/resource/compare/release-0.11.5...release-0.12.0
[0.11.5]: https://github.com/metaborg/resource/compare/release-0.11.4...release-0.11.5
