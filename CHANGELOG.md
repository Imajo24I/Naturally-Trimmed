#### Config
- Reworked big parts of the config:
  - Config Fields are now named using the camelCase (e.g. `trimChance`) instead of the snake_case (e.g. `trim_chance`) naming scheme
    - The snake_case naming scheme still works tho, such that old configs won't break. Config files will be automatically migrated when the mod saves (most likely when saving via config screen)

  - Config Versioning, Deprecation and Migration:
      - The config is now versioned using a new `_version` field. Do not modify this field, unless you know what you're doing, as it's important for config migration
      - Config Fields from previous config schema's may now be deprecated. This means, the mod will continue to deserialize and load these config fields from the config file, however the mod won't save these config fields anymore. Deprecation is necessary for config migration:
      - Config Fields from previous config schema's may now be migrated. This means, deprecated config fields will now, where applicable, be automatically migrated to newer alternatives

  - Lots of internal refactoring

#### Improved Trim Filtering
- Replaced `material_filter` and `pattern_filter` with `trimFilter`
  - These two separate filters were quite limiting, due to being separate. You weren't able to handle anything, that requires both trim material and pattern. For example, you couldn't blacklist single specific trims, without blacklisting entire trim materials / patterns
  - `trimFilter` combines these filters into one, allowing for both all previously possible behavior and some new possibilities (for example blacklisting single specific armor trims, without having to blacklist entire trim materials / patterns)
  - See the according [wiki page](https://github.com/Imajo24I/Naturally-Trimmed/wiki/Config-%E2%80%90-3.6.0#trim-filter--trimfilter) for documentation about this config field
  - Due to being too 'complex', this new field is currently only possible to edit via the config file, and not in the screen.
  - `material_filter` and `pattern_filter` have been deprecated with automatic migration to `trimFilter`

- Removed `predefined_trims`
  - The new `trimFilter` also has the capability to do this, so `predefined_trims` is no longer necessary
  - See the according [wiki page](https://github.com/Imajo24I/Naturally-Trimmed/wiki/Config-%E2%80%90-3.6.0#trim-filter--trimfilter) for documentation about `trimFilter`
  - `predefined_trims` has been deprecated with automatic migration to `trimFilter`

- Removed `trim_system`
  - No longer necessary since `predefined_trims` has been removed
  - `trim_system` has been deprecated as it's needed in `predefined_trims`'s automatic migration

- Replaced `texture_validation_filtering` with `missingTextureFiltering`
  - This new config field allows configuring which strategy the mod uses to filter out missing-texture trims
  - It was previously very unclear, what kind of additional filtering the mod was actually doing, for users -> this config field makes this more transparent and configurable
  - See the according [wiki page](https://github.com/Imajo24I/Naturally-Trimmed/wiki/Config-%E2%80%90-3.6.0#missing-texture-filtering--missingtexturefiltering) for documentation about this config field
  - Since `texture_validation_filtering` was previously used to control part of this, it has been deprecated with automatic migration to `missingTextureFiltering`

- Renamed `trim_chance` to `pieceTrimChance` (`trimMobs` config category)
  - The previous name was very misleading
  - Automatic Migration exists

- Inverted and renamed `noTrimsChance` to `trimChance`
  - Now that `trim_chance` / `trimChance` is available, this rename (and inversion, to make the new name actually accurate) makes this config field's functionality much more clear
  - Automatic Migration exists

- Fixed precautionary trimming (see `missingTextureFiltering`) being unnecessarily restrictive
  - Instead of only allowing following trim combination: `(from vanilla) + (from any mod or vanilla)`, this now additionally allows for: `(from mod A) + (from mod A)`


#### Other
- Added support for 26.3
- Adjusted default config values, for trims to be rarer
- Added an `openWiki` button to the utils category of the config screen
- Updated the wiki, translations, descriptions and the README
