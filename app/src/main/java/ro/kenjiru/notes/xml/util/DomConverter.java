package ro.kenjiru.notes.xml.util;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomReader;
import com.thoughtworks.xstream.io.xml.DomWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Serializes and deserializes part of the XStream as a DOM Document or Element object.
 */
public class DomConverter implements Converter {
    public boolean canConvert(Class clazz) {
        return Document.class.isAssignableFrom(clazz) || Element.class.isAssignableFrom(clazz);
    }

    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        DomReader reader;

        if (object instanceof Document) {
            Document doc = (Document) object;
            reader = new DomReader(doc);
        } else {
            Element element = (Element) object;
            reader = new DomReader(element);
        }

        copy(reader, writer);
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ConversionException("Cannot instantiate " + Document.class.getName(), e);
        }

        Document document = documentBuilder.newDocument();
        DomWriter writer = new DomWriter(document);

        copy(reader, writer);

        if (Document.class.isAssignableFrom(unmarshallingContext.getRequiredType())) {
            return document;
        } else {
            return document.getDocumentElement();
        }
    }

    public static void copy(HierarchicalStreamReader reader, HierarchicalStreamWriter writer) {
        writer.startNode(reader.getNodeName());

        int attributeCount = reader.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = reader.getAttributeName(i);
            String attributeValue = reader.getAttribute(i);

            writer.addAttribute(attributeName, attributeValue);
        }

        while (reader.hasMoreChildren()) {
            reader.moveDown();
            copy(reader, writer);
            reader.moveUp();
        }

        String value = reader.getValue();
        if (value != null && value.trim().length() > 0) {
            writer.setValue(value);
        }

        writer.endNode();
    }
}