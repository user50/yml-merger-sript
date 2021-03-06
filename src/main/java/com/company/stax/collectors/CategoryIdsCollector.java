package com.company.stax.collectors;

import com.company.stax.XmlEventHandler;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by user50 on 27.06.2015.
 */
public class CategoryIdsCollector implements XmlEventHandler {

    private boolean inCategory;
    private String currentId;

    Set<String> categoryIds;

    public CategoryIdsCollector(Set<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public void handle(XMLEvent event) throws XMLStreamException {
        if (event.isStartElement() && event.asStartElement().getName().getLocalPart().equals("category") )
        {
            Iterator<Attribute> attributesIterator = event.asStartElement().getAttributes();

            while (attributesIterator.hasNext()){
                Attribute attribute = attributesIterator.next();
                if(attribute.getName().toString().equals("id"))
                    currentId = attribute.getValue();
            }
            inCategory = true;
        }

        if (inCategory && event.isEndElement() && event.asEndElement().getName().getLocalPart().equals("category") )
        {
            categoryIds.add(currentId);
            inCategory = false;
        }

    }
}
