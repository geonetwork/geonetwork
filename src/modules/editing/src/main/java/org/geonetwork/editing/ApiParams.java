/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

public class ApiParams {
    public static final String API_OP_BATCH_DESCRIPTION =
            """
Batch editing API allows to apply multiple edits to one or more record.

**Warning: You can break things here. When defining xpath and using delete or replace mode,
be sure to test on a single record before applying changes to a lot of records.
If needed, back up your record first and use the preview mode to check the processing.**

Changes are defined on a per standard basis so it is recommended to apply changes
on records which are encoded using the same standard.

When applying changes, user privileges apply, so if the user cannot edit a selected record,
batch edits will not be applied to that record.

This operations applies the `update-fixed-info.xsl` transformation for the metadata schema and
 updates the change date if the parameter `updateDateStamp` is set to `true`.

## Changes with GeoNetwork 4

* `diffType` not yet supported.

```
""";

    public static final String API_OP_BATCH_BODY =
            """
The edits to apply to the records. The body is a list of `BatchEditParameter` objects.
XML snippet MUST declare namespace they are using to be valid.
""";

    public static final String API_OP_BATCH_PREVIEW_RESPONSE =
            """
In preview mode, the response is an XML document with the modified records.

```xml
<preview>
    <gmd:MD_Metadata ...
    <gmd:MD_Metadata ...
</preview>
```
""";

    public @interface BatchApiRequestBodyDoc {}
}
