package com.company.stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by user50 on 04.07.2015.
 */
public class InElement implements XmlEventHandler {

    private String elementName;
    private boolean startElement;
    private boolean inElement;

    public InElement(String elementName) {
        this.elementName = elementName;
    }

    @Override
    public void handle(XMLEvent event) throws XMLStreamException {
        startElement = false;

        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals(elementName) )
        {
            startElement = true;
            inElement = true;
        }

        if (event.isEndElement() && event.asEndElement().getName().getLocalPart().equals(elementName) )
        {
            inElement = false;
        }
    }

    public boolean isInElement() {
        return inElement;
    }

    public boolean isStartElement() {
        return startElement;
    }
}
