package ro.kenjiru.notes.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.InputStream;

import ro.kenjiru.notes.xml.util.DomConverter;

public class ConversionUtils {
    public static Manifest toManifest(InputStream is) {
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(Manifest.class);
        xStream.processAnnotations(NoteEntry.class);

        return (Manifest) xStream.fromXML(is);
    }

    public static Note toNote(InputStream is) {
        XStream xStream = new XStream(new DomDriver());
        xStream.registerConverter(new DomConverter());
        xStream.registerConverter(new DateConverter("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ", new String[]{
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ",
                "yyyy-MM-dd'T'HH:mm:ssZ"
        }, true));
        xStream.processAnnotations(Note.class);
        xStream.ignoreUnknownElements();

        return (Note) xStream.fromXML(is);
    }
}
