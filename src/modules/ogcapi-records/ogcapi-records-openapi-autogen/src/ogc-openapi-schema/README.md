This was taken from https://github.com/opengeospatial/ogcapi-records/tree/master/core/openapi

Unfortunately, the schemas there didn't generate "good" java.

Therefore, they were modified to produce "good" java objects.

General Changes

Building:

`mvn clean install`

This will build/package the code as well as run the test cases.

Method for developing (when changing the yaml):

1. validate
   openapi-generator-cli validate -i src/ogc-openapi-schema/openapi/ogcapi-records-1-example-ref-schema-repo.yaml
2. generate code
   rm -r delme ; openapi-generator-cli generate -g spring --generate-alias-as-model -i
   src/ogc-openapi-schema/openapi/ogcapi-records-1-example-ref-schema-repo.yaml -o delme -v

Verify:

1. No class should end in "1". If so, change the YAML so that you move something that was broken out to another file so
   that it's inline.
2. The only interface should be GeometryGeoJSON.java. If you see an interface, its likely due to an openapi "oneOf" in
   the schema. See if this is removable.
3. None of the definitions should be represented by a java "Object". If so, the YAML is nested too far. Take the schema
   definition and put it in its own file (cf. schemas/extracted) and $ref it. Check the Contact.java file as this is
   the "deepest" nested object.
4. Test json-string -> Java Object -> json-string with examples

Notes:

1. ID for GeoJSON Feature is now a string (instead of either string or number)
2. The YAML for GeometryGeoJSON was modified quite a bit - but it is, mostly, functionally equivalent.
  + added discriminator
  + .yaml filename for the "subclasses" (i.e. Point.yaml) MUST match the discriminator name
  + had to remove the "enum" for the "subclasses" (for exact type names). This was causing java inheritance issues.
3. Some of the "oneOf" definitions were removed. This causes some problem in the java generation - the differences in
   the model are minor (i.e. validation)
4. Roles were moved inline as it was a simple `List<String>` but wasn't generating correctly
5. Catalog#itemtype was defined as a either a `String` or  `List<String>`. Changed so its always a `List<String>`
6. BBOX - simplified to not have "oneOf" constraints
7. Time - simplified to only have one super-pattern instead of a bunch of "oneOf" sub-patterns

Configuring the Jackson JSON ObjectMapper:

See the test cases for how the Jackson JSON ObjectMapper is configured. Some of the configurations are to make the test
cases work easier (i.e. don't output `[]` or `null` items). However, the date-time configurations is required or you'll
get epoc-numbers instead of an ISO string for dates.

```
mapper.findAndRegisterModules();
mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
```
