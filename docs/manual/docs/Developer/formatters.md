# GeoNetwork Formatters

The GeoNetwork formatter system provides a flexible architecture for transforming metadata records into various output formats. This guide explains the basic design and how to implement custom formatters.

## Architecture Components

### 1. Formatter Interface

The core `Formatter` interface defines the contract for all formatters:

```java
public interface Formatter {
    void format(Metadata metadata, String formatterId, OutputStream outputStream, 
                Map<String, Object> config) throws IOException;
    
    boolean isFormatterAvailable(Metadata metadata, String formatterId);
}
```

**Key responsibilities:**
- Transform metadata into a specific output format
- Write the formatted content to an output stream
- Check formatter availability for given metadata

### 2. FormatterApi

The `FormatterApi` class provides high-level operations for formatting:

- **Access Control**: Validates user permissions before formatting
- **Formatter Discovery**: Lists available formatters for schemas and metadata
- **Formatter Selection**: Finds appropriate formatter based on ID and profile

**Key methods:**
- `getRecordFormattedBy()`: Main entry point for formatting a metadata record
- `getAvailableFormattersForSchema()`: Lists formatters for a specific schema
- `getAllFormatters()`: Returns all formatters organized by MIME type and profile

(Check FormatterApi class for more methods and updated informations)

## Formatter Types

### 1. Java-based Formatters (IndexFormatter Pattern)

These formatters work by programmatically transforming data using Java code.

**Example: IndexFormatter**
- Retrieves metadata from the search index or from the database
- Transforms using Java code or delegates to a processor
- Outputs JSON or custom format via processor

**Advantages:**
- Full programmatic control
- Can integrate with any Java library
- Easy unit testing

Relevant discussion [here](https://github.com/geonetwork/geonetwork/issues/50#issuecomment-2713199302)

### 2. XSLT-based Formatters (XsltFormatter Pattern)

These formatters use XSLT stylesheets to transform XML metadata, using legacy approach.

**Example: XsltFormatter**
- Loads XSLT
- Applies transformation to metadata XML
- Outputs transformed result

**Advantages:**
- Reuse of XML-to-XML transformations developer in [metadata101](https://github.com/metadata101) schemas
- Better for XML sources

**Disvantages:**
- XSLT availability depends on original schema of XML.

## Implementing a New Formatter

### Option 1: Java-based Formatter

1. **Create the formatter class:**

```java
@Component
public class MyCustomFormatter implements Formatter {
    
    @Override
    public void format(Metadata metadata, String formatterId, 
                      OutputStream outputStream, Map<String, Object> config) 
                      throws IOException {
        try {
            // 1. Extract data from metadata
            String metadataXml = metadata.getData();
            
            // 2. Transform the data
            String formatted = transformMetadata(metadataXml, config);
            
            // 3. Write to output stream
            outputStream.write(formatted.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            
        } catch (Exception e) {
            throw new FormatterException("Failed to format metadata", e);
        }
    }
    
    @Override
    public boolean isFormatterAvailable(Metadata metadata, String formatterId) {
        // Check if this formatter can handle the metadata
        return "myformat".equals(formatterId);
    }
    
    private String transformMetadata(String xml, Map<String, Object> config) {
        // Your transformation logic here
        // Can use config for parameters like language, style, etc.
        return transformedContent;
    }
}
```

2. **Register with FormatterFactory:**
   - Ensure your formatter is picked up by Spring's component scan
   - Configure in FormatterFactory if needed

### Option 2: XSLT-based Formatter

1. **Create XSLT stylesheet:**
   
   Place your XSLT file in the appropriate location:
   ```
   src/main/resources/schemas/{schema-id}/formatter/{formatter-id}/view.xsl
   ```

   Example XSLT:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <xsl:stylesheet version="2.0" 
                   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
       
       <xsl:output method="html" encoding="UTF-8"/>
       
       <xsl:template match="/">
           <html>
               <body>
                   <h1><xsl:value-of select="//title"/></h1>
                   <!-- Your transformation here -->
               </body>
           </html>
       </xsl:template>
   </xsl:stylesheet>
   ```

2. **Use XsltFormatter:**
   - The existing `XsltFormatter` will automatically pick up your XSLT
   - No Java code needed!

### Option 3: Custom Processor with IndexFormatter

For complex transformations that need index data:

1. **Create a processor:**

```java
@Component
public class MyIndexFormatterProcessor implements IndexFormatterProcessor {
    
    @Override
    public void process(IndexRecord indexDocument, OutputStream outputStream, 
                       Map<String, Object> config) throws IOException {
        // Transform index record to your format
        MyFormat result = convertToMyFormat(indexDocument);
        
        // Serialize and write
        objectMapper.writeValue(outputStream, result);
    }
}
```

2. **Register in IndexFormatterProcessorFactory:**
```java
public IndexFormatterProcessor getFormatterProcessor(String formatterId) {
    if ("myformat".equals(formatterId)) {
        return myIndexFormatterProcessor;
    }
    return null;
}
```

