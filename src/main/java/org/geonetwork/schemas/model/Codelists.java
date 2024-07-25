package org.geonetwork.schemas.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Codelists model. */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"noNamespaceSchemaLocation"})
@JacksonXmlRootElement(localName = "codelists")
public class Codelists {
  @JacksonXmlProperty(localName = "codelist")
  @JacksonXmlElementWrapper(useWrapping = false)
  List<Codelist> codelist;
}
