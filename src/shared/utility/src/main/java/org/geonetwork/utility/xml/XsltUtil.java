/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.utility.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Xslt30Transformer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import org.springframework.stereotype.Component;

/** XSLT utility class. */
@Component
@Slf4j
public class XsltUtil {

    /**
     * Transform Object to Object.
     *
     * <p>Using Jackson XML mapper, Maybe JAXB can be better ?
     */
    public static <T> T transformObjectToObject(
            Object inputObject,
            URL xsltFile,
            Class<T> objectClass,
            Map<QName, net.sf.saxon.s9api.XdmValue> xslParameters) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            String xmlAsString =
                    transformXmlAsString(xmlMapper.writeValueAsString(inputObject), xsltFile, xslParameters);
            return xmlMapper.readValue(xmlAsString, objectClass);
        } catch (Exception e) {
            log.atError().log(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /** Transform Object to String. */
    public static <T> String transformObjectToString(
            Object inputObject,
            URL xsltFile,
            Class<T> objectClass,
            Map<QName, net.sf.saxon.s9api.XdmValue> xslParameters) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return transformXmlAsString(xmlMapper.writeValueAsString(inputObject), xsltFile, xslParameters);

        } catch (Exception e) {
            log.atError().log(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /** Transform XML string and write result to XMLStreamWriter. */
    public static void transformAndStreamInDocument(
            String inputXmlString,
            InputStream xsltFile,
            XMLStreamWriter streamWriter,
            Map<QName, net.sf.saxon.s9api.XdmValue> xslParameters) {
        try {
            Processor proc = XsltTransformerFactory.getProcessor();
            XsltCompiler compiler = proc.newXsltCompiler();

            XsltExecutable xsl = compiler.compile(new StreamSource(xsltFile));
            Xslt30Transformer transformer = xsl.load30();
            if (xslParameters != null) {
                transformer.setStylesheetParameters(xslParameters);
            }
            transformer.transform(
                    new StreamSource(new StringReader(inputXmlString)),
                    new XmlStreamWriterDestinationInDocument(streamWriter));
        } catch (SaxonApiException e) {
            log.atError().log(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /** Transform XML string in OutputStream. */
    public static void transformXmlAsOutputStream(
            String inputXmlString,
            InputStream xsltFile,
            Map<QName, net.sf.saxon.s9api.XdmValue> xslParameters,
            OutputStream outputStream) {
        try {
            Processor proc = XsltTransformerFactory.getProcessor();
            XsltCompiler compiler = proc.newXsltCompiler();

            XsltExecutable xsl = compiler.compile(new StreamSource(xsltFile));
            Xslt30Transformer transformer = xsl.load30();
            if (xslParameters != null) {
                transformer.setStylesheetParameters(xslParameters);
            }
            transformer.transform(new StreamSource(new StringReader(inputXmlString)), proc.newSerializer(outputStream));
        } catch (SaxonApiException e) {
            log.atError().log(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /** Transform XML string as String. */
    public static String transformXmlAsString(
            String inputXmlString, URL xsltFile, Map<QName, net.sf.saxon.s9api.XdmValue> xslParameters) {
        try {
            Processor proc = XsltTransformerFactory.getProcessor();
            XsltCompiler compiler = proc.newXsltCompiler();

            XsltExecutable xsl = compiler.compile(new StreamSource(xsltFile.toString()));
            Xslt30Transformer transformer = xsl.load30();
            if (xslParameters != null) {
                transformer.setStylesheetParameters(xslParameters);
            }
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new StreamSource(new StringReader(inputXmlString)), proc.newSerializer(stringWriter));
            return stringWriter.toString();
        } catch (SaxonApiException e) {
            log.atError().log(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
