package com.company.stax.conditions;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * Created by user50 on 04.07.2015.
 */
public interface WriteEventCondition {

    boolean isSuitable(XMLEvent xmlEvent) throws XMLStreamException;

}
