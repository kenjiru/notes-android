package ro.kenjiru.notes.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.InputStream;

public class ConversionUtils {
    public static Manifest toManifest(InputStream is) {
        XStream xStream = new XStream(new DomDriver());
        xStream.processAnnotations(Manifest.class);
        xStream.processAnnotations(NoteEntry.class);

        return (Manifest) xStream.fromXML(is);
    }
}
