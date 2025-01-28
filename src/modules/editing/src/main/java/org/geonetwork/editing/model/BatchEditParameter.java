/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@XmlRootElement(name = "edit")
@Schema(description = "Batch edit parameter. Use either xpath or property to identify the element to edit.")
public class BatchEditParameter implements Serializable {
    @Schema(description = "XPath to the element to edit")
    private String xpath;

    @Schema(description = "Property in the index object of the element to edit")
    private String property;

    @Schema(description = "Insertion mode and value.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String value;

    @Schema(
            description =
                    "XPath condition to evaluate against the metadata record to decide if edit should be applied.")
    private String condition;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("XPath: ");
        sb.append(this.xpath).append(System.lineSeparator());
        if (StringUtils.isNotEmpty(this.value)) {
            sb.append("XML or value: ").append(this.value).append(System.lineSeparator());
        }
        sb.append("Condition: ").append(this.condition).append(System.lineSeparator());
        return sb.toString();
    }
}
