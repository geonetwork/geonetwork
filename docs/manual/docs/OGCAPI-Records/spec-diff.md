
### Differences with the Specification

There are some minor differences with the specification - these are mostly due to difficulties in the OGCAPI-Records OpenAPI .yaml (especially with `oneOf` OpenAI definitions).

Please see the comments in the README.md and .yaml files in the code autogeneration module (`src/modules/ogcapi-records/ogcapi-records-openapi-autogen/src/ogc-openapi-schema`).


1. The specification allows `ID`s to be either integers or strings.  We support incoming strings or integers, but they are always outputted as strings.
2. `bbox.yaml` slightly simplified
3. `time.yaml` slightly simplified by only having one time regex instead of several in a `oneOf`.
4. The `itemtype` property can either be a list of a single value.  This was simplified to be only a simple string property (no list).  This was originally simplified to be a list (most compatible), however, some clients do not accepts an `itemType` list so it was changed to be a single value.

There are other, minor changes.  Please report an issue if you see one.

Some of the conversions from the Elastic JSON Index Record to a GeoJSON record (or `catalog.yaml` object) are simplified and will need ongoing community discussion to ensure they are the best possible outcome.