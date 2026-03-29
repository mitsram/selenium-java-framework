package com.framework.api;

import com.framework.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Validates SOAP response bodies against an XSD schema (extracted from WSDL).
 * Uses only JDK built-in javax.xml.validation — zero extra dependencies.
 *
 * Usage:
 *   WsdlSchemaValidator.validate(responseXml, "AddResponse");
 */
public final class WsdlSchemaValidator {

    private static final Logger log = LoggerFactory.getLogger(WsdlSchemaValidator.class);
    private static volatile Validator validator;

    private WsdlSchemaValidator() {}

    private static Validator getValidator() {
        if (validator == null) {
            synchronized (WsdlSchemaValidator.class) {
                if (validator == null) {
                    try {
                        String xsdPath = ConfigManager.get("soap.schema.path");
                        InputStream xsd = WsdlSchemaValidator.class.getClassLoader()
                                .getResourceAsStream(xsdPath);
                        if (xsd == null) {
                            throw new IllegalStateException("XSD schema not found: " + xsdPath);
                        }
                        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                        Schema schema = factory.newSchema(new StreamSource(xsd));
                        validator = schema.newValidator();
                    } catch (SAXException e) {
                        throw new IllegalStateException("Failed to load XSD schema", e);
                    }
                }
            }
        }
        return validator;
    }

    /**
     * Validates the SOAP body element against the XSD schema.
     *
     * @param soapResponseXml full SOAP envelope XML string
     * @param expectedElement the expected root element inside soap:Body (e.g. "AddResponse")
     * @throws AssertionError if the response does not conform to the schema
     */
    public static void validate(String soapResponseXml, String expectedElement) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            // Prevent XXE attacks
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            Document doc = dbf.newDocumentBuilder()
                    .parse(new ByteArrayInputStream(soapResponseXml.getBytes(StandardCharsets.UTF_8)));

            // Extract the first child of soap:Body
            NodeList bodyChildren = doc.getElementsByTagNameNS("http://schemas.xmlsoap.org/soap/envelope/", "Body");
            if (bodyChildren.getLength() == 0) {
                throw new AssertionError("SOAP Body element not found in response");
            }

            Node bodyContent = null;
            NodeList children = bodyChildren.item(0).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    bodyContent = children.item(i);
                    break;
                }
            }

            if (bodyContent == null) {
                throw new AssertionError("No element found inside SOAP Body");
            }

            String actualElement = bodyContent.getLocalName();
            if (!expectedElement.equals(actualElement)) {
                throw new AssertionError(
                        "Expected SOAP body element '%s' but got '%s'"
                                .formatted(expectedElement, actualElement));
            }

            // Validate the body content element against XSD
            getValidator().validate(new DOMSource(bodyContent));
            log.info("SOAP contract validation PASSED for <{}>", expectedElement);

        } catch (SAXException e) {
            throw new AssertionError("SOAP contract validation FAILED: " + e.getMessage(), e);
        } catch (AssertionError e) {
            throw e;
        } catch (Exception e) {
            throw new AssertionError("SOAP contract validation error: " + e.getMessage(), e);
        }
    }
}
