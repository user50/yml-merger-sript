package com.company;

import com.company.config.Config;
import com.company.config.ConfigProvider;
import com.company.http.HttpService;
import com.company.merge.HttpClientProvider;
import com.company.stax.*;
import com.company.stax.collectors.CategoryIdsCollector;
import com.company.stax.factories.XmlEventMergerFactory;
import com.company.stax.providers.IncludedCategoriesProvider;
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

        Iterator<XMLEventReaderProvider> readerProvidersIterator = readerProviders.iterator();

        if (!readerProvidersIterator.hasNext())
            throw new RuntimeException("Must be specified at least one xml source");

        XMLOutputFactory ofactory = XMLOutputFactory.newFactory();
        XMLEventWriter mergedOut = ofactory.createXMLEventWriter(new FileOutputStream(config.getOutputFile()), config.getEncoding());

        Set<String> addedCategoryIds = new HashSet<>();

        CategoryIdsCollector categoryIdsCollector = new CategoryIdsCollector(addedCategoryIds);

        StAXService service = new StAXService(readerProvidersIterator.next());
        service.process(categoryIdsCollector);

        Set<String> idsFromConfig = new IncludedCategoriesProvider(readerProviders, config.getCategoryIds()).get();

        XmlEventMergerFactory mergerFactory = new XmlEventMergerFactory(readerProviders, mergedOut, idsFromConfig);

        service.process(mergerFactory.create());

        mergedOut.close();


    }
}
