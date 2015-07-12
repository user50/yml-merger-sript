package com.company.stax.providers;

import com.company.stax.StAXService;
import com.company.stax.XMLEventReaderProvider;
import com.company.stax.collectors.CategoriesCollector;
import com.company.stax.collectors.Category;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by user50 on 11.07.2015.
 */
public class IncludedCategoriesProvider {

    private List<XMLEventReaderProvider> readerProviders;
    private Set<String> categoryIds;

    public Set<String> get() throws FileNotFoundException, XMLStreamException {
        Set<Category> allCategories = new HashSet<>();
        CategoriesCollector categoriesCollector = new CategoriesCollector(allCategories);
        for (XMLEventReaderProvider readerProvider : readerProviders) {
            StAXService stAXService = new StAXService(readerProvider);
            stAXService.process(categoriesCollector);
        }

        Set<Category> categoriesFromConfig = new HashSet<>();
        for (Category category : allCategories) {
            if (categoryIds.contains(category.getId()))
                categoriesFromConfig.add(category);
        }

        ListMultimap<String, Category> multimap = ArrayListMultimap.create();

        for (Category category : categoriesFromConfig) {
            multimap.get(category.getParentId()).add(category);
        }



    }
}
