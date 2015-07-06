package com.company.stax.conditions;

import com.company.stax.InElement;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by user50 on 04.07.2015.
 */
public class WriteCategoryCondition implements WriteEventCondition {

    WriteElementCondition writeElementCondition;
    Set<String> addedCategoryIds;

    private String currentId;

    public WriteCategoryCondition(WriteElementCondition writeElementCondition, Set<String> addedCategoryIds) {
        this.writeElementCondition = writeElementCondition;
        this.addedCategoryIds = addedCategoryIds;
    }

    @Override
    public boolean isSuitable(XMLEvent xmlEvent) throws XMLStreamException {
        boolean condition = writeElementCondition.isSuitable(xmlEvent);

        if(!condition)
            return false;

        if (xmlEvent.isStartElement() && xmlEvent.asStartElement().getName().getLocalPart().equals("category")){
            currentId = getCategoryId(xmlEvent);
        }

        if (xmlEvent.isEndElement() && xmlEvent.asEndElement().getName().getLocalPart().equals("category")){
            addedCategoryIds.add(currentId);
        }

        return !addedCategoryIds.contains(currentId);
    }

    private String getCategoryId(XMLEvent xmlEvent) {
        Iterator<Attribute> attributesIterator = xmlEvent.asStartElement().getAttributes();

        while (attributesIterator.hasNext()){
            Attribute attribute = attributesIterator.next();
            if (attribute.getName().toString().equals("id")){
                return attribute.getValue();
            }
        }

        throw new RuntimeException("Id ia required attribute of category");
    }
}
