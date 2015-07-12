import com.company.stax.XMLEventReaderProvider;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Yevhen on 2015-07-12.
 */
public class ResourceFileXmlEventReaderProvider implements XMLEventReaderProvider {
    private String fileName;
    private String encoding;

    public ResourceFileXmlEventReaderProvider(String fileName, String encoding) {
        this.fileName = fileName;
        this.encoding = encoding;
    }

    @Override
    public XMLEventReader get() {
        XMLInputFactory ifactory = XMLInputFactory.newFactory();
        ifactory.setProperty(XMLInputFactory.IS_VALIDATING, false);

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try {
            return ifactory.createXMLEventReader(new FileInputStream(file), encoding);
        } catch (XMLStreamException e) {
            throw new RuntimeException("Unable to process file " + fileName + "\n" + e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can not find file " + fileName);
        }
    }
}
