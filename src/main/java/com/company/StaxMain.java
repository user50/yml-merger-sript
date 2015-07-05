package com.company;

import com.company.config.Config;
import com.company.config.ConfigProvider;
import com.company.http.HttpService;
import com.company.merge.HttpClientProvider;
import com.company.stax.*;
import com.company.stax.conditions.WriteCategoryCondition;
import com.company.stax.conditions.WriteElementCondition;
import org.apache.http.impl.client.CloseableHttpClient;

import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by user50 on 04.07.2015.
 */
public class StaxMain {

    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        Config config = new ConfigProvider().get();

        CloseableHttpClient httpClient = new HttpClientProvider(config.getUser(), config.getPsw()).get();
        HttpService httpService = new HttpService(httpClient);

        List<XMLEventReaderProvider> readerProviders = new ArrayList<>();

        for (String url : config.getUrls())
            readerProviders.add(new HttpXMLEventReaderProvider(httpService, url, config.getEncoding()));

        for (String fileName : config.getFiles())
            readerProviders.add(new FileXMLEventReaderProvider(fileName, config.getEncoding()));

        Iterator<XMLEventReaderProvider> iterator = readerProviders.iterator();

        if (!iterator.hasNext())
            throw new RuntimeException("Must be specified at least one xml source");


        List<XmlEventHandler> handlers = new ArrayList<>();
        InElement inOffers = new InElement("offers");
        InElement inCategories = new InElement("categories");

        handlers.add(inOffers);
        handlers.add(inCategories);

        XMLOutputFactory ofactory = XMLOutputFactory.newFactory();
        XMLEventWriter out = ofactory.createXMLEventWriter(new FileOutputStream(config.getOutputFile()), config.getEncoding());

        Set<String> addedCategoryIds = new HashSet<>();

        CategoryIdsCollector categoryIdsCollector = new CategoryIdsCollector(addedCategoryIds);

        StAXService service = new StAXService(iterator.next());
        service.process(categoryIdsCollector);

        while (iterator.hasNext())
        {
            XMLEventReaderProvider readerProvider = iterator.next();
            ElementWriter offersElementWriter = new ElementWriter(new WriteElementCondition(new InElement("offers")), readerProvider);
            handlers.add(new ElementSourceHandler(offersElementWriter, out, inOffers));

            ElementWriter categoriesElementWriter = new ElementWriter(
                    new WriteCategoryCondition(new WriteElementCondition(new InElement("categories")), addedCategoryIds), readerProvider);
            handlers.add(new ElementSourceHandler(categoriesElementWriter, out, inCategories));
        }

        MultiXmlEventHandler multiEventHandler = new MultiXmlEventHandler(handlers, out);

        service.process(multiEventHandler);

        out.close();
    }
}
