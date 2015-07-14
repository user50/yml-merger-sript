package com.company.stax.conditions;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Yevhen on 2015-07-12.
 */
public class WriteCategoryByIdsCondition implements WriteEventCondition {

    private WriteElementCondition writeElementCondition;
    private Set<String> ids;
    private Set<String> addedIds;

    private String currentId;

    public WriteCategoryByIdsCondition(WriteElementCondition writeElementCondition, Set<String> ids, Set<String> addedIds) {
        this.writeElementCondition = writeElementCondition;
        this.ids = ids;
        this.addedIds = addedIds;
    }

    @Override
    public boolean isSuitable(XMLEvent xmlEvent) throws XMLStreamException {
        boolean condition = writeElementCondition.isSuitable(xmlEvent);

        if(!condition)
            return false;

        if (xmlEvent.isStartElement() && xmlEvent.asStartElement().getName().getLocalPart().equals("category")){
            currentId = getCategoryId(xmlEvent);
        }

        boolean result = currentId != null && ids.contains(currentId) && !addedIds.contains(currentId);

        if (xmlEvent.isEndElement() && xmlEvent.asEndElement().getName().getLocalPart().equals("category")){
            if (ids.contains(currentId))
            addedIds.add(currentId);
            currentId = null;
        }

        return result;
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
