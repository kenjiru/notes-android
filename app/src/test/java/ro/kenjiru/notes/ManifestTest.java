package ro.kenjiru.notes;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ro.kenjiru.notes.xml.Manifest;
import ro.kenjiru.notes.xml.NoteEntry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ManifestTest {
    @Test
    public void readFromXmlString() {
        String xmlStr = "<?xml version=\"1.0\" ?><sync revision=\"0\" server-id=\"42323b80-4c53-4737-a81f-f8b40ee4a2fd\"><note id=\"d21223a1-c8df-4f30-9ee7-b344657d68d6\" rev=\"0\"></note><note id=\"e04fdb88-189d-498f-9b8b-d97f955153f0\" rev=\"1\"></note><note id=\"8141e855-89e7-42e0-851a-fc2d9d4b8495\" rev=\"2\"></note></sync>\n";
        Manifest manifest = convertStringToObject(xmlStr);

        assertNotNull(manifest);

        assertEquals("42323b80-4c53-4737-a81f-f8b40ee4a2fd", manifest.getServerId());
        assertEquals(0, manifest.getRevision());
        assertEquals(3, manifest.getNotes().size());

        List<NoteEntry> notes = manifest.getNotes();
        assertEquals("d21223a1-c8df-4f30-9ee7-b344657d68d6", notes.get(0).getId());
        assertEquals(0, notes.get(0).getRevision());

        assertEquals("e04fdb88-189d-498f-9b8b-d97f955153f0", notes.get(1).getId());
        assertEquals(1, notes.get(1).getRevision());

        assertEquals("8141e855-89e7-42e0-851a-fc2d9d4b8495", notes.get(2).getId());
        assertEquals(2, notes.get(2).getRevision());

    }

    private Manifest convertStringToObject(String xmlStr) {
        XStream xStream = new XStream(new StaxDriver());
        xStream.processAnnotations(Manifest.class);
        xStream.processAnnotations(NoteEntry.class);
        Manifest manifest = null;

        try {
            InputStream is = new ByteArrayInputStream(xmlStr.getBytes("UTF-16"));

            manifest = (Manifest) xStream.fromXML(is);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return manifest;
    }

    @Test
    public void persistToXml() {
        String xmlStr = this.convertObjectToXmlString();
        Document dom = this.convertStringToDocument(xmlStr);

        assertNotNull(dom);

        Element root = dom.getDocumentElement();

        assertEquals("sync", root.getTagName());
        assertEquals(2, root.getAttributes().getLength());

        assertEquals("0", root.getAttribute("revision"));
        assertEquals("42323b80-4c53-4737-a81f-f8b40ee4a2fd", root.getAttribute("server-id"));

        NodeList childNotes = root.getChildNodes();
        assertEquals(3, childNotes.getLength());

        Element first = (Element) childNotes.item(0);
        assertEquals("note", first.getTagName());
        assertEquals("d21223a1-c8df-4f30-9ee7-b344657d68d6", first.getAttribute("id"));
        assertEquals("0", first.getAttribute("rev"));

        Element second = (Element) childNotes.item(1);
        assertEquals("note", second.getTagName());
        assertEquals("e04fdb88-189d-498f-9b8b-d97f955153f0", second.getAttribute("id"));
        assertEquals("1", second.getAttribute("rev"));

        Element third = (Element) childNotes.item(2);
        assertEquals("note", third.getTagName());
        assertEquals("8141e855-89e7-42e0-851a-fc2d9d4b8495", third.getAttribute("id"));
        assertEquals("2", third.getAttribute("rev"));
    }

    private Document convertStringToDocument(String xmlStr) {
        Document dom = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource is = new InputSource(new StringReader(xmlStr));
            dom = builder.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dom;
    }

    private String convertObjectToXmlString() {
        XStream xStream = new XStream(new StaxDriver());
        xStream.autodetectAnnotations(true);

        Manifest manifest = new Manifest();
        manifest.setRevision(0);
        manifest.setServerId("42323b80-4c53-4737-a81f-f8b40ee4a2fd");

        List<NoteEntry> noteEntries = new ArrayList<NoteEntry>();
        noteEntries.add(new NoteEntry("d21223a1-c8df-4f30-9ee7-b344657d68d6", 0));
        noteEntries.add(new NoteEntry("e04fdb88-189d-498f-9b8b-d97f955153f0", 1));
        noteEntries.add(new NoteEntry("8141e855-89e7-42e0-851a-fc2d9d4b8495", 2));

        manifest.setNotes(noteEntries);

        return xStream.toXML(manifest);
    }
}
