package ch.softenvironment.util;
/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Utility for DOM-handling. Based on JAXP.
 *
 * @author Peter Hirzel
 */
@Slf4j
public final class DOMUtils {

    private DOMUtils() {
        throw new IllegalStateException("utility class");
    }

    /**
     * Default Error-Handler for DOM-Parser.
     * <p>
     * see #readDOM()
     */
    private static class DefaultErrorHandler implements ErrorHandler {

        private String getDetailedMessage(SAXParseException error) {
            return "Row=" + error.getLineNumber() +
                "\n SystemId=" + error.getSystemId() +
                (error.getPublicId() == null ? "" : "\n PublicId=" + error.getPublicId()) +
                "\n errorMessage=" + error.getMessage() +
                (error.getCause() == null ? "" : "\n cause=" + error.getCause());
        }

        @Override
        public void warning(SAXParseException error) {
            log.warn("{}", getDetailedMessage(error));
        }

        @Override
        public void error(SAXParseException error) {
            log.error("{}", getDetailedMessage(error), error);
        }

        @Override
        public void fatalError(SAXParseException error) {
            log.error("Developer error: {}", getDetailedMessage(error));
        }
    }

    /**
     * Create a new instance of a DOM-Document builder.
     *
     * @param validating
     * @return
     * @throws ParserConfigurationException
     */
    public static DocumentBuilder createDocumentBuilder(boolean validating) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        log.debug("DocumentBuilderFactory={}", factory);
        factory.setValidating(validating);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new DefaultErrorHandler());
        log.debug("DocumentBuilder={}", builder);
        return builder;
    }

    /**
     * Transform given XML-instance (data) by given XSLT-Rule-set (templates) and write the result into given writer.
     * <p>
     * Xalan-Java is an XSLT processor for transforming XML documents into HTML, text, or other XML document types. It implements XSL Transformations (XSLT) Version 1.0 and XML Path Language (XPath)
     * Version 1.0.
     *
     * @param xml (XML-Instance filename for e.g. myData.xml)
     * @param xsl (XSLT rule filename for e.g. myTransformRules.xsl)
     * @param writer (for e.g. StringWriter or BufferedWriter)
     * @return StringWriter (containing transformed Stream)
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws DeveloperException (if given files are not readable)
     */
    public static void transform(String xml, String xsl, Writer writer) throws TransformerConfigurationException, TransformerException, DeveloperException {
        File xmlFile = new File(xml);
        File xsltFile = new File(xsl);
        if (xmlFile.canRead() && xsltFile.canRead()) {
            TransformerFactory transFactory = TransformerFactory.newInstance();
            StreamSource xmlSrc = new StreamSource(xmlFile);
            StreamSource xsltSrc = new StreamSource(xsltFile);
            Transformer transformer = transFactory.newTransformer(xsltSrc);
            StreamResult result = new StreamResult(writer);
            transformer.transform(xmlSrc, result);
        } else {
            throw new DeveloperException("one or both filenames not readable");
        }
    }

    /**
     * Parse given filename (for e.g. MyFile.xml containing DOM-tree) and return resulting DOM-Document.
     *
     * @param filename
     * @param builder (null => @see #createDocumentBuilder(boolean))
     * @return Document (or null if filename not readable)
     * @throws ParserConfigurationException
     * @throws SAXParseException
     * @throws IOException
     * @throws SAXException
     */
    public static Document read(String filename, DocumentBuilder builder) throws ParserConfigurationException, SAXParseException, IOException, SAXException {
        File file = new File(filename);
        if (file.canRead()) {
            try {
                DocumentBuilder tmp = builder;
                if (tmp == null) {
                    // create default builder
                    tmp = createDocumentBuilder(false);
                }
                org.w3c.dom.Document document = tmp.parse(file);
                return document;
            } catch (SAXParseException pe) {
                log.error("{} Parse-Error (row={})", filename, pe.getLineNumber(), pe);
                throw pe;
            } catch (IOException ioe) {
                log.error("{}", filename, ioe);
                throw ioe;
            } catch (SAXException saxe) {
                log.error("{}", filename, saxe);
                throw saxe;
            }
        } else {
            log.warn("not existing or readable: {}", filename);
        }

        return null;
    }

    /**
     * Write given DOM-Document into writer.
     *
     * @param document
     * @param outStream (for e.g. FileWriter or StringWriter)
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public static void writeXML(Document document, OutputStream/*Writer*/ outStream) throws TransformerConfigurationException, TransformerException {
        Transformer trn = TransformerFactory.newInstance().newTransformer();
        trn.setOutputProperty(OutputKeys.METHOD, "xml");
        trn.setOutputProperty(OutputKeys.ENCODING, "UTF-8");    // default anyway!
        trn.setOutputProperty(OutputKeys.INDENT, "yes");
        //      trn.setOutputProperty(OutputKeys.VERSION, "1.0");       // default for output method "xml"
        //      trn.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "publicId");
        //      trn.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "systemId");

        // use a FileOutputStream rather than a FileWriter. The latter applies its own encoding, 
        // which is almost certainly not UTF-8 (depending on your platform, it's probably Windows-1252 or IS-8859-1).
        StreamResult result = new StreamResult(outStream);
        trn.transform(new DOMSource(document), result);
    }
}