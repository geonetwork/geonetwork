/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.utility.xml;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
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
            File xsltFile,
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
            File xsltFile,
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

    /** Transform XML string to Object. */
    public static <T> T transformXmlToObject(
            String inputXmlString,
            File xsltFile,
            Class<T> objectClass,
            Map<QName, net.sf.saxon.s9api.XdmValue> xslParameters) {
        String xmlAsString = transformXmlAsString(inputXmlString, xsltFile, xslParameters);
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.readValue(xmlAsString, objectClass);
        } catch (Exception e) {
            log.atError().log(e.getMessage());
            throw new RuntimeException(e);
        }
        //    return null;
        //    TransformerFactory factory = XsltTransformerFactory.get();
        //    StreamSource xslt = new StreamSource(xsltFile);
        //    StreamSource text = new StreamSource(new StringReader(inputXmlString));
        //    try {
        //      JAXBContext jaxbContext = JAXBContext.newInstance(objectClass);
        //      JAXBResult result = new JAXBResult(jaxbContext);
        //
        //      Transformer transformer = factory.newTransformer(xslt);
        //      transformer.transform(text, result);
        //      Object o = result.getResult();
        //      if (objectClass.isInstance(o)) {
        //        return (T) o;
        //      } else {
        //        return null;
        //      }
        //    } catch (TransformerConfigurationException e) {
        //      e.printStackTrace();
        //    } catch (TransformerException e2) {
        //      e2.printStackTrace();
        //    } catch (JAXBException e) {
        //      throw new RuntimeException(e);
        //    }
        //    return null;
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
            String inputXmlString, File xsltFile, Map<QName, net.sf.saxon.s9api.XdmValue> xslParameters) {
        try {
            Processor proc = XsltTransformerFactory.getProcessor();
            XsltCompiler compiler = proc.newXsltCompiler();

            XsltExecutable xsl = compiler.compile(new StreamSource(xsltFile));
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
