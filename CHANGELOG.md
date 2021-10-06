# Changelog
All notable changes to this project are documented in this file, based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/).

## [Unreleased]
### Added
- `ClassLoaderToNativeResolver` to allow class loader resources to be resolved to resources native to the application, such as Eclipse resources. `FSResourceClassLoaderToNativeResolver` is the default implementation that resolves to local filesystem resources.
- `tryAsNativeResource` and `asNativeResource` to `ClassLoaderResource` that use the above resolver.


## [0.11.5]
### Added
- Tests for `(Qualified)ResourceKeyString` roundtrips.

### Changed
- `FSResourceRegistry` accepts local filesystem paths in `getResourceKey` and `getResource`.


[Unreleased]: https://github.com/metaborg/resource/compare/release-0.11.5...HEAD
[0.11.5]: https://github.com/metaborg/resource/compare/release-0.11.4...release-0.11.5
