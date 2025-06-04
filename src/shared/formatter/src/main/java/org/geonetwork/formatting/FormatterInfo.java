package org.geonetwork.formatting;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FormatterInfo {

  String mimeType;
  List<String> schemas;


  public FormatterInfo() {
    schemas = new ArrayList<>();
  }
}
