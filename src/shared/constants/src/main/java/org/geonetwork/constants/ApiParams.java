/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.constants;

/** A set of static variables describing API parameters. */
public class ApiParams {
    public static final String API_CLASS_RECORD_TAG = "records";
    public static final String API_CLASS_RECORD_TAG_DESCRIPTION = """
All operations related to metadata records.
""";
    public static final String API_CLASS_RECORD_OPS = "Metadata record operations";
    public static final String API_CLASS_CATALOG_OPS = "Catalog operations";
    public static final String API_CLASS_CATALOG_TAG = "site";
    public static final String API_CLASS_REGISTRIES_OPS = "Registries related operations";
    public static final String API_CLASS_REGISTRIES_TAG = "registries";
    public static final String API_CLASS_TOOLS_TAG = "tools";
    public static final String API_CLASS_TOOLS_OPS = "Utility operations";
    public static final String API_CLASS_FORMATTERS_TAG = "formatters";
    public static final String API_CLASS_FORMATTERS_OPS = "Formatter operations";

    public static final String API_PARAM_RECORD_UUID = "Record UUID.";
    public static final String API_PARAM_RECORD_UUIDS = "One or more record UUIDs";
    public static final String API_PARAM_RECORD_UUIDS_OR_SELECTION = "Record UUIDs. If null current selection is used.";
    public static final String API_PARAM_CLEAR_ALL_BEFORE_INSERT = "Clear all before adding new ones";
    public static final String API_PARAM_SCHEMA_IDENTIFIERS = "Schema identifiers";
    public static final String API_PARAM_PROCESS_TEST_ONLY =
            "Test only (ie. metadata are not saved). Return the report only.";
    public static final String API_PARAM_PROCESS_ID = "Process identifier";
    public static final String API_OP_NOTE_PROCESS = "Process a metadata with an XSL transformation declared in "
            + "the metadata schema (See the process folder). "
            + "Parameters sent to the service are forwarded "
            + "to XSL process.";
    public static final String API_OP_NOTE_PROCESS_PREVIEW =
            "Preview result of a process applied to metadata records with an XSL transformation declared in "
                    + "the metadata schema (See the process folder). "
                    + "Parameters sent to the service are forwarded "
                    + "to XSL process. Append mode has 2 limitations. First, it only support a set of records "
                    + "in the same schema. Secondly, it does not propagate URL parameters. This mode is mainly "
                    + "used to create custom reports based on metadata records content."
                    + "If process name ends with '.csv', the XSL process output a text document which is returned.";
    public static final String API_PARAM_METADATA_TITLE = "Metadata title";
    public static final String API_PARAM_MAPSERVER_RESOURCE = "Resource name (could be a file or a db connection)";
    public static final String API_PARAM_METADATA_ABSTRACT = "Metadata abstract";
    public static final String API_PARAM_ISO_3_LETTER_CODE = "ISO 3 letter code";

    public static final String API_RESPONSE_NOT_ALLOWED_ONLY_ADMIN =
            "Operation not allowed. Only Administrators can access it.";
    public static final String API_RESPONSE_NOT_ALLOWED_ONLY_USER_ADMIN =
            "Operation not allowed. Only UserAdmins can access it.";
    public static final String API_RESPONSE_NOT_ALLOWED_ONLY_REVIEWER =
            "Operation not allowed. Only Reviewvers can access it.";
    public static final String API_RESPONSE_NOT_ALLOWED_ONLY_EDITOR =
            "Operation not allowed. Only Editors can access it.";
    public static final String API_RESPONSE_NOT_ALLOWED_CAN_EDIT =
            "Operation not allowed. User needs to be able to edit the resource.";
    public static final String API_RESPONSE_NOT_ALLOWED_CAN_VIEW =
            "Operation not allowed. User needs to be able to view the resource.";
    public static final String API_RESPONSE_NOT_ALLOWED_ONLY_AUTHENTICATED =
            "Operation not allowed. Only authenticated user can access it.";
    public static final String API_RESPONSE_RESOURCE_NOT_FOUND = "Resource not found.";
    public static final String API_PARAM_BUCKET_NAME = "Selection bucket name (not supported in GeoNetwork 5)";
    public static final String API_PARAM_UPDATE_DATESTAMP =
            """
If true updates the record date stamp with the current time of the operation.
Date stamp element in each standards can be different. eg. In ISO19139 it is `gmd:dateStamp`,
in ISO19115-3 it is the revision date of the metadata.
""";
}
