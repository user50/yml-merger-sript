package com.company.stax.factories;

import com.company.config.Config;
import com.company.stax.*;
import com.company.stax.conditions.WriteCategoryByIdsCondition;
import com.company.stax.conditions.WriteCategoryCondition;
import com.company.stax.conditions.WriteElementCondition;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.util.XMLEventConsumer;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by user50 on 11.07.2015.
 */
public class XmlEventMergerFactory implements XmlEventHandlerFactory {
    private List<XMLEventReaderProvider> readerProviders;
    private XMLEventConsumer mergedOut;
    private Set<String> addedCategoryIds;
    private Set<String> idsFromConfig;

    public XmlEventMergerFactory(List<XMLEventReaderProvider> readerProviders,
                                 XMLEventConsumer mergedOut, Set<String> idsFromConfig) {
        this.readerProviders = readerProviders;
        this.mergedOut = mergedOut;
        this.idsFromConfig = idsFromConfig;
    }

    @Override
    public XmlEventHandler create() {
        Iterator<XMLEventReaderProvider> readerProvidersIterator = readerProviders.iterator();

        if (!readerProvidersIterator.hasNext())
            throw new RuntimeException("Must be specified at least one xml source");

        List<XmlEventHandler> handlers = new ArrayList<>();
        InElement inOffers = new InElement("offers");
        InElement inCategories = new InElement("categories");

        handlers.add(inOffers);
        handlers.add(inCategories);

        Set<String> addedIds = new HashSet<>();

        while (readerProvidersIterator.hasNext())
        {
            XMLEventReaderProvider readerProvider = readerProvidersIterator.next();
            ElementWriter offersElementWriter = new ElementWriter(new WriteElementCondition(new InElement("offers")), readerProvider);
            handlers.add(new ElementSourceHandler(offersElementWriter, mergedOut, inOffers));

            ElementWriter categoriesElementWriter = new ElementWriter(
                    new WriteCategoryByIdsCondition(
                            new WriteElementCondition(new InElement("categories")), idsFromConfig, addedIds), readerProvider);
            handlers.add(new ElementSourceHandler(categoriesElementWriter, mergedOut, inCategories));
        }


        return new MultiXmlEventHandler(handlers, mergedOut);
    }
}
