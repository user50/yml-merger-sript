package com.company.stax.conditions;

import com.company.stax.InElement;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;


/**
 * Created by user50 on 04.07.2015.
 */
public class WriteElementCondition implements WriteEventCondition {

    InElement inElement;

    public WriteElementCondition(InElement inElement) {
        this.inElement = inElement;
    }

    @Override
    public boolean isSuitable(XMLEvent xmlEvent) throws XMLStreamException {
        inElement.handle(xmlEvent);

        return inElement.isInElement() && !inElement.isStartElement();
    }
}
