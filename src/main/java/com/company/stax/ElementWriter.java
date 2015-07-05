package com.company.stax;

import com.company.stax.conditions.WriteEventCondition;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventConsumer;

/**
 * Created by user50 on 04.07.2015.
 */
public class ElementWriter {

    WriteEventCondition writeEventCondition;
    XMLEventReaderProvider readerProvider;

    public ElementWriter(WriteEventCondition writeEventCondition, XMLEventReaderProvider readerProvider) {
        this.writeEventCondition = writeEventCondition;
        this.readerProvider = readerProvider;
    }

    public void writeElements(XMLEventConsumer out) throws XMLStreamException {
        XMLEventReader reader = readerProvider.get();

        while (reader.hasNext()) {
            XMLEvent xmlEvent = reader.nextEvent();

            if (writeEventCondition.isSuitable(xmlEvent))
                out.add(xmlEvent);
        }
    }

}
