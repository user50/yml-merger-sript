package com.company.stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventConsumer;
import java.util.List;

/**
 * Created by user50 on 27.06.2015.
 */
public class MultiXmlEventHandler implements XmlEventHandler {
    private XMLEventConsumer out;

    private List<XmlEventHandler> handlers;

    public MultiXmlEventHandler(List<XmlEventHandler> handlers, XMLEventConsumer out) {
        this.handlers = handlers;
        this.out = out;
    }

    @Override
    public void handle(XMLEvent event) throws XMLStreamException {
        for (XmlEventHandler handler : handlers) {
            handler.handle(event);
        }

        out.add(event);
    }
}
