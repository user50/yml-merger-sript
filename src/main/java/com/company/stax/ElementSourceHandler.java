package com.company.stax;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventConsumer;

/**
 * Created by user50 on 04.07.2015.
 */
public class ElementSourceHandler implements XmlEventHandler {

    private ElementWriter elementWriter;
    private XMLEventConsumer out;
    private InElement inElement;

    private boolean called;

    public ElementSourceHandler(ElementWriter elementWriter, XMLEventConsumer out, InElement inElement) {
        this.elementWriter = elementWriter;
        this.out = out;
        this.inElement = inElement;
    }

    @Override
    public void handle(XMLEvent event) throws XMLStreamException {
        if (called || !inElement.isInElement() || inElement.isStartElement())
            return;

        elementWriter.writeElements(out);

        called = true;
    }


}
