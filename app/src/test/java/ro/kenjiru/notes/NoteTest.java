package ro.kenjiru.notes;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ro.kenjiru.notes.xml.Note;
import ro.kenjiru.notes.xml.util.DomConverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NoteTest {
    @Test
    public void restoreFromXmlString() throws ParseException {
        String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?><note version=\"0.3\" " +
                "xmlns:link=\"http://beatniksoftware.com/tomboy/link\" " +
                "xmlns:size=\"http://beatniksoftware.com/tomboy/size\" " +
                "xmlns=\"http://beatniksoftware.com/tomboy\"><title>Android build</title>" +
                "<text xml:space=\"preserve\"><note-content version=\"0.1\">" +
                "Android buildBuild error</note-content></text>" +
                "<last-change-date>2016-11-30T00:20:40.1150460+01:00</last-change-date>" +
                "<last-metadata-change-date>2016-11-30T00:20:40.1174570+01:00</last-metadata-change-date>" +
                "<create-date>2016-11-30T00:20:06.2798990+01:00</create-date>" +
                "<cursor-position>27</cursor-position><selection-bound-position>29</selection-bound-position>" +
                "<width>450</width><height>360</height><x>0</x><y>0</y>" +
                "<open-on-startup>False</open-on-startup></note>\n";
        System.out.println(xmlStr);

        Note note = convertStringToObject(xmlStr);
        assertNotNull(note);

        Document noteText = note.getText();
        assertNotNull(noteText);

        Element root = noteText.getDocumentElement();
        assertNotNull(root);

        assertEquals("text", noteText.getDocumentElement().getTagName());
        assertEquals(1, root.getChildNodes().getLength());

        Element noteContent = (Element) root.getFirstChild();
        assertNotNull(noteContent);
        assertEquals("0.1", noteContent.getAttribute("version"));
        assertEquals("Android buildBuild error", noteContent.getTextContent());

        assertEquals(getDate("2016-11-30T00:20:40.1150460+01:00"), note.getLastChangeDate());
        assertEquals(getDate("2016-11-30T00:20:40.1174570+01:00"), note.getLastMetadataChangeDate());
        assertEquals(getDate("2016-11-30T00:20:06.2798990+01:00"), note.getCreateDate());

        assertEquals(27, note.getCursorPosition());
        assertEquals(29, note.getSelectionBoundPositionPosition());
        assertEquals(450, note.getWidth());
        assertEquals(360, note.getHeight());
        assertEquals(0, note.getX());
        assertEquals(0, note.getY());
        assertEquals(false, note.isOpenOnStartup());
    }

    private Date getDate(String dateStr) throws ParseException {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setLenient(true);

        return simpleDateFormat.parse(dateStr);
    }

    private Note convertStringToObject(String xmlStr) {
        XStream xStream = new XStream(new StaxDriver());
        xStream.registerConverter(new DomConverter());
        xStream.registerConverter(new DateConverter("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSX", new String[0], true));
        xStream.processAnnotations(Note.class);
        xStream.ignoreUnknownElements();

        Note note = null;

        try {
            InputStream is = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));

            note = (Note) xStream.fromXML(is);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return note;
    }

    @Test
    public void persistToXml() {
        Note note = new Note();
        note.setVersion(0.3f);

        String xmlStr = this.convertObjectToXmlString(note);

        Document dom = this.convertStringToDocument(xmlStr);
        assertNotNull(dom);

        Element root = dom.getDocumentElement();
        assertEquals("note", root.getTagName());
        assertEquals(0.3f, root.getAttributes().getLength(), 2);
    }


    private String convertObjectToXmlString(Note note) {
        QNameMap qNameMap = new QNameMap();
        qNameMap.setDefaultNamespace("http://beatniksoftware.com/tomboy");

//        qNameMap.registerMapping(new QName("http://beatniksoftware.com/tomboy/link", "internal", "link"),
//                "internal");

        StaxDriver staxDriver = new StaxDriver(qNameMap);

        XStream xStream = new XStream(staxDriver);
        xStream.autodetectAnnotations(true);
        xStream.ignoreUnknownElements();

        return xStream.toXML(note);
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

}
