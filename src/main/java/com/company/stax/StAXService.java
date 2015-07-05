package com.company.stax;


import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;

/**
 * Created by user50 on 27.06.2015.
 */
public class StAXService {

    private XMLEventReaderProvider readerProvider;

    public StAXService(XMLEventReaderProvider readerProvider) {
        this.readerProvider = readerProvider;
    }

    public void process(XmlEventHandler eventHandler) throws FileNotFoundException, XMLStreamException {
        XMLEventReader in = readerProvider.get();

        while(in.hasNext()){
            XMLEvent e = in.nextEvent();
            eventHandler.handle(e);
        }
    }
}
