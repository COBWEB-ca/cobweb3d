package cobweb3d.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


class CobwebXmlHelper {

    /**
     * Creates an XML document with given root name and namespace.
     * The namespace is nested within the cobweb2 namespace.
     * Cobweb version is included as an attribute.
     *
     * @param rootName   name of root element
     * @param rootSchema sub-namespace for root element
     * @return root Element of the new Document. Use root.getOwnerDocument() to get the document
     */
    public static Element createDocument(String rootName, String rootSchema) {
        Document d;
        try {
            d = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        Element root = d.createElementNS("http://cobweb.ca/schema/cobweb2/" + rootSchema, rootName);
        // TODO: root.setAttribute("cobweb-version", Versionator.getVersion());
        d.appendChild(root);

        return root;
    }

    public static void writeDocument(OutputStream stream, Document d) {
        Source s = new DOMSource(d);

        Transformer t;
        TransformerFactory tf = TransformerFactory.newInstance();
        try {
            t = tf.newTransformer();

        } catch (TransformerConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty(OutputKeys.STANDALONE, "yes");
        t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        Result r = new StreamResult(stream);
        try {
            t.transform(s, r);
        } catch (TransformerException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Opens XML file and returns the root node.
     * Strips whitespace elements after load.
     *
     * @param file where to read the document from
     * @return root node of document
     */
    public static Element openDocument(InputStream file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setIgnoringComments(true);
        factory.setValidating(false); // TODO: Make this true when a DTD Schema is available.

        Document document;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(file);
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            throw new IllegalArgumentException("Can't open config file", ex);
        }

        Element root = document.getDocumentElement();
        stripWhitespaceNodes(root);

        return root;
    }


    private static void stripWhitespaceNodes(Element parent) {
        Node nextNode = parent.getFirstChild();
        for (Node child = parent.getFirstChild(); nextNode != null; ) {
            child = nextNode;
            nextNode = child.getNextSibling();
            if (child.getNodeType() == Node.TEXT_NODE) {
                if (child.getTextContent().matches("^\\s*$")) {
                    parent.removeChild(child);
                }
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                stripWhitespaceNodes((Element) child);
            }
        }
    }


}
